package rocks.inspectit.shared.all.tracing.constants;

/**
 * Mobile tag keys that are not defined by the opentracing and used exclusively by inspectIT to
 * enrich the span information for mobile spans.
 * <p>
 * Not using the opentracing.io Tag implementation due to the default package modifiers.
 *
 * @author Christopher Völker
 *
 */
public interface MobileTags extends ExtraTags {
	/**
	 * Span kind of the span (usually client or server).
	 */
	String SPAN_KIND = "span.kind";

	/**
	 * latitude value during the http request.
	 */
	String HTTP_REQUEST_LATITUDE = "http.request.latitude";

	/**
	 * longitude value during the http request.
	 */
	String HTTP_REQUEST_LONGITUDE = "http.request.longitude";

	/**
	 * Network connection during the http request.
	 */
	String HTTP_REQUEST_NETWORKCONNECTION = "http.request.networkConnection";

	/**
	 * Network provider during the http request.
	 */
	String HTTP_REQUEST_NETWORKPROVIDER = "http.request.networkProvider";

	/**
	 * SSID during the http request.
	 */
	String HTTP_REQUEST_SSID = "http.request.ssid";

	/**
	 * Timeout of the http request.
	 */
	String HTTP_REQUEST_TIMEOUT = "http.request.timeout";

	/**
	 * Timeout of the http response.
	 */
	String HTTP_RESPONSE_TIMEOUT = "http.response.timeout";

	/**
	 * latitude value during the http response.
	 */
	String HTTP_RESPONSE_LATITUDE = "http.response.latitude";

	/**
	 * longitude value during the http response.
	 */
	String HTTP_RESPONSE_LONGITUDE = "http.response.longitude";

	/**
	 * Network connection during the http response.
	 */
	String HTTP_RESPONSE_NETWORKCONNECTION = "http.response.networkConnection";

	/**
	 * Network provider during the http response.
	 */
	String HTTP_RESPONSE_NETWORKPROVIDER = "http.response.networkProvider";

	/**
	 * response code for the http response.
	 */
	String HTTP_RESPONSE_RESPONSECODE = "http.response.responseCode";

	/**
	 * SSID during the http response.
	 */
	String HTTP_RESPONSE_SSID = "http.response.ssid";

	/**
	 * URL of the http request.
	 */
	String HTTP_URL = "http.url";

	/**
	 * Method of the http request.
	 */
	String HTTP_METHOD = "http.method";
}
