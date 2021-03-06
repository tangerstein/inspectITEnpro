package rocks.inspectit.ui.rcp.editor.map.filter;

import java.awt.Color;

import javax.swing.JComponent;

import rocks.inspectit.ui.rcp.editor.map.MapSubView.FilterValueObject;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITMarker;
import rocks.inspectit.ui.rcp.editor.map.model.NumericFilterPanel;
import rocks.inspectit.ui.rcp.editor.map.model.NumericRange;

/**
 * The filter element for numeric values.
 *
 * @author Christopher Völker
 *
 * @param <T>
 *            The generic type of this numeric element.
 */
public class NumericMapFilter<T> extends AbstractMapFilter<T> {

	/**
	 * The total range which should be allowed by the sliders.
	 */
	NumericRange totalRange;
	/**
	 * The actual range (lower and upper bound) which represents the current value of the sliders.
	 */
	NumericRange filteredRange;

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
	public NumericMapFilter(String tagKey, Boolean colored) {
		super(tagKey, colored);
		totalRange = new NumericRange();
		filteredRange = new NumericRange();
		initColors();
	}

	@Override
	public void initColors() {
		colorList.clear();
		if (isColored) {
			colorList.add(new Color(0, 0, 128));
			colorList.add(new Color(0, 128, 0));
			colorList.add(new Color(0, 255, 255));
			colorList.add(new Color(128, 0, 0));
			colorList.add(new Color(128, 0, 128));
		}  else {
			colorList.add(new Color(0, 0, 0, 80));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValue(Object value) {
		try {
			Double temp = (Double) value;
			totalRange.updateBounds(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent getPanel(FilterValueObject filterValueObject) {
		NumericFilterPanel temp = new NumericFilterPanel(filterValueObject, totalRange, filteredRange);
		return temp;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateFilter() {
		filterMap.clear();
		double temp = (totalRange.getUpperBound() - totalRange.getLowerBound()) / colorList.size();
		for (int i = 0; i < (colorList.size() - 1); i++) {
			Double section = totalRange.getLowerBound() + (temp * i);
			putFilterConstraint((T) section, new MarkerFilterElement(colorList.get(i)));
		}
		Double section = totalRange.getUpperBound();
		putFilterConstraint((T) section, new MarkerFilterElement(colorList.get(colorList.size() - 1)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InspectITMarker<?> applyFilter(InspectITMarker<?> marker) {
		Double temp = Double.parseDouble((String) marker.getTags().get(tagKey));
		if (!filteredRange.withinRange(temp)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		MarkerFilterElement element = getFilter((T) temp);
		marker.setStyle(element.style());
		marker.setVisible(element.isVisible());
		return marker;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeSelection(Object selection) {
		filteredRange = (NumericRange) selection;
	}

}
