package bunny.structure;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

import bunny.wrap.SetView;
import bunny.wrap.SetWrapper;

public class HashMultiset<T> extends AbstractSet<T> implements Multiset<T>{

	private int count = 0;
	private HashMap<T, Integer> map;
	
	public HashMultiset() {
		map = new HashMap<T, Integer>();
	}
	public HashMultiset(Collection<? extends T> elements) {
		map = new HashMap<T, Integer>();
		addAll(elements);
	}
	
	@Override
	public int size() {
		return count;
	}
	@Override
	public int count(T element) {
		return map.getOrDefault(element, 0);
	}
	@Override
	public int setCount(T element, int count) {
		if (count < 0) throw new IllegalArgumentException("Element count cannot be negative!");
		int oldCount = map.getOrDefault(element, 0);
		if (count == 0) {
			map.remove(element);
		} else {
			map.put(element, count);
		}
		this.count += count - oldCount;
		return oldCount;
	}
	@Override
	public boolean contains(Object element) {
		return map.containsKey(element);
	}
	@Override
	public boolean add(T element) {
		map.put(element, map.getOrDefault(element, 0) + 1);
		count++;
		return true;
	}
	@Override
	public int add(T element, int occurrences) {
		if (occurrences < 0) throw new IllegalArgumentException("Occurrences cannot be negative!");
		int current = map.getOrDefault(element, 0);
		if (occurrences != 0) {
			map.put(element, current + occurrences);
			count += occurrences;
		}
		return current;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object element) {
		if (!map.containsKey(element)) return false;
		int current = map.get(element);
		if (current > 1) {
			map.put((T)element, current - 1);
		} else {
			map.remove(element);
		}
		count--;
		return true;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int remove(Object element, int occurrences) {
		if (occurrences < 0) throw new IllegalArgumentException("Occurrences cannot be negative!");
		if (!map.containsKey(element)) return 0;
		int current = map.get(element);
		if (current > occurrences) {
			count -= occurrences;
			map.put((T)element, current - occurrences);
		} else {
			count -= current;
			map.remove(element);
		}
		return current;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		if (c == null) return false;
		boolean changed = false;
		for (Object o : c) {
			changed = changed || remove(o);
		}
		return changed;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> c) {
		if (c == null) return false;
		boolean changed = false;
		for (Object o : c) {
			int count = map.getOrDefault(o, 0);
			if (count > 0) {
				map.put((T)o, -count);
			}
		}
		for (Iterator<Map.Entry<T, Integer>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<T, Integer> entry = it.next();
			int value = entry.getValue();
			if (value > 0) {
				it.remove();
				count -= value;
				changed = true;
			} else {
				entry.setValue( - value);
			}
		}
		return changed;
	}
	@Override
	public void clear() {
		map.clear();
		count = 0;
	}
	@Override
	public Set<Entry<T>> entrySet() {
		return new SetWrapper<Entry<T>, Map.Entry<T, Integer>>(map.entrySet(), new Function<Map.Entry<T, Integer>, Entry<T>>() {
			@Override
			public Entry<T> apply(Map.Entry<T, Integer> t) {return new HashMultisetEntry(t);}
		}).readOnly();
	}
	@Override
	public Set<T> elementSet() {
		return SetView.of(map.keySet()).readOnly();
	}
	@Override
	public Iterator<T> iterator() {
		return new HashMultiSetIterator();
	}
	
	
	private class HashMultiSetIterator implements Iterator<T> {

		private Iterator<Entry<T>> entryIterator;
		private T current;
		private T last;
		private int currentCount = 0;
		private int i = 0;
		
		public HashMultiSetIterator() {
			entryIterator = entrySet().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return i < currentCount || entryIterator.hasNext();
		}

		@Override
		public T next() {
			if (i < currentCount) {
				i++;
				return current;
			}
			if (entryIterator.hasNext()) {
				Entry<T> entry = entryIterator.next();
				current = entry.getElement();
				last = current;
				currentCount = entry.getCount();
				i = 1;
				return current;
			}
			throw new NoSuchElementException();
		}
		
		@Override
		public void remove() {
			if (last == null) throw new IllegalStateException();
			HashMultiset.this.remove(last);
			last = null;
		}
	}
	
	private class HashMultisetEntry implements Entry<T> {
		
		private Map.Entry<T, Integer> mapEntry;
		
		public HashMultisetEntry(Map.Entry<T, Integer> mapEntry) {
			this.mapEntry = mapEntry;
		}
		
		@Override
		public T getElement() {return mapEntry.getKey();}
		@Override
		public int getCount() {return mapEntry.getValue();}
		
	}
	
}
