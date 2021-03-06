package bunny.wrap;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class SetWrapper<T, E> extends AbstractSet<T> {

	private Set<E> original;
	private Function<E, T> map;
	private Function<T, E> mapBack;
	private boolean readOnly = false;
	
	public SetWrapper(Set<E> original, Function<E, T> mapFunction) {
		this.original = original;
		map = mapFunction;
	}
	public SetWrapper(Set<E> original, Function<E, T> mapFunction, Function<T, E> mapBackFunction) {
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
	
	public SetWrapper<T, E> readOnly() {
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
