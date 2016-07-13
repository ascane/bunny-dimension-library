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

	@Override
	public int size() {
		return original.size();
	}
	
	@Override
	public boolean add(T element) {
		if (readOnly) throw new UnsupportedOperationException("This view is read-only!");
		return original.add(element);
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
		public ViewIterator(Iterator<T> original) {
			this.original = original;
		}
		@Override
		public boolean hasNext() {return original.hasNext();}
		@Override
		public T next() {return original.next();}
		@Override
		public void remove() {
			if (readOnly) throw new UnsupportedOperationException("This view is read-only!");
			original.remove();
		}
	}

}
