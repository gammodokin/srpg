package myutil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SortedMap<K, V> extends HashMap<K, V> {

	private List<V> sorted;
	private Comparator<V> comp;
	
	public SortedMap() {
		sorted = new ArrayList<V>();
	}
	
	public SortedMap(Comparator<V> c) {
		this();
		comp = c;
	}

	public void clear() {
		super.clear();
		sorted.clear();
	}

	public V put(K key, V value) {
		sorted.add(value);
		Collections.sort(sorted, comp);
		return super.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		for(Map.Entry<?, ? extends V> e : m.entrySet())
			sorted.add(e.getValue());
		super.putAll(m);
	}

	public V remove(Object key) {
		sorted.remove(super.get(key));
		return super.remove(key);
	}
	
	public void update(K key) {
		Collections.sort(sorted, comp);
	}
	
	public List<V> getSorted() {
		return sorted;
	}

}
