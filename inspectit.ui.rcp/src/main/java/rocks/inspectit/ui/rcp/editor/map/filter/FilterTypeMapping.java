package rocks.inspectit.ui.rcp.editor.map.filter;

import rocks.inspectit.shared.all.tracing.constants.MobileTags;
import rocks.inspectit.ui.rcp.InspectITConstants;

/**
 * The final class which provides a function for mapping a tag name to the correct filter object.
 *
 * @author Christopher Völker
 *
 */
public final class FilterTypeMapping {

	/**
	 * The private constructor for ensuring that this class is not instantiated.
	 */
	private FilterTypeMapping() {
	}

	/**
	 * The function which maps a tag name to the correct filter object initializing it with the
	 * given coloring flag.
	 *
	 * @param tagName
	 *            The tag name to create the filter for.
	 * @param coloring
	 *            The coloring flag to be set in the filter.
	 * @return The filter for the given tag name.
	 */
	public static MapFilter<?> getMapFilter(String tagName, Boolean coloring) {
		switch (tagName) {
		case (MobileTags.HTTP_REQUEST_NETWORKCONNECTION):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_REQUEST_NETWORKPROVIDER):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_NETWORKCONNECTION):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_NETWORKPROVIDER):
			return new StringMapFilter<String>(tagName, coloring);
		case (InspectITConstants.DURATION):
			return new NumericMapFilter<Double>(tagName, coloring);
		case (MobileTags.HTTP_URL):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.OPERATION_NAME):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_REQUEST_SSID):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_SSID):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_REQUEST_TIMEOUT):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_TIMEOUT):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_RESPONSE_RESPONSECODE):
			return new StringMapFilter<String>(tagName, coloring);
		case (MobileTags.HTTP_METHOD):
			return new StringMapFilter<String>(tagName, coloring);
		case (InspectITConstants.NOFILTER):
			return new StringMapFilter<String>(tagName, coloring);
		default:
			return null;
		}
	}

}
