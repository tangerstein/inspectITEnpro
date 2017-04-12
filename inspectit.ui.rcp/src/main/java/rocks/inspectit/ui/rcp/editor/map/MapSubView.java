package rocks.inspectit.ui.rcp.editor.map;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent.COMMAND;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import rocks.inspectit.ui.rcp.InspectITConstants;
import rocks.inspectit.ui.rcp.editor.AbstractSubView;
import rocks.inspectit.ui.rcp.editor.ISubView;
import rocks.inspectit.ui.rcp.editor.map.filter.MapFilter;
import rocks.inspectit.ui.rcp.editor.map.input.MapInputController;
import rocks.inspectit.ui.rcp.editor.map.model.InspectITMarker;
import rocks.inspectit.ui.rcp.editor.map.model.MapSettings;
import rocks.inspectit.ui.rcp.editor.preferences.PreferenceEventCallback.PreferenceEvent;
import rocks.inspectit.ui.rcp.editor.preferences.PreferenceId;

/**
 * @author Christopher Völker
 *
 */
public class MapSubView extends AbstractSubView {

	/**
	 * The Component holding the map and the filter panel.
	 */
	private Composite swtAwtComponent;

	private JComboBox<String> tagComboBox;
	private JPanel filterValuePanel;
	private JMenuBar optionsMenu;
	private String selection;

	/**
	 * The map filter panel.
	 */
	private JPanel filter;

	Map<String, MapFilter> filterMap;

	/**
	 * The map frame.
	 */
	JMapViewer mapViewer;

	/**
	 * The referenced input controller.
	 */
	private MapInputController mapInputController;

	/**
	 * Default constructor which needs a map input controller to create all the content etc.
	 *
	 * @param mapInputController
	 *            The map input controller.
	 */
	public MapSubView(MapInputController mapInputController) {
		this.mapInputController = mapInputController;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		mapInputController.setInputDefinition(getRootEditor().getInputDefinition());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPartControl(Composite parent, FormToolkit toolkit) {
		swtAwtComponent = new Composite(parent, SWT.EMBEDDED);
		java.awt.Frame frame = SWT_AWT.new_Frame(swtAwtComponent);
		tagComboBox = new JComboBox<>();
		filterValuePanel = new JPanel(new BorderLayout());
		optionsMenu = new JMenuBar();
		optionsMenu.add(createOptionMenu(mapInputController.getSettings()));
		filter = new JPanel();
		filter.setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(optionsMenu);
		centerPanel.add(tagComboBox);
		filter.add(centerPanel, BorderLayout.WEST);
		filter.add(filterValuePanel, BorderLayout.CENTER);

		frame.setLayout(new BorderLayout());
		mapViewer = new JMapViewer() {

			@Override
			public String getToolTipText(MouseEvent e) {
				List<InspectITMarker> temp = mapInputController.getClusteredMarkers(this.getPosition(e.getX(), e.getY()));
				if ((temp != null) && (temp.size() > 4)) {
					return String.valueOf(temp.size());
				}
				return "";
			}
		};
		mapViewer.setAutoscrolls(true);
		mapViewer.setInheritsPopupMenu(true);
		mapViewer.setScrollWrapEnabled(true);
		mapViewer.setToolTipText("");
		mapViewer.addJMVListener(new JMapViewerEventListener() {

			@Override
			public void processCommand(JMVCommandEvent arg0) {
				if (arg0.getCommand().equals(COMMAND.ZOOM)) {
					zoomLevelChanged();
				} else {
					// this would be triggered if the map is moved!
				}
			}
		});
		zoomLevelChanged();
		// this.add(mapInputController.getTestPanel(), BorderLayout.EAST);

		frame.add(filter, BorderLayout.NORTH);
		frame.add(mapViewer, BorderLayout.CENTER);
		mapInputController.doRefresh();
		mapViewer.setMapMarkerList((List<MapMarker>) mapInputController.getMapInput());
		filterMap = mapInputController.getMapFilter();
		refreshKeyBox();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<PreferenceId> getPreferenceIds() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doRefresh() {
		mapInputController.doRefresh();
		mapViewer.setMapMarkerList((List<MapMarker>) mapInputController.getMapInput());
		filterMap = mapInputController.getMapFilter();
		optionsMenu.removeAll();
		optionsMenu.add(createOptionMenu(mapInputController.getSettings()));
		updateFilterValues(mapInputController.getSettings());
		filterValuePanel.updateUI();
		optionsMenu.updateUI();
		mapViewer.updateUI();
		filter.updateUI();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preferenceEventFired(PreferenceEvent preferenceEvent) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataInput(List<? extends Object> data) {
		mapInputController.setData(data);
		refreshKeyBox();
		doRefresh();
	}

	public void setSelection(List<? extends Object> data) {
		mapInputController.setDataSelection(data);
		mapInputController.settingChanged(MapSettings.settings.coloredMarkers.toString(), false);
		doRefresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return swtAwtComponent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISelectionProvider getSelectionProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISubView getSubViewWithInputController(Class<?> inputControllerClass) {
		if (Objects.equals(inputControllerClass, mapInputController.getClass())) {
			return this;
		}
		return null;
	}

	/**
	 * Function which is called upon the change of the zoom Level of the map. It propagates the
	 * change to the mapInputController in order to adapt the data displayed data.
	 *
	 */
	private void zoomLevelChanged() {
		mapInputController.setZoomLevel(mapViewer.getZoom());
		doRefresh();
	}

	private JMenu createOptionMenu(Map<String, Boolean> settings) {
		JMenu menu = new JMenu("Options");
		for (String name : settings.keySet()) {
			JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem(name, settings.get(name));
			cbMenuItem.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					JCheckBoxMenuItem item = ((JCheckBoxMenuItem) e.getItem());
					mapInputController.settingChanged(item.getText(), item.isSelected());
					doRefresh();
				}
			});
			menu.add(cbMenuItem);
		}
		return menu;
	}

	private void updateFilterValues(Map<String, Boolean> settings) {
		filterValuePanel.removeAll();
		if ((selection != null) && !InspectITConstants.NOFILTER.equals(selection)) {
			JComponent filterValues = filterMap.get(selection).getPanel(new FilterValueObject());
			filterValuePanel.add(filterValues, BorderLayout.CENTER);
		}
	}

	private void refreshKeyBox() {
		// tagComboBox.removeAllItems();
		// add this manually in order to have it as first entry
		if (tagComboBox.getComponentCount() == 0) {
			tagComboBox.addItem(InspectITConstants.NOFILTER);
			selection = InspectITConstants.NOFILTER;
		}
		if (filterMap.entrySet() != null) {
			for (Entry<String, MapFilter> tag : filterMap.entrySet()) {
				Boolean unknownKey = true;
				for (int index = 0; index < tagComboBox.getItemCount(); index++) {
					if (tagComboBox.getItemAt(index).equals(tag.getKey())) {
						unknownKey = false;
						break;
					}
				}
				if (unknownKey) {
					tagComboBox.addItem(tag.getKey());
				}
			}
		}
		tagComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) tagComboBox.getSelectedItem();
				if ((selectedItem == null) || (selectedItem == selection)) {
					return;
				}
				selection = selectedItem;
				mapInputController.keySelectionChanged(selectedItem);
				doRefresh();
			}
		});
	}

	public class FilterValueObject {

		public void selectionChanged(Object value) {
			mapInputController.valueSelectionChanged(value);
			doRefresh();
		}
	}


}
