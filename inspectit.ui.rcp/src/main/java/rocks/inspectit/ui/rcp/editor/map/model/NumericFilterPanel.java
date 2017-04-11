package rocks.inspectit.ui.rcp.editor.map.model;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rocks.inspectit.ui.rcp.editor.map.MapSubView.FilterValueObject;


public class NumericFilterPanel extends JPanel {

	private JLabel lowerRangeValue;
	private JLabel upperRangeValue;
	private JSlider lowerBound;
	private JSlider upperBound;
	FilterValueObject filterValueObject;

	private NumericFilterPanel self;

	public NumericFilterPanel(FilterValueObject filterValueObject, NumericRange totalRange, NumericRange filteredRange) {
		this.filterValueObject = filterValueObject;
		self = this;
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		lowerBound = initSlider(new LowerSliderStateChangeListener(), totalRange.getLowerBound(), filteredRange.getUpperBound(), filteredRange.getLowerBound());
		upperBound = initSlider(new UpperSliderStateChangeListener(), filteredRange.getLowerBound(), totalRange.getUpperBound(), filteredRange.getUpperBound());

		JPanel rangeValuePanel = createRangeValuePanel();
		this.add(lowerBound);
		this.add(upperBound);
		this.add(rangeValuePanel);
	}


	private JSlider initSlider(ChangeListener listener, double min, double max, double value) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL);
		adaptSlider(slider, min, max, value);
		slider.addChangeListener(listener);
		return slider;
	}

	private void adaptSlider(JSlider slider, double min, double max, double value) {
		slider.setMinimum((int) min);
		slider.setMaximum((int) max);
		slider.setValue((int) value);
		slider.setMajorTickSpacing(getMajorTickSpacing((int) min, (int) max));
		slider.setMinorTickSpacing(getMinorTickSpacing((int) min, (int) max));
	}

	private JPanel createRangeValuePanel() {
		JPanel panel = new JPanel(new FlowLayout());
		JLabel range = new JLabel("Range:");
		JLabel space = new JLabel("-");
		lowerRangeValue = new JLabel(lowerBound.getValue() + "");
		upperRangeValue = new JLabel(upperBound.getValue() + "");
		panel.add(range);
		panel.add(lowerRangeValue);
		panel.add(space);
		panel.add(upperRangeValue);
		return panel;
	}

	private class LowerSliderStateChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			lowerRangeValue.setText(lowerBound.getValue() + "");
			// Only trigger reloading of the map after selection is done
			if (!lowerBound.getValueIsAdjusting()) {
				adaptSlider(upperBound, lowerBound.getValue(), upperBound.getMaximum(), upperBound.getValue());
				upperBound.revalidate();
				filterValueObject.selectionChanged(new NumericRange(lowerBound.getValue(), upperBound.getValue()));
			}
		}

	}

	private class UpperSliderStateChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			upperRangeValue.setText(upperBound.getValue() + "");
			// Only trigger reloading of the map after selection is done
			if (!upperBound.getValueIsAdjusting()) {
				adaptSlider(lowerBound, lowerBound.getMinimum(), upperBound.getValue(), lowerBound.getValue());
				lowerBound.revalidate();
				filterValueObject.selectionChanged(new NumericRange(lowerBound.getValue(), upperBound.getValue()));
			}
		}

	}

	private int getMajorTickSpacing(int lowerBound, int upperBound) {
		return (upperBound - lowerBound) / 3;
	}

	private int getMinorTickSpacing(int lowerBound, int upperBound) {
		return (upperBound - lowerBound) / 9;
	}

	private String printLowerSlider() {
		return "Lower: " + lowerBound.getMinimum() + ", " + lowerBound.getValue() + ", " + lowerBound.getMaximum();
	}

	private String printUpperSlider() {
		return "Upper: " + upperBound.getMinimum() + ", " + upperBound.getValue() + ", " + upperBound.getMaximum();
	}
}
