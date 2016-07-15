package bunny.structure;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

import bunny.wrap.SetWrapper;

// The user needs to implement: size(), count(), setCount(), elementSet(), and clear() for performance.
public abstract class AbstractMultiset<T> extends AbstractSet<T> implements Multiset<T> {

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object element) {
		return count((T)element) > 0;
	}
	
	@Override
	public boolean add(T element) {
		int old = count(element);
		setCount(element, old + 1);
		return true;
	}
	
	@Override
	public int add(T element, int occurrences) {
		if (occurrences < 0) throw new IllegalArgumentException("Occurrences cannot be negative!");
		int old = count(element);
		setCount(element, old + occurrences);
		return old;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object element) {
		int old = count((T)element);
		if (old > 0) {
			setCount((T)element, old - 1);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int remove(Object element, int occurrences) {
		if (occurrences < 0) throw new IllegalArgumentException("Occurrences cannot be negative!");
		int old = count((T)element);
		if (old > 0) {
			setCount((T)element, Math.max(old - occurrences, 0));
		}
		return old;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> c) {
		if (c == null) return false;
		boolean changed = false;
		for (Object o : c) {
			if (setCount((T)o, 0) > 0) {
				changed = true;
			}
		}
		return changed;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> c) {
		if (c == null) return false;
		boolean changed = false;
		HashSet<T> cache = new HashSet<T>();
		for (Object o : c) {
			int count = count((T)o);
			if (count > 0) {
				cache.add((T)o);
			}
		}
		for (Iterator<Entry<T>> it = entrySet().iterator(); it.hasNext();) {
			Entry<T> entry = it.next();
			if (!cache.contains(entry.getElement())) {
				it.remove();
				changed = true;
			}
		}
		return changed;
	}
	
	@Override
	public void clear() {
		for (T element : elementSet()) {
			setCount(element, 0);
		}
	}

	@Override
	public Set<Entry<T>> entrySet() {
		return new SetWrapper<T, Entry<T>>(elementSet(), new Function<T, Entry<T>>() {
			@Override
			public Entry<T> apply(T t) {return new MultisetEntry(t);}
		});
	}

	@Override
	public Iterator<T> iterator() {
		return new MultisetIterator();
	}

	private class MultisetEntry implements Entry<T> {
		
		private T element;
		private int count;
		
		public MultisetEntry(T element) {
			this.element = element;
			count = count(element);
		}
		
		@Override
		public T getElement() {return element;}
		@Override
		public int getCount() {return count;}
		
	}
	
	private class MultisetIterator implements Iterator<T> {

		private Iterator<Entry<T>> entryIterator;
		private T current;
		private T last;
		private int currentCount = 0;
		private int i = 0;
		
		public MultisetIterator() {
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
			AbstractMultiset.this.remove(last);
			last = null;
		}
	}
	
}
