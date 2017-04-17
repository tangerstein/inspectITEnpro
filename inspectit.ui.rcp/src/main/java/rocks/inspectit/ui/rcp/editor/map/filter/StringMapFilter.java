package rocks.inspectit.ui.rcp.editor.map.filter;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import rocks.inspectit.ui.rcp.InspectITConstants;
import rocks.inspectit.ui.rcp.editor.map.MapSubView.FilterValueObject;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITMarker;
import rocks.inspectit.ui.rcp.editor.map.model.StringFilterPanel;

/**
 * The map filter which takes care about the available filter keys and the coloring for the
 * corresponding values.
 *
 * @param <T>
 *            The generic parameter of which type the keys are.
 *
 * @author Christopher Völker, Simon Lehmann
 *
 */
public class StringMapFilter<T> extends AbstractMapFilter<T> {

	/**
	 * The set of values belonging to the set tag key of this filter.
	 *
	 */
	Set<String> values;

	/**
	 * The set of values which are to be hidden from the map.
	 *
	 */
	Set<T> toHide;

	/**
	 * The constructor for this filter which needs a tag key as well as the initial setting for
	 * coloring.
	 *
	 * @param tagKey
	 *            The tag key this filter belongs to.
	 * @param colored
	 *            The initial boolean value for coloring.
	 *
	 */
	public StringMapFilter(String tagKey, boolean colored) {
		super(tagKey, colored);
		values = new HashSet<>();
		toHide = new HashSet<>();
		initColors();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initColors() {
		colorList.clear();
		if (isColored) {
			colorList.add(new Color(192, 0, 0));
			colorList.add(new Color(0, 112, 192));
			colorList.add(new Color(0, 176, 80));
			colorList.add(new Color(123, 123, 123));
			colorList.add(new Color(112, 48, 160));
			colorList.add(new Color(0, 32, 96));
			colorList.add(new Color(100, 255, 192));
			colorList.add(new Color(0, 176, 240));
			colorList.add(new Color(3, 135, 83));
			colorList.add(new Color(255, 0, 199));
		} else {
			colorList.add(new Color(0, 0, 0, 80));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValue(Object value) {
		values.add((String) value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent getPanel(FilterValueObject filterValueObject) {
		StringFilterPanel<T> temp = new StringFilterPanel<T>(filterValueObject, this.getKeys(), filterMap);
		return temp;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateFilter() {
		filterMap.clear();
		if (this.values.size() <= colorList.size()) {
			int index = 0;
			for (String value : this.values) {
				MarkerFilterElement temp = new MarkerFilterElement(colorList.get(index));
				if (toHide.contains(value)) {
					temp.setVisible(false);
				}
				putFilterConstraint((T) value, temp);
				index++;
			}
		} else {
			System.err.println("Did not provide enough colors!");
			for (String value : this.values) {
				MarkerFilterElement temp = new MarkerFilterElement(colorList.get(0));
				if (toHide.contains(value)) {
					temp.setVisible(false);
				}
				putFilterConstraint((T) value, temp);

			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InspectITMarker<?> applyFilter(InspectITMarker<?> marker) {
		if (tagKey.equals(InspectITConstants.NOFILTER)) {
			return adaptMarker(marker, new MarkerFilterElement());
		}
		@SuppressWarnings("unchecked")
		MarkerFilterElement elem = getFilter((T) marker.getTags().get(tagKey));
		if (elem.isVisible) {
			return adaptMarker(marker, elem);
		}
		return null;
	}

	/**
	 * This function takes a {@link InspectITMarker} as well as a corresponding
	 * {@link MarkerFilterElement} which is applied on the marker.
	 *
	 * @param marker
	 *            The marker the filter is to be applied on.
	 * @param element
	 *            The {@link MarkerFilterElement} which is to be applied on the marker.
	 * @return The filter on which the filter was applied on.
	 *
	 */
	private InspectITMarker<?> adaptMarker(InspectITMarker<?> marker, MarkerFilterElement element) {
		marker.setStyle(element.style());
		marker.setVisible(element.isVisible());
		return marker;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void changeSelection(Object selection) {
		if (toHide.contains(selection)) {
			toHide.remove(selection);
		} else {
			toHide.add((T) selection);
		}
	}

}
