package bunny.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class SimpleIterable<T> implements Iterable<T> {
	
	protected abstract T nextValue(T last);
	
	@Override
	public Iterator<T> iterator() {
		return new SimpleIterator();
	}
	
	public class SimpleIterator implements Iterator<T> {
		
		private T current = null;
		private boolean nextReady = false;

		@Override
		public boolean hasNext() {
			if (!nextReady) {
				current = nextValue(current);
				nextReady = true;
			}
			return current != null;
		}

		@Override
		public T next() {
			if (hasNext()) {
				nextReady = false;
				return current;
			} else {
				throw new NoSuchElementException();
			}
		}
		
	}

}
