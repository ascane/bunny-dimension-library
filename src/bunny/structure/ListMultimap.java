package bunny.structure;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import bunny.wrap.SetView;
import bunny.wrap.Wrapper;

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
		boolean changed = false;
		for (Entry<? extends K, ? extends V> entry : multimap.entries()) {
			if (put(entry.getKey(), entry.getValue())) {
				changed = true;
			}
		}
		return changed;
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
		return new ListMultimapValueListView(key);
	}

	@Override
	public Set<K> keySet() {
		return new ListMultisetKeySet();
	}

	@Override
	public Multiset<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		return new Wrapper<Entry<K, V>, V>(entries(), new Function<Entry<K, V>, V>() {
			@Override
			public V apply(Entry<K, V> t) {return t.getValue();}
		});
	}

	@Override
	public Collection<Map.Entry<K, V>> entries() {
		return new ListMultimapEntryCollection();
	}

	@Override
	public Map<K, Collection<V>> asMap() {
		// TODO Auto-generated method stub
		return null;
	}

	private class ListMultimapValueListView extends AbstractList<V> {

		private K key;
		
		public ListMultimapValueListView(K key) {
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
	
	private class ListMultimapEntry implements Entry<K, V> {
		
		private K key;
		private int index;
		
		public ListMultimapEntry(K key, int index) {
			this.key = key;
			this.index = index;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return map.get(key).get(index);
		}

		@Override
		public V setValue(V value) {
			V old = map.get(key).get(index);
			map.get(key).set(index, value);
			return old;
		}
		
	}
	
	private class ListMultimapEntryCollection extends AbstractCollection<Entry<K, V>> {

		@Override
		public int size() {
			return count;
		}
		
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new ListMultimapEntrySetIterator();
		}

		private class ListMultimapEntrySetIterator implements Iterator<Entry<K, V>> {

			private Iterator<Entry<K, ArrayList<V>>> entryIterator;
			private K currentKey;
			private ListMultimapEntry last;
			private int currentCount = 0;
			private int i = 0;
			
			public ListMultimapEntrySetIterator() {
				entryIterator = map.entrySet().iterator();
			}
			
			@Override
			public boolean hasNext() {
				return i < currentCount || entryIterator.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				if (i < currentCount) {
					last = new ListMultimapEntry(currentKey, i++);
					return last;
				}
				if (entryIterator.hasNext()) {
					Entry<K, ArrayList<V>> entry = entryIterator.next();
					currentKey = entry.getKey();
					currentCount = entry.getValue().size();
					i = 1;
					last = new ListMultimapEntry(currentKey, 0);
					return last;
				}
				throw new NoSuchElementException();
			}
			
			@Override
			public void remove() {
				if (last == null) throw new IllegalStateException();
				ArrayList<V> list = map.get(last.getKey());
				if (last.index == list.size() - 1) {
					list.remove(last.index);
				} else {
					list.set(last.index, list.get(list.size() - 1));
					list.remove(list.size() - 1);
				}
				if (list.size() == 0) {
					map.remove(last.getKey());
				}
				i--;
				currentCount--;
				last = null;
			}
		}
		
	}
	
	private class ListMultisetKeySet extends SetView<K> {
		
		public ListMultisetKeySet() {
			super(map.keySet());
		}
		
		@Override
		protected void addBehavior(K element) {
			throw new UnsupportedOperationException("Cannot add element to Multiset's elementSet view!");
		}
		@Override
		protected void removeBehavior(K element) {
			count -= map.get(element).size();
		}
		
	}

}
