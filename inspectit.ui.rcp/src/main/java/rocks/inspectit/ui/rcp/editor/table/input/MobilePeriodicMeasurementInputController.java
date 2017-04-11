package rocks.inspectit.ui.rcp.editor.table.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import rocks.inspectit.shared.all.communication.comparator.DefaultDataComparatorEnum;
import rocks.inspectit.shared.all.communication.comparator.IDataComparator;
import rocks.inspectit.shared.all.communication.comparator.ResultComparator;
import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;
import rocks.inspectit.shared.all.tracing.data.Span;
import rocks.inspectit.shared.cs.cmr.service.IMobilePeriodicMeasurementAccessService;
import rocks.inspectit.ui.rcp.InspectIT;
import rocks.inspectit.ui.rcp.InspectITImages;
import rocks.inspectit.ui.rcp.editor.inputdefinition.InputDefinition;
import rocks.inspectit.ui.rcp.editor.root.IRootEditor;
import rocks.inspectit.ui.rcp.editor.table.TableViewerComparator;
import rocks.inspectit.ui.rcp.editor.viewers.StyledCellIndexLabelProvider;
import rocks.inspectit.ui.rcp.formatter.NumberFormatter;

/**
 * @author Simon Lehmann
 *
 */
public class MobilePeriodicMeasurementInputController extends AbstractTableInputController {

	/**
	 * The private inner enumeration used to define the used IDs which are mapped into the columns.
	 * The order in this enumeration represents the order of the columns. If it is reordered,
	 * nothing else has to be changed.
	 *
	 * @author Patrice Bouillet
	 * @author Ivan Senic
	 *
	 */
	private static enum Column {
		/** The timestamp column. */
		TIMESTAMP("Timestamp", 200, InspectITImages.IMG_TIMESTAMP, DefaultDataComparatorEnum.TIMESTAMP),
		/** The package column. */
		BATTERYPOWER("Battery Power", 200, null, null),
		/** The class column. */
		CPUUSAGE("CPU Usage", 200, null, null),
		/** The method column. */
		MEMORYUSAGE("Memory Usage", 200, null, null),
		/** The count column. */
		STORAGEUSAGE("Storage Usage", 200, null, null);

		/** The name. */
		private String name;
		/** The width of the column. */
		private int width;
		/** The image descriptor. Can be <code>null</code> */
		private Image image;
		/** If the column should be shown in aggregated mode. */
		private IDataComparator<? super MobilePeriodicMeasurement> dataComparator;

		/**
		 * Default constructor which creates a column enumeration object.
		 *
		 * @param name
		 *            The name of the column.
		 * @param width
		 *            The width of the column.
		 * @param imageName
		 *            The name of the image. Names are defined in {@link InspectITImages}.
		 * @param dataComparator
		 *            Comparator for the column.
		 *
		 */
		Column(String name, int width, String imageName, IDataComparator<? super MobilePeriodicMeasurement> dataComparator) {
			this.name = name;
			this.width = width;
			this.image = InspectIT.getDefault().getImage(imageName);
			this.dataComparator = dataComparator;
		}

		/**
		 * Converts an ordinal into a column.
		 *
		 * @param i
		 *            The ordinal.
		 * @return The appropriate column.
		 */
		public static Column fromOrd(int i) {
			if ((i < 0) || (i >= Column.values().length)) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return Column.values()[i];
		}
	}

	/**
	 * Default comparator when no sorting is defined.
	 */
	private final ResultComparator<MobilePeriodicMeasurement> defaultComparator = new ResultComparator<>(DefaultDataComparatorEnum.TIMESTAMP, false);
	/**
	 * Periodic Measurement service.
	 */
	private IMobilePeriodicMeasurementAccessService mobilePeriodicMeasurementAccessService;
	/**
	 * List of mobile periodic measurements to be displayed.
	 */
	private Collection<MobilePeriodicMeasurement> measurementList = new ArrayList<>();

	/**
	 * The double clicked span.
	 */
	private Span selection;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInputDefinition(InputDefinition inputDefinition) {
		super.setInputDefinition(inputDefinition);
		mobilePeriodicMeasurementAccessService = inputDefinition.getRepositoryDefinition().getMobilePeriodicMeasurementAccess();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createColumns(TableViewer tableViewer) {
		for (Column column : Column.values()) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			viewerColumn.getColumn().setMoveable(true);
			viewerColumn.getColumn().setResizable(true);
			viewerColumn.getColumn().setText(column.name);
			viewerColumn.getColumn().setWidth(column.width);
			if (null != column.image) {
				viewerColumn.getColumn().setImage(column.image);
			}
			mapTableViewerColumn(column, viewerColumn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IContentProvider getContentProvider() {
		return new ArrayContentProvider();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBaseLabelProvider getLabelProvider() {
		return new MobilePeriodicMeasurementLabelProvider();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewerComparator getComparator() {
		TableViewerComparator<MobilePeriodicMeasurement> measurementViewerComparator = new TableViewerComparator<MobilePeriodicMeasurement>();

		measurementViewerComparator.addColumn(getMappedTableViewerColumn(Column.TIMESTAMP).getColumn(), defaultComparator);
		return measurementViewerComparator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getReadableString(Object object) {
		if (object instanceof MobilePeriodicMeasurement) {
			MobilePeriodicMeasurement data = (MobilePeriodicMeasurement) object;
			StringBuilder sb = new StringBuilder();
			for (Column column : Column.values()) {
				sb.append(getStyledTextForColumn(data, column).toString());
				sb.append('\t');
			}
			return sb.toString();
		}
		throw new RuntimeException("Could not create the human readable string!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getColumnValues(Object object) {
		if (object instanceof MobilePeriodicMeasurement) {
			MobilePeriodicMeasurement data = (MobilePeriodicMeasurement) object;
			List<String> values = new ArrayList<>();
			for (Column column : Column.values()) {
				values.add(getStyledTextForColumn(data, column).toString());
			}
			return values;
		}
		throw new RuntimeException("Could not create the column values!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getTableInput() {
		return measurementList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doRefresh(IProgressMonitor monitor, IRootEditor rootEditor) {
		monitor.beginTask("Getting mobile periodic measurement information..", IProgressMonitor.UNKNOWN);
		loadDataFromService();
		monitor.done();
	}

	/**
	 * Loads data from the service with current filters.
	 */
	protected void loadDataFromService() {
		
		//if (null != selection) {
		//	if (PropagationType.IOS.equals(selection.getPropagationType())) {
		//		long fromTimestamp = selection.getTimeStamp().getTime();
		//		long toTimestamp = (long) (fromTimestamp + selection.getDuration());
		//		measurementList = mobilePeriodicMeasurementAccessService.getMobilePeriodicMeasurementInstances(Long.parseLong(selection.getTags().get("deviceID")), fromTimestamp, toTimestamp);
		//		System.out.println(measurementList.size());
		//		return;
		//	}
		//}
		measurementList = mobilePeriodicMeasurementAccessService.getMobilePeriodicMeasurementInstances();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canOpenInput(List<? extends Object> data) {
		if (!data.isEmpty()) {
			selection = (Span) data.get(0);
		}
		return false;
	}

	/**
	 * Label provider for the view.
	 *
	 * @author Simon Lehmann
	 *
	 */
	private final class MobilePeriodicMeasurementLabelProvider extends StyledCellIndexLabelProvider {

		/**
		 * The resource manager is used for the images etc.
		 */
		private LocalResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

		/**
		 * {@inheritDoc}
		 */
		@Override
		public StyledString getStyledText(Object element, int index) {
			MobilePeriodicMeasurement data = (MobilePeriodicMeasurement) element;
			Column enumId = Column.fromOrd(index);

			StyledString styledString = getStyledTextForColumn(data, enumId);
			return styledString;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			resourceManager.dispose();
			super.dispose();
		}

	}

	/**
	 * Returns the styled text for a specific column.
	 *
	 * @param data
	 *            The data object to extract the information from.
	 * @param enumId
	 *            The enumeration ID.
	 * @return The styled string containing the information from the data object.
	 */
	private StyledString getStyledTextForColumn(MobilePeriodicMeasurement data, Column enumId) {
		switch (enumId) {
		case TIMESTAMP:
			return new StyledString(NumberFormatter.formatTimeWithMillis(data.getTimeStamp()));
		case BATTERYPOWER:
			return new StyledString(NumberFormatter.formatDouble(data.getBatteryPower() * 100, 0) + " %");
		case CPUUSAGE:
			return new StyledString(NumberFormatter.formatDouble(data.getCpuUsage() * 100, 2) + " %");
		case MEMORYUSAGE:
			return new StyledString(NumberFormatter.formatDouble(data.getMemoryUsage() * 100, 2) + " %");
		case STORAGEUSAGE:
			return new StyledString(NumberFormatter.formatDouble(data.getStorageUsage() * 100, 2) + " %");
		default:
			return new StyledString("error");
		}
	}

}
