package rocks.inspectit.ui.rcp.editor.map.model;

import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;

import rocks.inspectit.shared.all.tracing.data.Span;

/**
 * This class realizes a clustering marker by extending the JMapViewer MapMarkerCircle as well as
 * implementing the InspectITMarker interface.
 *
 * @author Christopher Völker
 *
 * @param <T>
 *            The generic parameter which has to extend a Span
 */
public class InspectITClusterMarker<T extends Span> extends MapMarkerCircle implements InspectITMarker<T> {

	/**
	 * Default constructor which needs a list of spans as well as a coordinate and radius for this
	 * cluster marker.
	 *
	 * @param coord
	 *            The coordinate need for placing this marker on a map.
	 * @param radius
	 *            The radius for the circle displayed by this marker.
	 */
	public InspectITClusterMarker(Coordinate coord, Double radius) {
		super(coord, radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getTags() {
		Map<String, Object> map = new HashMap<>();
		//map.put("duration", )
		return map;
	}
}
