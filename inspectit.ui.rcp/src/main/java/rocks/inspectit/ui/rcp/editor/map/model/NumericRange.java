package rocks.inspectit.ui.rcp.editor.map.model;

/**
 * The object which holds and manages the lower and upper bound of a numeric range.
 *
 * @author Christopher Völker
 *
 */
public class NumericRange {

	/**
	 * The actual lower bound of this range.
	 */
	private double lowerBound;
	/**
	 * The actual upper bound of this range.
	 */
	private double upperBound;

	/**
	 * The constructor which initializes this object with the given lower and upper bound.
	 *
	 * @param lowerBound
	 *            The lower bound to initialize this object with.
	 * @param upperBound
	 *            The upper bound to initialize this object with.
	 */
	public NumericRange(double lowerBound, double upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 * The constructor which initializes this object with minimum and maximum values of the Double
	 * object.
	 *
	 */
	public NumericRange() {
		this.lowerBound = Double.MIN_VALUE;
		this.upperBound = Double.MAX_VALUE;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * The function which updates the range with the given double value. If the value is smaller
	 * than the lower bound, the lower bound is set to the value. If the value is bigger than the
	 * upper bound, the upper bound is set to the value.
	 *
	 * @param duration
	 *            The value the range is updates with.
	 */
	public void updateBounds(Double duration) {
		if ((getLowerBound() == Double.MIN_VALUE) || (getLowerBound() > duration)) {
			setLowerBound(duration);
		}
		if ((getUpperBound() == Double.MAX_VALUE) || (getUpperBound() < duration)) {
			setUpperBound(duration);
		}
	}

	/**
	 * The function which checks if the given value is within the range of this object.
	 *
	 * @param value
	 *            The value to be checked.
	 * @return True if the value is within the range, false otherwise.
	 */
	public boolean withinRange(Double value) {
		if (lowerBound > value) {
			return false;
		}
		if (upperBound < value) {
			return false;
		}
		return true;
	}

}
