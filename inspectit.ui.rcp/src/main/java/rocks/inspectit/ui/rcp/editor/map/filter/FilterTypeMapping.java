package rocks.inspectit.ui.rcp.editor.map.filter;

import rocks.inspectit.shared.all.tracing.constants.MobileTags;
import rocks.inspectit.ui.rcp.InspectITConstants;

/**
 * @author Christopher Völker
 *
 */
public class FilterTypeMapping {

	private FilterTypeMapping() {
	}

	public static MapFilter getMapFilter(String tagName, Boolean coloring) {
		switch (tagName) {
		case (MobileTags.HTTP_REQUEST_NETWORKCONNECTION):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_REQUEST_NETWORKPROVIDER):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_NETWORKCONNECTION):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_NETWORKPROVIDER):
			return new StringMapFilter(tagName, coloring);
		case (InspectITConstants.DURATION):
			return new NumericMapFilter<Double>(tagName, coloring);
		case (MobileTags.HTTP_URL):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.OPERATION_NAME):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_REQUEST_SSID):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_SSID):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_REQUEST_TIMEOUT):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_TIMEOUT):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_RESPONSECODE):
			return new StringMapFilter(tagName, coloring);
		case (MobileTags.HTTP_METHOD):
			return new StringMapFilter(tagName, coloring);
		case (InspectITConstants.NOFILTER):
			return new StringMapFilter<String>(tagName, coloring);
		}

		return null;
	}

}
