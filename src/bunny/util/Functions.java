package bunny.util;

import java.util.function.Function;

public class Functions {
	
	public static <T> Function<T, T> identity() {
		return new Function<T, T>() {
			@Override
			public T apply(T t) {return t;}
		};
	}
	
	public static <T, V> Function<T, V> constant(final V c) {
		return new Function<T, V>() {
			@Override
			public V apply(T t) {return c;}
		};
	}
	
	public static <A, B, C> Function<A,C> compose(final Function<B,C> g, final Function<A,? extends B> f) {
		return new Function<A, C>() {
			@Override
			public C apply(A t) {return g.apply(f.apply(t));}
		};
	}
	
	public static <T> Function<T, String> toStringFunction() {
		return new Function<T, String>() {
			@Override
			public String apply(T t) {return t.toString();}
		};
	}
	
	public static <A extends Number> Function<A, Integer> convertToInteger() {
		return new Function<A, Integer>() {
			@Override
			public Integer apply(A number) {return number.intValue();}
		};
	}
	
	public static <A extends Number> Function<A, Long> convertToLong() {
		return new Function<A, Long>() {
			@Override
			public Long apply(A number) {return number.longValue();}
		};
	}
	
	public static <A extends Number> Function<A, Float> convertToFloat() {
		return new Function<A, Float>() {
			@Override
			public Float apply(A number) {return number.floatValue();}
		};
	}
	
	public static <A extends Number> Function<A, Double> convertToDouble() {
		return new Function<A, Double>() {
			@Override
			public Double apply(A number) {return number.doubleValue();}
		};
	}
}
