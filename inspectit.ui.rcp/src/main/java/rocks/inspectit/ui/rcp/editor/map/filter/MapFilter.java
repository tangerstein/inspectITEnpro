package rocks.inspectit.ui.rcp.editor.map.filter;

import java.awt.Color;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import rocks.inspectit.ui.rcp.editor.map.MapSubView.FilterValueObject;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITMarker;

/**
 * Interface used by all Markers used for the Map.
 *
 * @param <T>
 *            A generic parameter serving as key for the mapping
 *
 * @author Christopher Völker, Simon Lehmann
 *
 */
public interface MapFilter<T> {

	/**
	 * A Function which assigns a specific {@MarkerFilterElement} to a specific value (called key,
	 * since it represents the key within the map).
	 *
	 * @param key
	 *            The value which is assigned a {@MarkerFilterElement}.
	 * @param element
	 *            The {@MarkerFilterElement} which is assigned to the given value (key).
	 */
	void putFilterConstraint(T key, MarkerFilterElement element);

	/**
	 * A Function which retrieves a the {@MarkerFilterElement} mapped to the given value (called
	 * key, since it represents the key within the map).
	 *
	 * @param key
	 *            The value which is assigned a {@MarkerFilterElement}.
	 * @return The {@MarkerFilterElement} mapped to the key or a standard {@MarkerFilterElement} if
	 *         there is no corresponding mapping.
	 */
	MarkerFilterElement getFilter(T key);

	/**
	 * A Function which retrieves the available keys within the filter and returns them as Set.
	 *
	 * @return The Set of keys available within this filter.
	 */
	Set<T> getKeys();

	/**
	 * A Function which retrieves the available Colors within the filter and returns them as Set.
	 *
	 * @return The Set of Colors available within this filter.
	 */
	List<Color> getAvailableColor();

	/**
	 * A Function which sets the flag for colored or transparent (colored = false) markers.
	 *
	 * @param colored
	 *            The flag for colored (or transparent) markers.
	 */
	void setColored(boolean colored);

	/**
	 * A Function which creates and returns the filter Panel with the given {@FilterEventListener}.
	 *
	 * @param filterValueObject
	 *            The {FilterValueObject} to be used within the filter Panel.
	 * @return The created filter Panel.
	 */
	JComponent getPanel(FilterValueObject filterValueObject);

	/**
	 * A Function which finalizes the filter assigning colors or transparencies to all stored values
	 * within this filter.
	 *
	 */
	void updateFilter();

	/**
	 * A Function which returns the value mapped to the given key.
	 *
	 * @param key
	 *            The key which is used to identify the value to be returned.
	 * @return The value mapped to the given key.
	 */
	T getValue(T key);

	/**
	 * A function which takes a value and adds it to the filter.
	 *
	 * @param value
	 *            The value to be added to the filter.
	 */
	void addValue(T value);

	/**
	 * A function which takes a object (either {@NumericRange} or String) indicating that the
	 * selection and therefore the filter was changed.
	 *
	 * @param selection
	 *            The object which contains the new information.
	 */
	void changeSelection(Object selection);

	/**
	 * A function initializes the list of colors or alternatively a specific color with a specific
	 * transparency.
	 *
	 */
	void initColors();

	/**
	 * A Function which takes a {@InspectItMarker} and applies the filter to it. In order to apply
	 * the filter it maps the currently selected key within the filter to a value within the marker
	 * and applies the corresponding marker settings defined by the {@MarkerFilterElement}.
	 *
	 * @param marker
	 *            The marker to which the filter is to be applied.
	 * @return The marker containing the information defined by the {@MarkerFilterElement}.
	 */
	InspectITMarker<?> applyFilter(InspectITMarker<?> marker);

}
