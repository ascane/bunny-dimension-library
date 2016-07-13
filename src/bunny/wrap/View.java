package bunny.wrap;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class View<T> extends AbstractCollection<T> {

	private Collection<T> original;
	private boolean readOnly;
	
	public static <T> View<T> of(Collection<T> original) {
		return new View<T>(original);
	}
	
	public View(Collection<T> original) {
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
	
	public View<T> readOnly() {
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
