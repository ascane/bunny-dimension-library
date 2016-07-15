package bunny.structure;

import java.util.Set;

public interface Multiset<T> extends Set<T> {
	
	public int add(T element, int occurrences);
	
	public int remove(T element, int occurrences);
	
	public int count(T element);
	
	public int setCount(T element, int count);
	
	public Set<Entry<T>> entrySet();
	
	public Set<T> elementSet();
	
	public static interface Entry<T> {
		public T getElement();
		public int getCount();
	}
}
