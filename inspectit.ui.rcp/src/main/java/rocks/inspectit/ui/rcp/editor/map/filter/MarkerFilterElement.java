package rocks.inspectit.ui.rcp.editor.map.filter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import org.openstreetmap.gui.jmapviewer.Style;

/**
 * The filter which contains all visual values which can be set in a map marker.
 *
 * @author Christopher Völker
 *
 */
public class MarkerFilterElement {

	/**
	 * The style element for this filter element.
	 */
	Style style = new Style();

	/**
	 * The visibility of this filter element.
	 */
	Boolean isVisible;

	/**
	 * The constructor which sets the passed visiblity and initializes the other style elements with
	 * standard values.
	 *
	 * @param visible
	 *            The visibility of this element.
	 */
	public MarkerFilterElement(Boolean visible) {
		initialize();
		this.setVisible(visible);
	}

	/**
	 * The constructor which sets the passed background color and initializes the other style
	 * elements with standard values.
	 *
	 * @param backColor
	 *            The background color to be set for this element.
	 */
	public MarkerFilterElement(Color backColor) {
		initialize();
		this.setBackColor(backColor);
	}

	/**
	 * The constructor which initializes the style elements with standard values.
	 *
	 */
	public MarkerFilterElement() {
		initialize();
	}

	/**
	 * The function which initializes this element with default values.
	 */
	private void initialize() {
		this.setBackColor(Color.BLACK);
		this.setColor(Color.BLACK);
		this.setStroke(new BasicStroke());
		this.setFont(new Font(Font.SERIF, Font.BOLD, 12));
		this.isVisible = true;
	}

	/**
	 * The function which returns the current {@link Style} element.
	 *
	 * @return The current {@link Style} element.
	 */
	public Style style() {
		return style;
	}

	/**
	 * The function which returns the current visibility element.
	 *
	 * @return The visibility of this element.
	 */
	public Boolean isVisible() {
		return isVisible;
	}

	/**
	 * The function which sets the visibility of this element to the passed boolean flag.
	 *
	 * @param visible
	 *            The visibility for this element.
	 */
	public void setVisible(Boolean visible) {
		this.isVisible = visible;
	}

	/**
	 * The function which sets the background {@link Color} for this element.
	 *
	 * @param backColor
	 *            The background {@link Color} to be set.
	 */
	public void setBackColor(Color backColor) {
		this.style.setBackColor(backColor);
	}

	/**
	 * The function which sets the {@link Color} for this element.
	 *
	 * @param color
	 *            The {@link Color} to be set for this element.
	 */
	public void setColor(Color color) {
		this.style.setColor(color);
	}

	/**
	 * The function which sets the {@link Stroke} for this element.
	 *
	 * @param stroke
	 *            The {@link Stroke} to be set.
	 */
	public void setStroke(Stroke stroke) {
		this.style.setStroke(stroke);
	}

	/**
	 * The function which sets the {@link Font} for this element.
	 *
	 * @param font
	 *            The {@link Font} to be set.
	 */
	public void setFont(Font font) {
		this.style.setFont(font);
	}
}
