package rocks.inspectit.ui.rcp.editor.map.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import rocks.inspectit.shared.all.tracing.data.Span;
import rocks.inspectit.ui.rcp.InspectITConstants;

/**
 * The map marker for single spans which extends the {@link MapMarkerDot} class of the JMapViewer
 * library. It also implements the custom InspectITMarker interface for inspectIT.
 *
 * @param <T>
 *            The generic parameter has to be a type extending Span.
 *
 * @author Christopher Völker, Simon Lehmann
 *
 */
public class InspectITSpanMarker<T extends Span> extends MapMarkerDot implements InspectITMarker<T> {

	/**
	 * The span stored in this marker.
	 */
	private T span;

	/**
	 * Default constructor which needs a span and a coordinate.
	 *
	 * @param span
	 *            The span which is to be stored in this marker.
	 * @param coord
	 *            The coordinate for placing the marker on a map.
	 */
	public InspectITSpanMarker(T span, Coordinate coord) {
		super(coord);
		this.span = span;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColor() {
		return new Color(0, 0, 0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getTags() {
		Map<String, Object> map = new HashMap<>();
		map.put(InspectITConstants.DURATION, String.valueOf(span.getDuration()));
		map.putAll(span.getTags());
		return map;
	}
}
