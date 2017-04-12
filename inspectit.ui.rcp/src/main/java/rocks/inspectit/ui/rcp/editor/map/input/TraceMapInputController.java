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


public class TraceMapInputController extends AbstractMapInputController {

	ISpanService spanService;
	List<AbstractSpan> spans;
	List<AbstractSpan> selection;

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

	@Override
	public void setData(List<? extends Object> data) {
		if (spans.isEmpty()) {
			MapSettings.getInstance().setResetFilters(true);
		}
		retrieveChildSpans(data);
		System.out.println(spans.size());

		refreshFilters(spans);
	}

	@Override
	public void setDataSelection(List<? extends Object> data) {
		selection = (List<AbstractSpan>) data;
		if (!selection.isEmpty()) {
			retrieveChildSpans(data);
		}
		System.out.println(selection.size());
	}

	private void retrieveChildSpans(List<? extends Object> data) {
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
		spans = list;
	}

	@Override
	public void doRefresh() {
		for (Object o : spans) {
			long id = ((AbstractSpan) o).getSpanIdent().getTraceId();
		}
		refreshFilters(spans);
		clusterMarkers(spans);


	}

}
