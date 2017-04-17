package rocks.inspectit.ui.rcp.editor.map.filter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * The map filter which takes care about the available filter keys and the coloring for the
 * corresponding values.
 *
 * @param <T>
 *            The generic parameter of which type the keys are.
 *
 * @author Christopher Völker, Simon Lehmann
 *
 */
public abstract class AbstractMapFilter<T> implements MapFilter<T> {
	/**
	 * The tag key this filter was created for.
	 */
	protected String tagKey;
	/**
	 * The current color list for this filter.
	 */
	protected List<Color> colorList;
	/**
	 * The map containing the mapping of values to {@link MarkerFilterElement}.
	 */
	protected NavigableMap<T, MarkerFilterElement> filterMap;
	/**
	 * The boolean flag for colored markers.
	 */
	boolean isColored;

	/**
	 * The constructor for this filter which needs a tag key as well as the initial setting for
	 * coloring.
	 *
	 * @param tagKey
	 *            The tag key this filter belongs to.
	 * @param colored
	 *            The initial boolean value for coloring.
	 *
	 */
	public AbstractMapFilter(String tagKey, boolean colored) {
		this.isColored = colored;
		colorList = new ArrayList<>();
		filterMap = new TreeMap<>();
		this.tagKey = tagKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putFilterConstraint(T key, MarkerFilterElement element) {
		if (key != null) {
			if (element != null) {
				filterMap.put(key, element);
			} else {
				filterMap.put(key, new MarkerFilterElement());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Color> getAvailableColor() {
		return this.colorList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColored(boolean colored) {
		this.isColored = colored;
		initColors();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MarkerFilterElement getFilter(T key) {
		if (filterMap.isEmpty()) {
			return new MarkerFilterElement();
		}
		if (key != null) {
			if (filterMap.ceilingEntry(key) != null) {
				return filterMap.ceilingEntry(key).getValue();
			}
		}
		return new MarkerFilterElement(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<T> getKeys() {
		return filterMap.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getValue(T key) {
		return (T) getFilter(key);
	}



}
