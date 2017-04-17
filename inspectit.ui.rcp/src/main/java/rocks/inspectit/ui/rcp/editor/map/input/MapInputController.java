package rocks.inspectit.ui.rcp.editor.map.input;

import java.util.List;
import java.util.Map;

import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import rocks.inspectit.ui.rcp.editor.inputdefinition.InputDefinition;
import rocks.inspectit.ui.rcp.editor.map.filter.MapFilter;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITMarker;
import rocks.inspectit.ui.rcp.editor.root.SubViewClassificationController;

/**
 * The interface for all map input controller.
 *
 * @author Christopher Völker, Simon Lehmann
 *
 */
public interface MapInputController extends SubViewClassificationController {

	/**
	 * Sets the input definition of this controller.
	 *
	 * @param inputDefinition
	 *            The input definition.
	 */
	void setInputDefinition(InputDefinition inputDefinition);

	/**
	 * Function which sets the data to be displayed on the map.
	 *
	 * @param data
	 *            The data to be displayed.
	 */
	void setData(List<? extends Object> data);

	/**
	 * Function which sets the data to be displayed on the map which equals the selection within the
	 * tracing view.
	 *
	 * @param data
	 *            The selected data to be displayed.
	 */
	void setDataSelection(List<? extends Object> data);

	/**
	 * Gets the (clustered) markers at the given coordinate.
	 *
	 * @param coordinate
	 *            The coordinate for which the markers are requested.
	 * @return The list of (clustered) markers at this given coordinate.
	 */
	List<InspectITMarker<?>> getClusteredMarkers(ICoordinate coordinate);

	/**
	 * Resets the clustering of the markers by removing all entries from the clustering map.
	 *
	 */
	void resetClustering();

	/**
	 * Sets the new zoom level and recalculates the clustering of markers.
	 *
	 * @param zoomLevel
	 *            The new zoom level.
	 */
	void setZoomLevel(int zoomLevel);

	/**
	 * The function which returns the current map input which is to be displayed.
	 *
	 * @return The data to be displayed.
	 */
	Object getMapInput();

	/**
	 * The function which returns the current boolean settings for the map.
	 *
	 * @return The current boolean settings of the map.
	 */
	Map<String, Boolean> getSettings();

	/**
	 * The function which changes a given setting to the given value.
	 *
	 * @param name
	 *            The setting to be changed.
	 * @param value
	 *            The value to be set for the given setting.
	 */
	void settingChanged(String name, Object value);

	/**
	 *
	 */
	void doRefresh();

	/**
	 * The function returns the current map of all filter keys and their corresponding values.
	 *
	 * @return The map of current filters.
	 */
	Map<String, MapFilter<?>> getMapFilter();

	/**
	 * The function which is to be invoked upon the change of the filter key.
	 *
	 * @param key
	 *            The new key which was selected.
	 */
	void keySelectionChanged(String key);


	/**
	 * The function which is to be invoked upon the change of the filter value.
	 *
	 * @param value
	 *            The new value which was changed.
	 */
	void valueSelectionChanged(Object value);

}