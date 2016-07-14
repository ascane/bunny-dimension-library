package bunny.wrap;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

public class ListWrapper<E, T> extends AbstractList<T> {

	private List<E> original;
	private Function<E, T> map;
	private Function<T, E> mapBack;
	private boolean readOnly = false;
	
	public ListWrapper(List<E> original, Function<E, T> mapFunction) {
		this.original = original;
		map = mapFunction;
	}
	public ListWrapper(List<E> original, Function<E, T> mapFunction, Function<T, E> mapBackFunction) {
		this.original = original;
		map = mapFunction;
		mapBack = mapBackFunction;
	}

	@Override
	public int size() {
		return original.size();
	}
	
	@Override
	public T get(int index) {
		return map.apply(original.get(index));
	}
	
	@Override
	public T set(int index, T element) {
		if (readOnly) throw new UnsupportedOperationException("Wrapper is read-only!");
		if (mapBack == null) throw new UnsupportedOperationException("mapBack Function is not defined!");
		T old = map.apply(original.get(index));
		original.set(index, mapBack.apply(element));
		return old;
	}
	
	@Override
	public void add(int index, T element) {
		if (readOnly) throw new UnsupportedOperationException("Wrapper is read-only!");
		if (mapBack == null) throw new UnsupportedOperationException("mapBack Function is not defined!");
		original.add(index, mapBack.apply(element));
	}
	
	@Override
	public T remove(int index) {
		if (readOnly) throw new UnsupportedOperationException("Wrapper is read-only!");
		return map.apply(original.remove(index));
	}
	
	public ListWrapper<E, T> readOnly() {
		readOnly = true;
		return this;
	}

}
