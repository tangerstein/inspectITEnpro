package rocks.inspectit.server.service.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.diagnoseit.standalone.Launcher;
import org.diagnoseit.standalone.Launcher.RulePackage;
import org.spec.research.open.xtrace.adapters.inspectit.source.InspectITTraceConverter;
import org.spec.research.open.xtrace.api.core.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.gsonfire.GsonFireBuilder;
import io.gsonfire.TypeSelector;
import rocks.inspectit.agent.java.sdk.opentracing.internal.impl.SpanImpl;
import rocks.inspectit.agent.java.tracing.core.transformer.SpanTransformer;
import rocks.inspectit.server.cache.IBuffer;
import rocks.inspectit.server.cache.impl.BufferElement;
import rocks.inspectit.server.service.rest.error.JsonError;
import rocks.inspectit.server.util.CacheIdGenerator;
import rocks.inspectit.server.util.PlatformIdentCache;
import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.DefaultData;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;
import rocks.inspectit.shared.all.tracing.data.AbstractSpan;
import rocks.inspectit.shared.all.tracing.data.Span;
import rocks.inspectit.shared.cs.cmr.service.IInvocationDataAccessService;
import rocks.inspectit.shared.cs.cmr.service.ISpanService;

/**
 * Restful service provider for detail information.
 *
 * @author Tobias Angerstein, Alper Hidiroglu, Manuel Palenga
 *
 */
@Controller
@RequestMapping(value = "/mobile/newinvocation")
public class AgentRestfulService {

	@Autowired
	private PlatformIdentCache platformCache;

	@Autowired
	private IBuffer<DefaultData> buffer;

	@Autowired
	private CacheIdGenerator idGenerator;

	@Autowired
	private IInvocationDataAccessService dataAccessService;

	@Autowired
	private ISpanService spansService;

	/**
	 * Handling of all the exceptions happening in this controller.
	 *
	 * @param exception
	 *            Exception being thrown
	 * @return {@link ModelAndView}
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception exception) {
		return new JsonError(exception).asModelAndView();
	}

	@RequestMapping(method = POST, value = "")
	@ResponseBody
	public void addNewMobileRoot(@RequestBody final String json) {

		new Thread(new Runnable() {

			@Override
			public void run() {


				Gson gson = getGson();
				MobileRoot mobileRoot = gson.fromJson(json, MobileRoot.class);
				HashMap<Long, List<AbstractSpan>> spansFromMobileRoot = new HashMap<Long, List<AbstractSpan>>();

				for (SpanImpl span : mobileRoot.spans) {
					span.setTag("deviceID", mobileRoot.getDeviceID());
					AbstractSpan abstractSpan = SpanTransformer.transformSpan(span);
					abstractSpan.setPlatformIdent(-1);
					abstractSpan.setMethodIdent(0);
					abstractSpan.setSensorTypeIdent(0);
					idGenerator.assignObjectAnId(abstractSpan);

					long traceId = abstractSpan.getSpanIdent().getTraceId();

					if (!spansFromMobileRoot.containsKey(traceId)) {
						spansFromMobileRoot.put(traceId, new ArrayList<AbstractSpan>());
					}
					spansFromMobileRoot.get(traceId).add(abstractSpan);

					buffer.put(new BufferElement<DefaultData>(abstractSpan));
				}

				for (MobilePeriodicMeasurement measurement : mobileRoot.measurements) {
					measurement.setDeviceID(mobileRoot.getDeviceID());
					measurement.setPlatformIdent(-2);
					measurement.setSensorTypeIdent(0);
					idGenerator.assignObjectAnId(measurement);

					measurement.setTimeStamp(new Timestamp(measurement.getTimestamp()));

					buffer.put(new BufferElement<DefaultData>(measurement));
				}

				// Get all PlatformIdents
				List<PlatformIdent> platformIdentList = new ArrayList<PlatformIdent>();
				Collection<PlatformIdent> platformIdents = platformCache.getCleanPlatformIdents();
				for (PlatformIdent platIdent : platformIdents) {
					platformIdentList.add(platIdent);
				}

				List<InvocationSequenceData> listSequences = dataAccessService.getInvocationSequenceOverview(0, -1, null);
				List<InvocationSequenceData> listSequencesDetail = new LinkedList<InvocationSequenceData>();

				// Get all invocs
				for (InvocationSequenceData invocationSequenceData : listSequences) {
					InvocationSequenceData invocDetail = dataAccessService.getInvocationSequenceDetail(invocationSequenceData);
					// is already nested sequence?
					if (invocDetail != null) {
						listSequencesDetail.add(invocDetail);
					}
				}

				if(timeoutFound(spansFromMobileRoot)){
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				InspectITTraceConverter converter = new InspectITTraceConverter();

				// convert spans and trigger diagnoseIT
				for (Entry<Long, List<AbstractSpan>> entry : spansFromMobileRoot.entrySet()) {
					HashSet<Span> abstractSpans = new HashSet<Span>();
					abstractSpans.addAll(spansService.getSpans(entry.getKey()));
					abstractSpans.addAll(entry.getValue());
					Trace trace = converter.convertTraces(listSequencesDetail, platformIdentList,
							new ArrayList<Span>(abstractSpans), mobileRoot.measurements);
					Launcher.startLauncher(trace, RulePackage.MobilePackage);
				}
			}
		}).start();
	}

	private boolean timeoutFound(HashMap<Long, List<AbstractSpan>> spansFromMobileRoot) {
		for (Entry<Long, List<AbstractSpan>> entry : spansFromMobileRoot.entrySet()) {
			for(AbstractSpan span: entry.getValue()){
				String timeout = span.getTags().get("http.request.timeout");
				if((timeout != null) && timeout.equals("true")){
					return true;
				}
			}
		}
		return false;
	}

	private Gson getGson() {
		GsonFireBuilder builder = new GsonFireBuilder().registerTypeSelector(DefaultData.class,
				new TypeSelector<DefaultData>() {

			@Override
			public Class<? extends DefaultData> getClassForElement(JsonElement readElement) {
				String type = readElement.getAsJsonObject().get("type").getAsString();

				if (type.equals("MobilePeriodicMeasurement")) {
					return MobilePeriodicMeasurement.class;
				} else {
					/*
					 * returning null will trigger Gson's default
					 * behavior
					 */
					return null;
				}
			}
		});
		Gson gson = builder.createGson();
		return gson;
	}

	/**
	 * Header information for swagger requests.
	 *
	 * @param response
	 *            Response information
	 */
	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	}

	private class MobileRoot {

		private long deviceID;
		private List<SpanImpl> spans;
		private List<MobilePeriodicMeasurement> measurements;

		public MobileRoot() {
			spans = new ArrayList<SpanImpl>();
			measurements = new ArrayList<MobilePeriodicMeasurement>();
		}

		public void setDeviceID(long deviceID) {
			this.deviceID = deviceID;
		}

		public long getDeviceID() {
			return deviceID;
		}

		public List<MobilePeriodicMeasurement> getMeasurements() {
			return measurements;
		}

		public List<SpanImpl> getSpans() {
			return spans;
		}
	}
}
