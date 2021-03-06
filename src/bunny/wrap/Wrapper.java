package bunny.wrap;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class Wrapper<T, E> extends AbstractCollection<T> {

	private Collection<E> original;
	private Function<E, T> map;
	private Function<T, E> mapBack;
	private boolean readOnly = false;
	
	public Wrapper(Collection<E> original, Function<E, T> mapFunction) {
		this.original = original;
		map = mapFunction;
	}
	public Wrapper(Collection<E> original, Function<E, T> mapFunction, Function<T, E> mapBackFunction) {
		this.original = original;
		map = mapFunction;
		mapBack = mapBackFunction;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new WrapperIterator(original.iterator());
	}

	@Override
	public int size() {
		return original.size();
	}
	
	@Override
	public boolean add(T element) {
		if (readOnly) throw new UnsupportedOperationException("Wrapper is read-only!");
		if (mapBack == null) throw new UnsupportedOperationException("mapBack Function is not defined!");
		return original.add(mapBack.apply(element));
	}
	
	public Wrapper<T, E> readOnly() {
		readOnly = true;
		return this;
	}
	
	
	public class WrapperIterator implements Iterator<T> {
		public Iterator<E> original;
		public WrapperIterator(Iterator<E> original) {
			this.original = original;
		}
		@Override
		public boolean hasNext() {return original.hasNext();}
		@Override
		public T next() {return map.apply(original.next());}
		@Override
		public void remove() {
			if (readOnly) throw new UnsupportedOperationException("Wrapper is read-only!");
			original.remove();
		}
	}

}
