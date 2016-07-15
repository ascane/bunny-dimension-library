package bunny.structure;

import java.util.Iterator;

public abstract class AbstractIterable<T> implements Iterable<T> {
	
	protected abstract T nextValue(T last);
	
	@Override
	public Iterator<T> iterator() {
		return new AbstractIterator<T>() {
			@Override
			protected T nextValue(T last) {return nextValue(last);}
		};
	}
}
