package rocks.inspectit.ui.rcp.editor.map.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christopher Völker
 *
 */
public class MapSettings {

	/**
	 * This saved as Singelton.
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

	public enum settings {
		clusteredMarkers, clusteringConstant, coloredMarkers, zoomLevel, clusteringTreshhold, resetFilters, clusteringCoefficient;

		@Override
		public String toString() {
			switch (this) {
			case clusteredMarkers:
				return "clusteredMarkers";
			case coloredMarkers:
				return "coloredMarkers";
			case zoomLevel:
				return "zoomLevel";
			case clusteringTreshhold:
				return "clusteringTreshhold";
			case resetFilters:
				return "resetFilters";
			case clusteringCoefficient:
				return "clusteringCoefficient";
			case clusteringConstant:
				return "clusteringConstant";
			}
			return "";
		}
	}


	public Map<String, Boolean> getSettings() {
		Map<String, Boolean> map = new HashMap<>();
		map.put(settings.clusteredMarkers.toString(), clusteredMarkers);
		map.put(settings.coloredMarkers.toString(), coloredMarkers);
		map.put(settings.resetFilters.toString(), resetFilters);
		return map;
	}

	public MapSettings() {
		zoomLevel = 0;
		clusteringTreshhold = 5;
		resetFilters = true;
		clusteringCoefficient = 0.55;
		coloredMarkers = true;
		clusteredMarkers = true;
		clusteringConstant = 15.00;
	}

	public void setSetting(String name, Object value) {
		if (settings.clusteredMarkers.toString().equals(name)) {
			clusteredMarkers = (boolean) value;
		} else if (settings.coloredMarkers.toString().equals(name)) {
			coloredMarkers = (boolean) value;
		} else if (settings.zoomLevel.toString().equals(name)) {
			zoomLevel = (int) value;
		} else if (settings.clusteringTreshhold.toString().equals(name)) {
			clusteringTreshhold = (int) value;
		} else if (settings.resetFilters.toString().equals(name)) {
			resetFilters = (boolean) value;
		} else if (settings.clusteringCoefficient.toString().equals(name)) {
			clusteringCoefficient = (double) value;
		} else if (settings.clusteringConstant.toString().equals(name)) {
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
