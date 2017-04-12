package rocks.inspectit.ui.rcp.editor.map.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import rocks.inspectit.shared.all.tracing.constants.MobileTags;
import rocks.inspectit.shared.all.tracing.data.AbstractSpan;
import rocks.inspectit.ui.rcp.InspectITConstants;
import rocks.inspectit.ui.rcp.editor.inputdefinition.InputDefinition;
import rocks.inspectit.ui.rcp.editor.map.filter.FilterTypeMapping;
import rocks.inspectit.ui.rcp.editor.map.filter.MapFilter;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITClusterMarker;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITMarker;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITSpanMarker;
import rocks.inspectit.ui.rcp.editor.map.model.MapSettings;

public abstract class AbstractMapInputController implements MapInputController {

	/**
	 * A map which holds the markers clustered to specific coordinate spaces.
	 */
	Map<Coordinate, List<InspectITMarker>> coordSys = new HashMap<Coordinate, List<InspectITMarker>>();

	/**
	 * A list of all available data markers.
	 */
	List<InspectITMarker> displayedMarkers = new ArrayList<InspectITMarker>();

	/**
	 * The current mapFilter.
	 */
	Map<String, MapFilter> filterTypes;

	/**
	 * The current selected tag.
	 */
	String selectedTag = InspectITConstants.NOFILTER;

	/**
	 * The input definition.
	 */
	protected InputDefinition inputDefinition;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInputDefinition(InputDefinition inputDefinition) {
		Assert.isNotNull(inputDefinition);
		this.inputDefinition = inputDefinition;
	}

	/**
	 * Default constructor for this abstract map input controller.
	 *
	 */
	public AbstractMapInputController() {
		displayedMarkers = new ArrayList<>();
		filterTypes = new HashMap<>();
		resetFilters();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SubViewClassification getSubViewClassification() {
		return this.getSubViewClassification();
	}

	/**
	 * Creates and returns a circle marker with the given coordinate and radius.
	 *
	 * @param coord
	 *            The coordinate the marker should be displayed at.
	 * @param rad
	 *            The radius for circle marker.
	 * @return The created circle marker.
	 */
	private InspectITMarker getCircle(Coordinate coord, double rad) {
		return new InspectITClusterMarker(null, coord, rad * MapSettings.getInstance().getClusteringCoefficient());
	}

	private void resetFilters() {
		filterTypes.clear();
		filterTypes.put(InspectITConstants.NOFILTER, FilterTypeMapping.getMapFilter(InspectITConstants.NOFILTER, MapSettings.getInstance().isColoredMarkers()));
	}

	/**
	 * refreshes the filter within the sub view controlled by this input controller.
	 *
	 */
	protected void refreshFilters(List<AbstractSpan> data) {
		if (MapSettings.getInstance().isResetFilters()) {
			MapSettings.getInstance().setResetFilters(false);
			resetFilters();
		}
		for (AbstractSpan marker : data) {
			Map<String, String> tags = marker.getTags();
			addFilterValue(InspectITConstants.DURATION, marker.getDuration());
			for (Entry<String, String> s : tags.entrySet()) {
				addFilterValue(s.getKey(), s.getValue());
			}
		}
		// filters are to be reset only once!

		for (MapFilter t : filterTypes.values()) {
			t.updateFilter();
		}


	}

	private void addFilterValue(String key, Object value) {
		MapFilter filter;
		if (filterTypes.containsKey(key)) {
			filter = filterTypes.get(key);
		} else {
			if ((filter = FilterTypeMapping.getMapFilter(key, MapSettings.getInstance().isColoredMarkers())) == null) {
				return;
			}
		}
		filter.addValue(value);
		filterTypes.put(key, filter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetClustering() {
		coordSys = new HashMap<Coordinate, List<InspectITMarker>>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InspectITMarker> getClusteredMarkers(ICoordinate coordinate) {
		return this.coordSys.get(calculateCoordinate(coordinate));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomLevel(int zoomlevel) {
		MapSettings.getInstance().setZoomLevel(zoomlevel);
	}


	private double calculateRadius() {
		if (MapSettings.getInstance().getZoomLevel() < 2) {
			return MapSettings.getInstance().getClusteringConstant();
		}
		return MapSettings.getInstance().getClusteringConstant() / (Math.exp((MapSettings.getInstance().getZoomLevel() * MapSettings.getInstance().getClusteringCoefficient())));

	}


	private Coordinate calculateCoordinate(ICoordinate coord) {
		// Radius has to be considered for determining the Zone coordinate
		double zoneLength = 2*calculateRadius();
		double lat = calculateZoneCenter(coord.getLat(), 90.00, zoneLength);

		double lon = calculateZoneCenter(coord.getLon(), 180.00, zoneLength);

		return new Coordinate(lat, lon);
	}


	private Coordinate calculateCoordinate(InspectITMarker marker) {
		return calculateCoordinate(marker.getCoordinate());
	}


	private double calculateZoneCenter(double value, double offset, double zoneLength) {
		value = value+offset;
		int temp = (int)(value/zoneLength);
		double result = zoneLength*temp;
		result = result-offset;
		result = result+(zoneLength/2);
		return result;

	}

	protected void clusterMarkers(List<AbstractSpan> data) {
		resetClustering();
		List<InspectITMarker> clusteredMarkers = new ArrayList<InspectITMarker>();
		for (int i = 0; i < data.size(); i++) {
			InspectITMarker marker = createMarker(data.get(i));
			if (marker == null) {
				continue;
			}
			if ((filterTypes.get(selectedTag) != null) &&
					(filterTypes.get(selectedTag).applyFilter(marker)==null)) {
				continue;
			}
			if (!MapSettings.getInstance().isClusteredMarkers()) {
				clusteredMarkers.add(marker);
				continue;
			}
			Coordinate temp = calculateCoordinate(marker);
			if (coordSys.containsKey(temp)) {
				coordSys.get(temp).add(marker);
			} else{
				List<InspectITMarker> tempList = new ArrayList<InspectITMarker>();
				tempList.add(marker);
				coordSys.put(temp, tempList);
			}

		}
		if (MapSettings.getInstance().isClusteredMarkers()) {
			for (Coordinate coord : coordSys.keySet()) {
				if (coordSys.get(coord).size() > MapSettings.getInstance().getClusteringTreshhold()) {
					clusteredMarkers.add(getCircle(coord, calculateRadius()));
				} else {
					for (InspectITMarker mark : coordSys.get(coord)) {
						clusteredMarkers.add(mark);
					}
				}
			}
		}
		this.displayedMarkers = clusteredMarkers;
	}

	private InspectITMarker createMarker(AbstractSpan span) {
		Map<String, String> tags = span.getTags();
		if (tags.containsKey(MobileTags.HTTP_REQUEST_LATITUDE) && tags.containsKey(MobileTags.HTTP_REQUEST_LONGITUDE)) {
			return new InspectITSpanMarker(span, new Coordinate(Double.parseDouble(tags.get(MobileTags.HTTP_REQUEST_LATITUDE)), Double.parseDouble(tags.get(MobileTags.HTTP_REQUEST_LONGITUDE))));
		} else if (tags.containsKey(MobileTags.HTTP_RESPONSE_LATITUDE) && tags.containsKey(MobileTags.HTTP_RESPONSE_LONGITUDE)) {
			return new InspectITSpanMarker(span, new Coordinate(Double.parseDouble(tags.get(MobileTags.HTTP_RESPONSE_LATITUDE)), Double.parseDouble(tags.get(MobileTags.HTTP_RESPONSE_LONGITUDE))));
		} else {
			return null;
		}
	}

	@Override
	public Object getMapInput() {
		return displayedMarkers;
	}

	@Override
	public Map<String, Boolean> getSettings() {
		return MapSettings.getInstance().getSettings();
	}

	@Override
	public void settingChanged(String name, Object selected) {
		MapSettings.getInstance().setSetting(name, selected);
		applySettings();
	}

	private void applySettings() {
		MapFilter temp = filterTypes.get(selectedTag);
		temp.setColored(MapSettings.getInstance().isColoredMarkers());
		filterTypes.put(selectedTag, temp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, MapFilter> getMapFilter() {
		return filterTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void keySelectionChanged(String key) {
		selectedTag = key;
		applySettings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void valueSelectionChanged(Object value) {
		MapFilter temp = filterTypes.get(selectedTag);
		temp.changeSelection(value);
		filterTypes.put(selectedTag, temp);
	}

}
