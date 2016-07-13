package bunny.structure;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import bunny.wrap.SetView;

public class ListMultimap<K, V> implements Multimap<K, V> {

	private int count = 0;
	private HashMap<K, ArrayList<V>> map;
	
	public ListMultimap() {
		map = new HashMap<K, ArrayList<V>>();
	}
	
	@Override
	public int size() {
		return count;
	}

	@Override
	public boolean isEmpty() {
		return count == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		for (ArrayList<V> list : map.values()) {
			if (list.contains(value)) return true;
		}
		return false;
	}

	@Override
	public boolean containsEntry(Object key, Object value) {
		ArrayList<V> list = map.get(key);
		if (list == null) return false;
		return list.contains(value);
	}

	@Override
	public boolean put(K key, V value) {
		ArrayList<V> list = map.get(key);
		if (list == null) {
			list = new ArrayList<V>();
			map.put(key, list);
		}
		list.add(value);
		count++;
		return true;
	}

	@Override
	public boolean remove(Object key, Object value) {
		ArrayList<V> list = map.get(key);
		if (list == null) return false;
		if (list.remove(value)) {
			count--;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean putAll(K key, Iterable<? extends V> values) {
		Iterator<? extends V> it = values.iterator();
		if (!it.hasNext()) return false;
		ArrayList<V> list = map.get(key);
		if (list == null) {
			list = new ArrayList<V>();
			map.put(key, list);
		}
		while (it.hasNext()) {
			list.add(it.next());
			count++;
		}
		return true;
	}

	@Override
	public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
		Iterator<? extends V> it = values.iterator();
		if (!it.hasNext()) return removeAll(key);
		
		ArrayList<V> oldList = map.get(key);
		if (oldList == null) {
			oldList = new ArrayList<V>(0);
		}
		count -= oldList.size();
		
		ArrayList<V> newList = new ArrayList<V>();
		map.put(key, newList);
		while (it.hasNext()) {
			newList.add(it.next());
			count++;
		}
		return oldList;
	}

	@Override
	public Collection<V> removeAll(Object key) {
		ArrayList<V> oldList = map.get(key);
		if (oldList == null) {
			oldList = new ArrayList<V>(0);
		} else {
			map.remove(key);
			count -= oldList.size();
		}
		return oldList;
	}

	@Override
	public void clear() {
		map.clear();
		count = 0;
	}

	@Override
	public Collection<V> get(K key) {
		return new KeyView(key);
	}

	@Override
	public Set<K> keySet() {
		return SetView.of(map.keySet()).readOnly();
	}

	@Override
	public Multiset<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Map.Entry<K, V>> entries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<K, Collection<V>> asMap() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	private class KeyView extends AbstractList<V> {

		private K key;
		
		public KeyView(K key) {
			this.key = key;
		}
		
		@Override
		public int size() {
			ArrayList<V> list = map.get(key);
			if (list == null) return 0;
			return list.size();
		}
		
		@Override
		public V get(int index) {
			ArrayList<V> list = map.get(key);
			if (list == null) throw new IndexOutOfBoundsException();
			return list.get(index);
		}
		
		@Override
		public V set(int index, V value) {
			ArrayList<V> list = map.get(key);
			if (list == null) throw new IndexOutOfBoundsException();
			return list.set(index, value);
		}
		
		@Override
		public void add(int index, V value) {
			ArrayList<V> list = map.get(key);
			int size = (list == null ? 0 : list.size());
			if (index < 0 || index > size) throw new IndexOutOfBoundsException();
			if (list == null) {
				list = new ArrayList<V>();
				map.put(key, list);
			}
			list.add(index, value);
			count++;
		}
		
		@Override
		public V remove(int index) {
			ArrayList<V> list = map.get(key);
			if (list == null) throw new IndexOutOfBoundsException();
			V removed = list.remove(index);
			if (removed != null) {
				count--;
			}
			return removed;
		}
	}
}
