package rocks.inspectit.ui.rcp.editor.map.model;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rocks.inspectit.ui.rcp.editor.map.MapSubView.FilterValueObject;

/**
 * The map filter which takes care about the available numeric filter keys and the coloring for the
 * corresponding values.
 *
 *
 * @author Christopher Völker, Simon Lehmann
 *
 */
public class NumericFilterPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The label showing the current lower range value.
	 */
	private JLabel lowerRangeValue;
	/**
	 * The label showing the current upper range value.
	 */
	private JLabel upperRangeValue;
	/**
	 * The slider enabling the user to set the lower bound.
	 */
	private JSlider lowerBound;
	/**
	 * The slider enabling the user to set the upper bound.
	 */
	private JSlider upperBound;
	/**
	 * The {@link FilterValueObject} which is the callback for any bounds that are changed within
	 * this filter.
	 */
	FilterValueObject filterValueObject;

	/**
	 * The constructor for this filter class.
	 *
	 * @param filterValueObject
	 *            The {@link FilterValueObject} which is the callback for any bounds that are
	 *            changed within this filter.
	 * @param totalRange
	 *            The total range which should be allowed by the sliders.
	 * @param filteredRange
	 *            The actual range (lower and upper bound) which represents the current value of the
	 *            sliders.
	 */
	public NumericFilterPanel(FilterValueObject filterValueObject, NumericRange totalRange, NumericRange filteredRange) {
		this.filterValueObject = filterValueObject;
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		lowerBound = initSlider(new LowerSliderStateChangeListener(), totalRange.getLowerBound(), filteredRange.getUpperBound(), filteredRange.getLowerBound());
		upperBound = initSlider(new UpperSliderStateChangeListener(), filteredRange.getLowerBound(), totalRange.getUpperBound(), filteredRange.getUpperBound());

		JPanel rangeValuePanel = createRangeValuePanel();
		this.add(lowerBound);
		this.add(upperBound);
		this.add(rangeValuePanel);
	}

	/**
	 * A function which initializes a slider with the given range, currently set value and the
	 * corresponding change listener.
	 *
	 * @param listener
	 *            The change listener for the slider.
	 * @param min
	 *            The minimum value for the slider.
	 * @param max
	 *            The maximum value for the slider
	 * @param value
	 *            The current value to be set for the slider.
	 * @return The created slider.
	 */
	private JSlider initSlider(ChangeListener listener, double min, double max, double value) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL);
		adaptSlider(slider, min, max, value);
		slider.addChangeListener(listener);
		return slider;
	}

	/**
	 * A function which changes the properties of a given slider to the given range, currently set
	 * value and the corresponding change listener.
	 *
	 * @param slider
	 *            The slider to be adapted.
	 * @param min
	 *            The minimum value for the slider.
	 * @param max
	 *            The maximum value for the slider
	 * @param value
	 *            The current value to be set for the slider.
	 */
	private void adaptSlider(JSlider slider, double min, double max, double value) {
		slider.setMinimum((int) min);
		slider.setMaximum((int) max);
		slider.setValue((int) value);
		slider.setMajorTickSpacing(getMajorTickSpacing((int) min, (int) max));
		slider.setMinorTickSpacing(getMinorTickSpacing((int) min, (int) max));
	}

	/**
	 * The function which creates the panel showing the current selected lower and upper bound.
	 *
	 * @return The panel which shows the current bounds.
	 */
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

	/**
	 * The implementation of the {@link ChangeListener} class which realizes the listener for the
	 * lower bound.
	 *
	 * @author Simon Lehmann
	 *
	 */
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

	/**
	 * The implementation of the {@link ChangeListener} class which realizes the listener for the
	 * upper bound.
	 *
	 * @author Simon Lehmann
	 *
	 */
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

	/**
	 * The function which calculates the major tick spacing depending on the given lower and upper
	 * bound.
	 *
	 * @param lowerBound
	 *            The lower bound to calculate the tick spacing from.
	 * @param upperBound
	 *            The upper bound to calculate the tick spacing from.
	 * @return The calculated tick spacing.
	 */
	private int getMajorTickSpacing(int lowerBound, int upperBound) {
		return (upperBound - lowerBound) / 3;
	}

	/**
	 * The function which calculates the minor tick spacing depending on the given lower and upper
	 * bound.
	 *
	 * @param lowerBound
	 *            The lower bound to calculate the tick spacing from.
	 * @param upperBound
	 *            The upper bound to calculate the tick spacing from.
	 * @return The calculated tick spacing.
	 */
	private int getMinorTickSpacing(int lowerBound, int upperBound) {
		return (upperBound - lowerBound) / 9;
	}
}
