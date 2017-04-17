package rocks.inspectit.ui.rcp.editor.map.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rocks.inspectit.shared.all.tracing.constants.MobileTags;
import rocks.inspectit.shared.all.tracing.data.AbstractSpan;
import rocks.inspectit.shared.all.tracing.data.Span;
import rocks.inspectit.shared.cs.cmr.service.ISpanService;
import rocks.inspectit.ui.rcp.editor.inputdefinition.InputDefinition;
import rocks.inspectit.ui.rcp.editor.map.model.MapSettings;

/**
 * The actual implementation of the {@link MapInputController} for the map sub view within the
 * tracing view.
 *
 * @author Christopher Völker
 *
 */
public class TraceMapInputController extends AbstractMapInputController {

	/**
	 * The {@link ISpanService} which further span can be retrieved from.
	 */
	ISpanService spanService;
	/**
	 * The list of all spans known to the tracing view.
	 */
	List<AbstractSpan> spans;
	/**
	 * The list of spans of the selection of spans on the tracing view.
	 */
	List<AbstractSpan> selection;

	/**
	 * The constructor of this class which initializes the lists.
	 */
	public TraceMapInputController() {
		super();
		spans = new ArrayList<>();
		selection = new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInputDefinition(InputDefinition inputDefinition) {
		super.setInputDefinition(inputDefinition);
		spanService = inputDefinition.getRepositoryDefinition().getSpanService();
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void setData(List<? extends Object> data) {
		if (spans.isEmpty()) {
			MapSettings.getInstance().setResetFilters(true);
		}
		spans = retrieveChildSpans(data);
		refreshFilters(spans);
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void setDataSelection(List<? extends Object> data) {
		if (data.isEmpty()) {
			selection = new ArrayList<>();
			return;
		}
		selection = retrieveChildSpans(data);
	}

	/**
	 * The function which retrieves the children of all given spans.
	 *
	 * @param data
	 *            The spans to retrieve the children from.
	 *
	 * @return The list of child spans.
	 */
	private List<AbstractSpan> retrieveChildSpans(List<? extends Object> data) {
		List<AbstractSpan> list = new ArrayList<>();
		for (Object rootSpan : data) {
			for (Span span : spanService.getSpans(((AbstractSpan) rootSpan).getSpanIdent().getTraceId())) {
				Map<String, String> tags = span.getTags();
				if ((tags.containsKey(MobileTags.HTTP_REQUEST_LATITUDE) && tags.containsKey(MobileTags.HTTP_REQUEST_LONGITUDE))
						|| (tags.containsKey(MobileTags.HTTP_RESPONSE_LATITUDE) && tags.containsKey(MobileTags.HTTP_RESPONSE_LONGITUDE))) {
					/*
					 * usually use cases do not last long enough in order to have a significant
					 * difference with respect to the location. Adding all spans of a root span
					 * would result in many markers being placed at exactly the same location.
					 * Nevertheless it might be a good idea to check for it and define a specific
					 * minimum difference in order to be picked up separately.
					 */
					list.add((AbstractSpan) span);
					break;
				}
			}
		}
		return list;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void doRefresh() {
		if (selection.isEmpty()) {
			refreshFilters(spans);
			clusterMarkers(spans);
		} else {
			refreshFilters(selection);
			clusterMarkers(selection);
		}

	}
}
