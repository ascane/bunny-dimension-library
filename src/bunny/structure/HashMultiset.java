package bunny.structure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import bunny.wrap.SetView;

public class HashMultiset<T> extends AbstractMultiset<T> {

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
	public void clear() {
		map.clear();
		count = 0;
	}

	@Override
	public Set<T> elementSet() {
		return new HashMultisetElementSet();
	}
	
	private class HashMultisetElementSet extends SetView<T> {
		
		public HashMultisetElementSet() {
			super(map.keySet());
		}
		
		@Override
		protected void addBehavior(T element) {
			throw new UnsupportedOperationException("Cannot add element to Multiset's view!");
		}
		@Override
		protected void removeBehavior(T element) {
			count -= map.get(element);
		}
	}
}
