package rocks.inspectit.ui.rcp.editor.map.model;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.NavigableMap;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import rocks.inspectit.ui.rcp.editor.map.MapSubView.FilterValueObject;
import rocks.inspectit.ui.rcp.editor.map.filter.MarkerFilterElement;



public class StringFilterPanel<T> extends JPanel {

	/**
	 * Default constructor which needs a set of keys and the corresponding map which maps values to
	 * {@MarkerFilterElement}.
	 *
	 * @param keys
	 *            The set of keys.
	 * @param map
	 *            The value to {@MarkerFilterElement} map.
	 */
	public StringFilterPanel(final FilterValueObject filterValueObject, Set<T> keys, NavigableMap<T, MarkerFilterElement> map) {
		for (Object value : keys) {
			JPanel newCboxPanel = new JPanel(new BorderLayout());
			newCboxPanel.setBorder(new LineBorder(map.get(value).style().getBackColor(), 3));
			JCheckBox newCbox = new JCheckBox(String.valueOf(value));
			newCbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					filterValueObject.selectionChanged(((JCheckBox) e.getItem()).getText());
				}
			});
			newCbox.setSelected(true);
			newCboxPanel.add(newCbox, BorderLayout.CENTER);
			this.add(newCboxPanel);
		}
	}
}
