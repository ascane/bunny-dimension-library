package bunny.structure;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.function.Function;

import bunny.wrap.Wrapper;

import java.util.Set;

// You need to implement: size(), put(), asMap() (make sure it's efficient)
public abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return asMap().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		for (List<V> list : asMap().values()) {
			if (list.contains(value)) return true;
		}
		return false;
	}

	@Override
	public boolean containsEntry(Object key, Object value) {
		List<V> list = asMap().get(key);
		if (list == null) return false;
		return list.contains(value);
	}
	
	@Override
	public boolean putAll(K key, Iterable<? extends V> values) {
		boolean changed = false;
		for (V value : values) {
			if (put(key, value)) {
				changed = true;
			}
		}
		return changed;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object key, Object value) {
		return get((K)key).remove(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<V> removeAll(Object key) {
		List<V> values = get((K)key);
		List<V> oldList = new ArrayList<V>(values);
		values.clear();
		return oldList;
	}
	
	@Override
	public List<V> replaceValues(K key, Iterable<? extends V> values) {
		Iterator<? extends V> it = values.iterator();
		List<V> oldList = removeAll(key);
		
		List<V> newList = get(key);
		while (it.hasNext()) {
			newList.add(it.next());
		}
		return oldList;
	}

	@Override
	public void clear() {
		asMap().clear();
	}

	@Override
	public List<V> get(K key) {
		return new MultimapValueView(key);
	}

	@Override
	public Set<K> keySet() {
		return asMap().keySet();
	}

	@Override
	public Multiset<K> keys() {
		return new MultimapKeyMultiset();
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
		return new MultimapEntryCollection();
	}
	
	private class MultimapValueView extends AbstractList<V> {

		private K key;
		Map<K, List<V>> map;
		
		public MultimapValueView(K key) {
			this.key = key;
			map = asMap();
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
			return list.remove(index);
		}
	}
	
	private class MultimapEntry implements Entry<K, V> {
		
		private K key;
		private List<V> list;
		private int index;
		
		public MultimapEntry(K key, int index) {
			this.key = key;
			this.list = get(key);
			this.index = index;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return list.get(index);
		}

		@Override
		public V setValue(V value) {
			V old = list.get(index);
			list.set(index, value);
			return old;
		}
	}
	
	private class MultimapEntryCollection extends AbstractCollection<Entry<K, V>> {

		@Override
		public int size() {
			return AbstractMultimap.this.size();
		}
		
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new MultimapEntryCollectionIterator();
		}

		private class MultimapEntryCollectionIterator implements Iterator<Entry<K, V>> {

			private Iterator<Entry<K, List<V>>> entryIterator;
			private K currentKey;
			private List<V> currentList;
			private boolean canDelete;
			private int currentCount = 0;
			private int i = 0;
			
			public MultimapEntryCollectionIterator() {
				entryIterator = asMap().entrySet().iterator();
				canDelete = false;
			}
			
			@Override
			public boolean hasNext() {
				return i < currentCount || entryIterator.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				if (i < currentCount) {
					canDelete = true;
					return new MultimapEntry(currentKey, i++);
				}
				if (entryIterator.hasNext()) {
					Entry<K, List<V>> entry = entryIterator.next();
					currentKey = entry.getKey();
					currentList = entry.getValue();
					currentCount = entry.getValue().size();
					i = 1;
					canDelete = true;
					return new MultimapEntry(currentKey, 0);
				}
				throw new NoSuchElementException();
			}
			
			@Override
			public void remove() {
				if (!canDelete) throw new IllegalStateException();
				int index = i - 1;
				if (index == currentList.size() - 1) {
					currentList.remove(index);
				} else {
					currentList.set(index, currentList.get(currentList.size() - 1));
					currentList.remove(currentList.size() - 1);
				}
				i--;
				currentCount--;
				canDelete = false;
			}
		}
	}
	
	private class MultimapKeyMultiset extends AbstractMultiset<K> {

		@Override
		public int size() {
			return AbstractMultimap.this.size();
		}
		
		@Override
		public int count(K element) {
			return get(element).size();
		}

		@Override
		public int setCount(K element, int count) {
			throw new UnsupportedOperationException("Multiset's keys do not support setCount.");
		}

		@Override
		public Set<K> elementSet() {
			return asMap().keySet();
		}

		@Override
		public void clear() {
			asMap().clear();
		}
	}
}
