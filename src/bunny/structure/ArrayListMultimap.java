package bunny.structure;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayListMultimap<K, V> extends AbstractMultimap<K, V> {

	private int count = 0;
	private HashMap<K, List<V>> map;
	
	public ArrayListMultimap() {
		map = new HashMap<K, List<V>>();
	}

	@Override
	public int size() {
		return count;
	}
	
	@Override
	public boolean put(K key, V value) {
		List<V> list = map.get(key);
		if (list == null) {
			list = new ArrayList<V>();
			map.put(key, list);
		}
		list.add(value);
		count++;
		return true;
	}

	@Override
	public Map<K, List<V>> asMap() {
		// TODO Auto-generated method stub
		return null;
	}

	private class ArrayListMultimapMapView extends AbstractMap<K, List<V>> {

		@Override
		public boolean containsKey(Object key) {
			return map.containsKey(key);
		}
		
		@Override
		public List<V> get(Object key) {
			// TODO
			return null;
		}
		
		@Override
		public List<V> remove(Object key) {
			if (map.containsKey(key)) {
				List<V> oldList = map.get(key);
				map.remove(key);
				count -= oldList.size();
				return oldList;
			}
			return null;
		}
		
		@Override
		public Set<java.util.Map.Entry<K, List<V>>> entrySet() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
