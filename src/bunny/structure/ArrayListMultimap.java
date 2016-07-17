package bunny.structure;

import java.util.AbstractList;
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
		map = new HashMap<>();
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
		return new ArrayListMultimapMapView();
	}
	
	private class ArrayListMultimapValueView extends AbstractList<V> {

		private K key;
		
		public ArrayListMultimapValueView(K key) {
			this.key = key;
		}
		
		@Override
		public int size() {
			List<V> list = map.get(key);
			if (list == null) return 0;
			return list.size();
		}
		
		@Override
		public V get(int index) {
			List<V> list = map.get(key);
			if (list == null) throw new IndexOutOfBoundsException();
			return list.get(index);
		}
		
		@Override
		public V set(int index, V value) {
			List<V> list = map.get(key);
			if (list == null) throw new IndexOutOfBoundsException();
			return list.set(index, value);
		}
		
		@Override
		public void add(int index, V value) {
			put(key, value);
		}
		
		@Override
		public V remove(int index) {
			List<V> list = map.get(key);
			if (list == null) throw new IndexOutOfBoundsException();
			count--;
			return list.remove(index);
		}
		
		@Override
		public void clear() {
			List<V> list = map.get(key);
			if (list != null) {
				count -= list.size();
				map.remove(key);
			}
		}
	}

	private class ArrayListMultimapMapView extends AbstractMap<K, List<V>> {

		@Override
		public boolean containsKey(Object key) {
			return map.containsKey(key);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public List<V> get(Object key) {
			return new ArrayListMultimapValueView((K)key);
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
