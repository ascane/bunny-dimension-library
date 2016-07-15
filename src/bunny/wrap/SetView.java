package bunny.wrap;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class SetView<T> extends AbstractSet<T> {

	private Set<T> original;
	private boolean readOnly;
	
	public static <T> SetView<T> of(Set<T> original) {
		return new SetView<T>(original);
	}
	
	public SetView(Set<T> original) {
		this.original = original;
	}
	
	protected void addBehavior(T element) {
		// To be overridden.
	}
	
	protected void removeBehavior(T element) {
		// To be overridden.
	}

	@Override
	public int size() {
		return original.size();
	}
	
	@Override
	public boolean contains(Object o) {
		return original.contains(o);
	}
	
	@Override
	public void clear() {
		if (readOnly) throw new UnsupportedOperationException("This view is read-only!");
		original.clear();
	}
	
	@Override
	public boolean add(T element) {
		if (readOnly) throw new UnsupportedOperationException("This view is read-only!");
		addBehavior(element);
		return original.add(element);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object element) {
		if (readOnly) throw new UnsupportedOperationException("This view is read-only!");
		removeBehavior((T)element);
		return original.remove(element);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new ViewIterator(original.iterator());
	}
	
	public SetView<T> readOnly() {
		readOnly = true;
		return this;
	}
	
	public class ViewIterator implements Iterator<T> {
		public Iterator<T> original;
		private T last;
		public ViewIterator(Iterator<T> original) {
			this.original = original;
		}
		@Override
		public boolean hasNext() {return original.hasNext();}
		@Override
		public T next() {return last = original.next();}
		@Override
		public void remove() {
			if (readOnly) throw new UnsupportedOperationException("This view is read-only!");
			removeBehavior(last);
			original.remove();
		}
	}

}
