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

/**
 * The abstract implementation of the MapInputController interface for the map sub view.
 *
 * @author Christopher Völker
 *
 */
public abstract class AbstractMapInputController implements MapInputController {

	/**
	 * A map which holds the markers clustered to specific coordinate spaces.
	 */
	Map<Coordinate, List<InspectITMarker<?>>> coordSys = new HashMap<Coordinate, List<InspectITMarker<?>>>();

	/**
	 * A list of all available data markers.
	 */
	List<InspectITMarker<?>> displayedMarkers = new ArrayList<InspectITMarker<?>>();

	/**
	 * The current mapFilter.
	 */
	Map<String, MapFilter<?>> filterTypes;

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
	@SuppressWarnings({ "rawtypes" })
	private InspectITMarker<?> getCircle(Coordinate coord, double rad) {
		return new InspectITClusterMarker(coord, rad * MapSettings.getInstance().getClusteringCoefficient());
	}

	/**
	 * The function which resets all filters.
	 */
	private void resetFilters() {
		filterTypes.clear();
		filterTypes.put(InspectITConstants.NOFILTER, FilterTypeMapping.getMapFilter(InspectITConstants.NOFILTER, MapSettings.getInstance().isColoredMarkers()));
	}

	/**
	 * refreshes the filter within the sub view controlled by this input controller.
	 *
	 * @param data
	 *            The data to refresh the filter from.
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

		for (MapFilter<?> t : filterTypes.values()) {
			t.updateFilter();
		}


	}

	/**
	 * The function which adds a given value to the corresponding given key within the filter map.
	 * It is only added if it is not already in it otherwise the existing value is updated..
	 *
	 * @param key
	 *            The key to add the value to.
	 * @param value
	 *            The value to be added to the given key.
	 */
	@SuppressWarnings("unchecked")
	private void addFilterValue(String key, Object value) {
		@SuppressWarnings("rawtypes")
		MapFilter filter;
		if (filterTypes.containsKey(key)) {
			filter = filterTypes.get(key);
		} else {
			filter = FilterTypeMapping.getMapFilter(key, MapSettings.getInstance().isColoredMarkers());
			if (filter == null) {
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
		coordSys = new HashMap<Coordinate, List<InspectITMarker<?>>>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InspectITMarker<?>> getClusteredMarkers(ICoordinate coordinate) {
		return this.coordSys.get(calculateZoneCoordinate(coordinate));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomLevel(int zoomlevel) {
		MapSettings.getInstance().setZoomLevel(zoomlevel);
	}

	/**
	 * This function calculates the radius for clustered markers depending on the current zoom
	 * level. The setting for the clustering constants and the clustering coefficient influences the
	 * calculation as well.
	 *
	 * @return The calculated radius.
	 */
	private double calculateRadius() {
		if (MapSettings.getInstance().getZoomLevel() < 2) {
			return MapSettings.getInstance().getClusteringConstant();
		}
		return MapSettings.getInstance().getClusteringConstant() / (Math.exp((MapSettings.getInstance().getZoomLevel() * MapSettings.getInstance().getClusteringCoefficient())));

	}

	/**
	 * This function calculates the coordinates for the clustering zone a given coordinate belongs
	 * to.
	 *
	 * @param coord
	 *            The coordinate to map to the clustering zone it belongs to.
	 * @return The coordinate of the clustering zone.
	 */
	private Coordinate calculateZoneCoordinate(ICoordinate coord) {
		// Radius has to be considered for determining the Zone coordinate
		double zoneLength = 2 * calculateRadius();
		double lat = calculateCenter(coord.getLat(), 90.00, zoneLength);

		double lon = calculateCenter(coord.getLon(), 180.00, zoneLength);

		return new Coordinate(lat, lon);
	}

	/**
	 * This function calculates the coordinates for the clustering zone a given marker belongs to.
	 *
	 * @param marker
	 *            The marker to map to the clustering zone it belongs to.
	 * @return The coordinate of the clustering zone the marker belongs to.
	 */
	private Coordinate calculateCoordinate(InspectITMarker<?> marker) {
		return calculateZoneCoordinate(marker.getCoordinate());
	}

	/**
	 * This function calculates the center of a zone for one coordinate value using the given zone
	 * length as well as the offset.
	 *
	 * @param value
	 *            The value to calculate the center from.
	 * @param offset
	 *            The offset which is needed for proper calculation and depends on whether latitude
	 *            or longitude values are to be handled.
	 * @param zoneLength
	 *            The zone length of the clustering zone.
	 * @return The value of the center.
	 */
	private double calculateCenter(double value, double offset, double zoneLength) {
		value = value + offset;
		int temp = (int) (value / zoneLength);
		double result = zoneLength * temp;
		result = result - offset;
		result = result + (zoneLength / 2);
		return result;

	}

	/**
	 * This functions creates and clusters the markers on the map if the corresponding flag is set.
	 * Clustering markers depends on the setting of the clustering threshold.
	 *
	 * @param data
	 *            The List of spans to create and cluster the markers from.
	 */
	protected void clusterMarkers(List<AbstractSpan> data) {
		resetClustering();
		List<InspectITMarker<?>> clusteredMarkers = new ArrayList<InspectITMarker<?>>();
		for (int i = 0; i < data.size(); i++) {
			InspectITMarker<?> marker = createMarker(data.get(i));
			if (marker == null) {
				continue;
			}
			if ((filterTypes.get(selectedTag) != null) && (filterTypes.get(selectedTag).applyFilter(marker) == null)) {
				continue;
			}
			if (!MapSettings.getInstance().isClusteredMarkers()) {
				clusteredMarkers.add(marker);
				continue;
			}
			Coordinate temp = calculateCoordinate(marker);
			if (coordSys.containsKey(temp)) {
				coordSys.get(temp).add(marker);
			} else {
				List<InspectITMarker<?>> tempList = new ArrayList<InspectITMarker<?>>();
				tempList.add(marker);
				coordSys.put(temp, tempList);
			}

		}
		if (MapSettings.getInstance().isClusteredMarkers()) {
			for (Coordinate coord : coordSys.keySet()) {
				if (coordSys.get(coord).size() > MapSettings.getInstance().getClusteringTreshhold()) {
					clusteredMarkers.add(getCircle(coord, calculateRadius()));
				} else {
					for (InspectITMarker<?> mark : coordSys.get(coord)) {
						clusteredMarkers.add(mark);
					}
				}
			}
		}
		this.displayedMarkers = clusteredMarkers;
	}

	/**
	 * This function creates a marker from a given span if (and only if) it contains a latitude and
	 * longitude value.
	 *
	 * @param span
	 *            The span to create the marker from.
	 * @return The marker created from the span, null if no latitude and longitude value are
	 *         provided by the marker.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private InspectITMarker<?> createMarker(AbstractSpan span) {
		Map<String, String> tags = span.getTags();
		if (tags.containsKey(MobileTags.HTTP_REQUEST_LATITUDE) && tags.containsKey(MobileTags.HTTP_REQUEST_LONGITUDE)) {
			return new InspectITSpanMarker(span, new Coordinate(Double.parseDouble(tags.get(MobileTags.HTTP_REQUEST_LATITUDE)), Double.parseDouble(tags.get(MobileTags.HTTP_REQUEST_LONGITUDE))));
		} else if (tags.containsKey(MobileTags.HTTP_RESPONSE_LATITUDE) && tags.containsKey(MobileTags.HTTP_RESPONSE_LONGITUDE)) {
			return new InspectITSpanMarker(span, new Coordinate(Double.parseDouble(tags.get(MobileTags.HTTP_RESPONSE_LATITUDE)), Double.parseDouble(tags.get(MobileTags.HTTP_RESPONSE_LONGITUDE))));
		} else {
			return null;
		}
	}

	/**
	 *
	 * {@inheritDoc}
	 */

	@Override
	public Object getMapInput() {
		return displayedMarkers;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Boolean> getSettings() {
		return MapSettings.getInstance().getSettings();
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void settingChanged(String name, Object value) {
		MapSettings.getInstance().setSetting(name, value);
		applySettings();
	}

	/**
	 * This function applies the coloring settings to the current selected tag.
	 */
	private void applySettings() {
		MapFilter<?> temp = filterTypes.get(selectedTag);
		temp.setColored(MapSettings.getInstance().isColoredMarkers());
		filterTypes.put(selectedTag, temp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, MapFilter<?>> getMapFilter() {
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
		MapFilter<?> temp = filterTypes.get(selectedTag);
		temp.changeSelection(value);
		filterTypes.put(selectedTag, temp);
	}
}
