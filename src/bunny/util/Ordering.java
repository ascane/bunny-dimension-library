package bunny.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;

public abstract class Ordering<T> implements Comparator<T> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Ordering<Comparable> NATURAL_ORDERING = new Ordering<Comparable>() {
		@Override
		public int compare(Comparable o1, Comparable o2) {return o1.compareTo(o2);}
	};
	
	private static final Ordering<Object> ARBITRARY_ORDERING = new Ordering<Object>() {
		private int counter = 0;
		private HashMap<Object, Integer> ids = new HashMap<Object, Integer>();
		
		private Integer getId(Object o) {
			if (ids.containsKey(o)) {
				return ids.get(o);
			} else {
				ids.put(o, counter);
				return counter++;
			}
		}
		
		@Override
		public int compare(Object o1, Object o2) {
			if (o1 == o2) {
				return 0;
			} else if (o1 == null) {
		    	return -1;
			} else if (o2 == null) {
				return 1;
			}
			int code1 = System.identityHashCode(o1);
			int code2 = System.identityHashCode(o2);
			if (code1 != code2) {
				return code1 < code2 ? -1 : 1;
			}
			return getId(o1).compareTo(getId(o2));
		}
	};
	
	private static final Ordering<Object> TO_STRING_ORDERING = new Ordering<Object>() {
		@Override
		public int compare(Object o1, Object o2) {return o1.toString().compareTo(o2.toString());}
	};
	
	
	
	public static <T> Ordering<T> from(final Comparator<T> c) {
		return new Ordering<T>() {
			@Override
			public int compare(T o1, T o2) {return c.compare(o1, o2);}
		};
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Comparable> Ordering<T> natural() {
		return (Ordering<T>)NATURAL_ORDERING;
	}
	
	public static Ordering<Object> arbitrary() {
		return ARBITRARY_ORDERING;
	}
	
	public static Ordering<Object> usingToString() {
		return TO_STRING_ORDERING;
	}
	

	public <S extends T> Ordering<S> reverse() {
		return new Ordering<S>() {
			@Override
			public int compare(S o1, S o2) {return Ordering.this.compare(o2, o1);}
		};
	}
	
	public <F> Ordering<F> onResultOf(final Function<F,? extends T> function) {
		return new Ordering<F>() {
			@Override
			public int compare(F o1, F o2) {return Ordering.this.compare(function.apply(o1), function.apply(o2));}
		};
	}
	
	public <S extends T> Ordering<Iterable<S>> lexigraphical() {
		return new Ordering<Iterable<S>>() {
			@Override
			public int compare(Iterable<S> i1, Iterable<S> i2) {
				Iterator<S> it1 = i1.iterator();
				Iterator<S> it2 = i2.iterator();
				while (it1.hasNext() || it2.hasNext()) {
					if (!it1.hasNext()) return -1;
					if (!it2.hasNext()) return 1;
					int res = Ordering.this.compare(it1.next(), it2.next());
					if (res != 0) return res;
				}
				return 0;
			}
		};
	}
	
	public <S extends T> Ordering<S[]> arrayLexigraphical() {
		return new Ordering<S[]>() {
			@Override
			public int compare(S[] a1, S[] a2) {
				int n = Math.min(a1.length, a2.length);
				for (int i = 0; i < n; i++) {
					int res = Ordering.this.compare(a1[i], a2[i]);
					if (res != 0) return res;
				}
				if (a1.length - a2.length > 0) return 1;
				if (a1.length - a2.length < 0) return -1;
				return 0;
			}
		};
	}
	
	public <S extends T> Ordering<S> compound(final Comparator<? super S> secondaryComparator) {
		return new Ordering<S>() {
			@Override
			public int compare(S o1, S o2) {
				int result = Ordering.this.compare(o1, o2);
				if (result != 0) return result;
				return secondaryComparator.compare(o1, o2);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	public T min(T... n) {
		T result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = compare(n[i], result) < 0 ? n[i] : result;
		}
		return result;
	}
	public T min(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		for (;itor.hasNext();) {
			T next = itor.next();
			result = compare(next, result) < 0 ? next : result;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public int argmin(T... n) {
		T result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (compare(n[i], result) < 0) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public int argmin(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		int index = 0;
		for (int i = 0; itor.hasNext();) {
			T next = itor.next();
			i++;
			if (compare(next, result) < 0) {
				result = next;
				index = i;
			}
		}
		return index;
	}
	
	@SuppressWarnings("unchecked")
	public T max(T... n) {
		T result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = compare(n[i], result) > 0 ? n[i] : result;
		}
		return result;
	}
	public T max(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		for (;itor.hasNext();) {
			T next = itor.next();
			result = compare(next, result) > 0 ? next : result;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public int argmax(T... n) {
		T result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (compare(n[i], result) > 0) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public int argmax(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		int index = 0;
		for (int i = 0; itor.hasNext();) {
			T next = itor.next();
			i++;
			if (compare(next, result) > 0) {
				result = next;
				index = i;
			}
		}
		return index;
	}
	
}
