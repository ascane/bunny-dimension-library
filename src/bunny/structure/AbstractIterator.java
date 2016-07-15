package bunny.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<T> implements Iterator<T> {

	private T current = null;
	private boolean nextReady = false;
	
	protected abstract T nextValue(T last);

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
