package rocks.inspectit.ui.rcp.editor.map.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The current map settings for the map.
 *
 * @author Christopher Völker
 *
 */
public final class MapSettings {

	/**
	 * This is saved as Singelton.
	 */
	private static MapSettings instance;

	/**
	 * Creates and returns the Singelton.
	 *
	 * @return the Singelton
	 */
	public static MapSettings getInstance() {
		if (instance == null) {
			instance = new MapSettings();
		}
		return instance;
	}

	/**
	 * The current zoom level.
	 */
	int zoomLevel;

	/**
	 * The current minimum amount of markers in a coordinate space in order to cluster.
	 */
	int clusteringTreshhold;

	/**
	 * A flag which determines if the filters have to be reset upon refresh.
	 */
	boolean resetFilters;

	/**
	 * The current cluster coefficient.
	 */
	double clusteringCoefficient;

	/**
	 * The current cluster constant.
	 */
	double clusteringConstant;

	/**
	 * Flag for colored markers (if false: transparent markers).
	 */
	boolean coloredMarkers;

	/**
	 * Flag for clustered markers (if false: no clustering).
	 */
	boolean clusteredMarkers;

	/**
	 * The enum which holds a mapping to string representations for all settings.
	 */
	public enum Settings {
		/**
		 * The tag for clustered markers.
		 */
		clusteredMarkers,
		/**
		 * The tag for the clustering constant.
		 */
		clusteringConstant,
		/**
		 * The tag for colored markers.
		 */
		coloredMarkers,
		/**
		 * The tag for clustered markers.
		 */
		zoomLevel,
		/**
		 * The tag for the zoom level.
		 */
		clusteringThreshold,
		/**
		 * The tag for the clustering threshold.
		 */
		resetFilters,
		/**
		 * The tag for the clustering coefficient.
		 */
		clusteringCoefficient;

		@Override
		public String toString() {
			switch (this) {
			case clusteredMarkers:
				return "clusteredMarkers";
			case coloredMarkers:
				return "coloredMarkers";
			case zoomLevel:
				return "zoomLevel";
			case clusteringThreshold:
				return "clusteringTreshhold";
			case resetFilters:
				return "resetFilters";
			case clusteringCoefficient:
				return "clusteringCoefficient";
			case clusteringConstant:
				return "clusteringConstant";
			default:
				return "";
			}
		}
	}

	/**
	 * The function which returns a map containing the settings for the map. Due to simplicity only
	 * boolean values are returned at the moment.
	 *
	 * @return The map which contains the mapping from settings to the corresponding values.
	 */
	public Map<String, Boolean> getSettings() {
		Map<String, Boolean> map = new HashMap<>();
		map.put(Settings.clusteredMarkers.toString(), clusteredMarkers);
		map.put(Settings.coloredMarkers.toString(), coloredMarkers);
		map.put(Settings.resetFilters.toString(), resetFilters);
		return map;
	}

	/**
	 * The private constructor which initializes the settings with default values.
	 */
	private MapSettings() {
		zoomLevel = 0;
		clusteringTreshhold = 5;
		resetFilters = true;
		clusteringCoefficient = 0.55;
		coloredMarkers = true;
		clusteredMarkers = true;
		clusteringConstant = 15.00;
	}

	/**
	 * A function to change a specific setting. Therefore it takes a name for the setting as well as
	 * a corresponding value to be set.
	 *
	 * @param name
	 *            The name of the setting to be set.
	 * @param value
	 *            The value to be set for given setting.
	 */
	public void setSetting(String name, Object value) {
		if (Settings.clusteredMarkers.toString().equals(name)) {
			clusteredMarkers = (boolean) value;
		} else if (Settings.coloredMarkers.toString().equals(name)) {
			coloredMarkers = (boolean) value;
		} else if (Settings.zoomLevel.toString().equals(name)) {
			zoomLevel = (int) value;
		} else if (Settings.clusteringThreshold.toString().equals(name)) {
			clusteringTreshhold = (int) value;
		} else if (Settings.resetFilters.toString().equals(name)) {
			resetFilters = (boolean) value;
		} else if (Settings.clusteringCoefficient.toString().equals(name)) {
			clusteringCoefficient = (double) value;
		} else if (Settings.clusteringConstant.toString().equals(name)) {
			clusteringConstant = (double) value;
		}
	}

	/**
	 * Gets {@link #zoomLevel}.
	 *
	 * @return {@link #zoomLevel}
	 */
	public int getZoomLevel() {
		return this.zoomLevel;
	}

	/**
	 * Sets {@link #zoomLevel}.
	 *
	 * @param zoomLevel
	 *            New value for {@link #zoomLevel}
	 */
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	/**
	 * Gets {@link #clusteringTreshhold}.
	 *
	 * @return {@link #clusteringTreshhold}
	 */
	public int getClusteringTreshhold() {
		return this.clusteringTreshhold;
	}

	/**
	 * Sets {@link #clusteringTreshhold}.
	 *
	 * @param clusteringTreshhold
	 *            New value for {@link #clusteringTreshhold}
	 */
	public void setClusteringTreshhold(int clusteringTreshhold) {
		this.clusteringTreshhold = clusteringTreshhold;
	}

	/**
	 * Gets {@link #resetFilters}.
	 *
	 * @return {@link #resetFilters}
	 */
	public boolean isResetFilters() {
		return this.resetFilters;
	}

	/**
	 * Sets {@link #resetFilters}.
	 *
	 * @param resetFilters
	 *            New value for {@link #resetFilters}
	 */
	public void setResetFilters(boolean resetFilters) {
		this.resetFilters = resetFilters;
	}

	/**
	 * Gets {@link #clusteringCoefficient}.
	 *
	 * @return {@link #clusteringCoefficient}
	 */
	public double getClusteringCoefficient() {
		return this.clusteringCoefficient;
	}

	/**
	 * Sets {@link #clusteringCoefficient}.
	 *
	 * @param clusteringCoefficient
	 *            New value for {@link #clusteringCoefficient}
	 */
	public void setClusteringCoefficient(double clusteringCoefficient) {
		this.clusteringCoefficient = clusteringCoefficient;
	}

	/**
	 * Gets {@link #clusteringConstant}.
	 *
	 * @return {@link #clusteringConstant}
	 */
	public double getClusteringConstant() {
		return this.clusteringConstant;
	}

	/**
	 * Sets {@link #clusteringConstant}.
	 *
	 * @param clusteringConstant
	 *            New value for {@link #clusteringConstant}
	 */
	public void setClusteringConstant(double clusteringConstant) {
		this.clusteringConstant = clusteringConstant;
	}

	/**
	 * Gets {@link #coloredMarkers}.
	 *
	 * @return {@link #coloredMarkers}
	 */
	public boolean isColoredMarkers() {
		return this.coloredMarkers;
	}

	/**
	 * Sets {@link #coloredMarkers}.
	 *
	 * @param coloredMarkers
	 *            New value for {@link #coloredMarkers}
	 */
	public void setColoredMarkers(boolean coloredMarkers) {
		this.coloredMarkers = coloredMarkers;
	}

	/**
	 * Gets {@link #clusteredMarkers}.
	 *
	 * @return {@link #clusteredMarkers}
	 */
	public boolean isClusteredMarkers() {
		return this.clusteredMarkers;
	}

	/**
	 * Sets {@link #clusteredMarkers}.
	 *
	 * @param clusteredMarkers
	 *            New value for {@link #clusteredMarkers}
	 */
	public void setClusteredMarkers(boolean clusteredMarkers) {
		this.clusteredMarkers = clusteredMarkers;
	}
}
