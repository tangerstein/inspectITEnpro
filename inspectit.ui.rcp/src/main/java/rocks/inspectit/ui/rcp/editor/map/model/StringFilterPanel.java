package rocks.inspectit.ui.rcp.editor.map.model;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.NavigableMap;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import rocks.inspectit.ui.rcp.editor.map.MapSubView.FilterValueObject;
import rocks.inspectit.ui.rcp.editor.map.filter.MarkerFilterElement;


/**
 * A Panel which takes care about all values of type string which can be shown or hidden on the map.
 *
 * @param <T>
 *            The type of the set of keys needed to access the values.
 *
 * @author Christopher Völker, Simon Lehmann
 *
 */
public class StringFilterPanel<T> extends JScrollPane {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor which needs a set of keys and the corresponding map which maps values to
	 * {@MarkerFilterElement}.
	 *
	 * @param filterValueObject
	 *            The object which serves as callback for any value being changed.
	 * @param keys
	 *            The set of keys.
	 * @param map
	 *            The value to {@MarkerFilterElement} map.
	 */
	public StringFilterPanel(final FilterValueObject filterValueObject, Set<T> keys, NavigableMap<T, MarkerFilterElement> map) {
		JPanel temp = new JPanel();
		for (Object value : keys) {
			JPanel newCboxPanel = new JPanel();
			newCboxPanel.setBorder(new LineBorder(map.get(value).style().getBackColor(), 3));
			JCheckBox newCbox = new JCheckBox(String.valueOf(value), map.get(value).isVisible());
			newCbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					filterValueObject.selectionChanged(((JCheckBox) e.getItem()).getText());
				}
			});
			newCboxPanel.add(newCbox);
			temp.add(newCboxPanel);
		}
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setViewportView(temp);
	}
}
