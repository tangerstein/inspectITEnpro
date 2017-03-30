package rocks.inspectit.ui.rcp.editor.map.filter;

import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import rocks.inspectit.ui.rcp.editor.map.model.InspectITMarker;
import rocks.inspectit.ui.rcp.editor.map.model.NumericFilterPanel;
import rocks.inspectit.ui.rcp.editor.map.model.NumericRange;

public class NumericMapFilter<T> extends AbstractMapFilter<T> {

	NumericRange totalRange;
	NumericRange filteredRange;

	public NumericMapFilter(String tagKey) {
		super(tagKey);
		totalRange = new NumericRange();
		filteredRange = new NumericRange();
		initColors();
	}

	private void initColors() {
		colorList.add(new Color(0, 0, 128));
		colorList.add(new Color(0, 128, 0));
		colorList.add(new Color(0, 255, 255));
		colorList.add(new Color(128, 0, 0));
		colorList.add(new Color(128, 0, 128));

	}

	@Override
	public void addValue(Object value) {
		Double temp = Double.parseDouble((String) value);
		totalRange.updateBounds(temp);
	}

	@Override
	public JPanel getPanel(FilterEventListener listener) {
		NumericFilterPanel temp = new NumericFilterPanel(totalRange);
		temp.setFilterEventListener(listener);
		return temp;
	}

	@Override
	public void finalizeFilter() {
		double temp = (totalRange.getUpperBound()-totalRange.getLowerBound())/5;
		List<Color> colorList = getAvailableColor();
		for (int i =0;i<5;i++) {
			Double section = totalRange.getLowerBound()+(temp*i);
			addFilterConstraint((T) section, new MarkerFilterElement(colorList.get(i)));
		}
	}

	@Override
	public InspectITMarker applyFilter(InspectITMarker marker) {
		Double temp = Double.parseDouble((String) marker.getTags().get(tagKey));
		if (!filteredRange.withinRange(temp)) {
			return null;
		}
		MarkerFilterElement element = getFilter((T)temp);
		marker.setStyle(element.style());
		marker.setVisible(element.isVisible());
		return marker;
	}

	@Override
	public void ChangeSelection(Object selection) {
		filteredRange = (NumericRange) selection;
	}

}
