package bunny.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import bunny.wrap.ListView;
import bunny.wrap.PrimitiveArrayView;

// A class that simplifies creating/transforming/copying sequential data(Array, List, Iterable).
// General rule: use primitive array or generic collection, not generic array.
public class Data {

	public static int[] filled(int value, int length) {
		int[] result = new int[length];
		Arrays.fill(result, value);
		return result;
	}
	public static long[] filled(long value, int length) {
		long[] result = new long[length];
		Arrays.fill(result, value);
		return result;
	}
	public static float[] filled(float value, int length) {
		float[] result = new float[length];
		Arrays.fill(result, value);
		return result;
	}
	public static double[] filled(double value, int length) {
		double[] result = new double[length];
		Arrays.fill(result, value);
		return result;
	}
	public static boolean[] filled(boolean value, int length) {
		boolean[] result = new boolean[length];
		Arrays.fill(result, value);
		return result;
	}
	public static char[] filled(char value, int length) {
		char[] result = new char[length];
		Arrays.fill(result, value);
		return result;
	}
	public static <T> T[] filled(T value, T[] array) {
		Arrays.fill(array, value);
		return array;
	}
	
	public static <T> ArrayList<T> filledList(T value, int length) {
		ArrayList<T> result = new ArrayList<T>(length);
		for (int i = 0; i < length; i++) {
			result.add(value);
		}
		return result;
	}
	
	public static int[] range(int n) {
		return range(0, n, 1);
	}
	public static int[] range(int st, int ed) {
		return range(st, ed, 1);
	}
	public static int[] range(int st, int ed, int step) {
		if (step == 0) throw new IllegalArgumentException();
		int[] result = new int[(ed - st - 1) / step + 1];
		int index = 0;
		if (step > 0) {
			for (int i = st; i < ed; i += step) {
				result[index++] = i;
			}
		} else {
			for (int i = st; i > ed; i += step) {
				result[index++] = i;
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> rangeList(int n) {
		return rangeList(0, n, 1);
	}
	public static ArrayList<Integer> rangeList(int st, int ed) {
		return rangeList(st, ed, 1);
	}
	public static ArrayList<Integer> rangeList(int st, int ed, int step) {
		if (step == 0) throw new IllegalArgumentException();
		ArrayList<Integer> result = new ArrayList<>();
		if (step > 0) {
			for (int i = st; i < ed; i += step) {
				result.add(i);
			}
		} else {
			for (int i = st; i > ed; i += step) {
				result.add(i);
			}
		}
		return result;
	}
	
	@SafeVarargs
	public static <T> List<T> asList(T... array) {
		return Arrays.asList(array);
	}
	public static List<Integer> asList(int[] array) {
		return new PrimitiveArrayView.IntArrayView(array);
	}
	public static List<Long> asList(long[] array) {
		return new PrimitiveArrayView.LongArrayView(array);
	}
	public static List<Float> asList(float[] array) {
		return new PrimitiveArrayView.FloatArrayView(array);
	}
	public static List<Double> asList(double[] array) {
		return new PrimitiveArrayView.DoubleArrayView(array);
	}
	public static List<Boolean> asList(boolean[] array) {
		return new PrimitiveArrayView.BooleanArrayView(array);
	}
	public static List<Character> asList(char[] array) {
		return new PrimitiveArrayView.CharArrayView(array);
	}
	
	@SafeVarargs
	public static <T> ArrayList<T> toArrayList(T... elements) {
		return new ArrayList<T>(Arrays.asList(elements));
	}
	public static <T> ArrayList<T> toArrayList(Iterable<T> it) {
		return toArrayList(it.iterator());
	}
	public static <T> ArrayList<T> toArrayList(Iterator<T> it) {
		ArrayList<T> result = new ArrayList<T>();
		for (;it.hasNext();) {
			result.add(it.next());
		}
		return result;
	}
	public static ArrayList<Integer> toArrayList(int[] array) {
		return new ArrayList<>(new PrimitiveArrayView.IntArrayView(array));
	}
	public static ArrayList<Long> toArrayList(long[] array) {
		return new ArrayList<>(new PrimitiveArrayView.LongArrayView(array));
	}
	public static ArrayList<Float> toArrayList(float[] array) {
		return new ArrayList<>(new PrimitiveArrayView.FloatArrayView(array));
	}
	public static ArrayList<Double> toArrayList(double[] array) {
		return new ArrayList<>(new PrimitiveArrayView.DoubleArrayView(array));
	}
	public static ArrayList<Boolean> toArrayList(boolean[] array) {
		return new ArrayList<>(new PrimitiveArrayView.BooleanArrayView(array));
	}
	public static ArrayList<Character> toArrayList(char[] array) {
		return new ArrayList<>(new PrimitiveArrayView.CharArrayView(array));
	}
	
	public static int[] toPrimitiveIntArray(Iterable<Integer> it) {
		Collection<Integer> c = it instanceof Collection<?> ? (Collection<Integer>) it : toArrayList(it);
		int[] result = new int[c.size()];
		int i = 0;
		for (Integer value : c) {
			result[i++] = value;
		}
		return result;
	}
	public static int[] toPrimitiveIntArray(Integer[] array) {
		return toPrimitiveIntArray(asList(array));
	}
	public static long[] toPrimitiveLongArray(Iterable<Long> it) {
		Collection<Long> c = it instanceof Collection<?> ? (Collection<Long>) it : toArrayList(it);
		long[] result = new long[c.size()];
		int i = 0;
		for (Long value : c) {
			result[i++] = value;
		}
		return result;
	}
	public static long[] toPrimitiveLongArray(Long[] array) {
		return toPrimitiveLongArray(asList(array));
	}
	public static float[] toPrimitiveFloatArray(Iterable<Float> it) {
		Collection<Float> c = it instanceof Collection<?> ? (Collection<Float>) it : toArrayList(it);
		float[] result = new float[c.size()];
		int i = 0;
		for (Float value : c) {
			result[i++] = value;
		}
		return result;
	}
	public static float[] toPrimitiveFloatArray(Float[] array) {
		return toPrimitiveFloatArray(asList(array));
	}
	public static double[] toPrimitiveDoubleArray(Iterable<Double> it) {
		Collection<Double> c = it instanceof Collection<?> ? (Collection<Double>) it : toArrayList(it);
		double[] result = new double[c.size()];
		int i = 0;
		for (Double value : c) {
			result[i++] = value;
		}
		return result;
	}
	public static double[] toPrimitiveDoubleArray(Double[] array) {
		return toPrimitiveDoubleArray(asList(array));
	}
	public static boolean[] toPrimitiveBooleanArray(Iterable<Boolean> it) {
		Collection<Boolean> c = it instanceof Collection<?> ? (Collection<Boolean>) it : toArrayList(it);
		boolean[] result = new boolean[c.size()];
		int i = 0;
		for (Boolean value : c) {
			result[i++] = value;
		}
		return result;
	}
	public static boolean[] toPrimitiveBooleanArray(Boolean[] array) {
		return toPrimitiveBooleanArray(asList(array));
	}
	public static char[] toPrimitiveCharArray(Iterable<Character> it) {
		Collection<Character> c = it instanceof Collection<?> ? (Collection<Character>) it : toArrayList(it);
		char[] result = new char[c.size()];
		int i = 0;
		for (Character value : c) {
			result[i++] = value;
		}
		return result;
	}
	public static char[] toPrimitiveCharArray(Character[] array) {
		return toPrimitiveCharArray(asList(array));
	}
	
	public static <T> void copy(List<? extends T> src, List<? super T> dest) {
		Collections.copy(dest, src);
	}
	public static <T> void copy(List<? extends T> src, int srcPos, List<? super T> dest, int destPos, int count) {
		ListView<? extends T> srcView = ListView.of(src).range(srcPos, srcPos + count);
		ListView<? super T> destView = ListView.of(dest).range(destPos, destPos + count);
		Collections.copy(destView, srcView);
	}
	public static <T> void copy(T[] srcArray, T[] destArray) {
		System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
	}
	public static <T> void copy(T[] srcArray, int srcPos, T[] destArray, int destPos, int count) {
		System.arraycopy(srcArray, srcPos, destArray, destPos, count);
	}
	public static void copy(int[] srcArray, int[] destArray) {
		System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
	}
	public static void copy(int[] srcArray, int srcPos, int[] destArray, int destPos, int count) {
		System.arraycopy(srcArray, srcPos, destArray, destPos, count);
	}
	public static void copy(long[] srcArray, long[] destArray) {
		System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
	}
	public static void copy(long[] srcArray, int srcPos, long[] destArray, int destPos, int count) {
		System.arraycopy(srcArray, srcPos, destArray, destPos, count);
	}
	public static void copy(float[] srcArray, float[] destArray) {
		System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
	}
	public static void copy(float[] srcArray, int srcPos, float[] destArray, int destPos, int count) {
		System.arraycopy(srcArray, srcPos, destArray, destPos, count);
	}
	public static void copy(double[] srcArray, double[] destArray) {
		System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
	}
	public static void copy(double[] srcArray, int srcPos, double[] destArray, int destPos, int count) {
		System.arraycopy(srcArray, srcPos, destArray, destPos, count);
	}
	public static void copy(boolean[] srcArray, boolean[] destArray) {
		System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
	}
	public static void copy(boolean[] srcArray, int srcPos, boolean[] destArray, int destPos, int count) {
		System.arraycopy(srcArray, srcPos, destArray, destPos, count);
	}
	public static void copy(char[] srcArray, char[] destArray) {
		System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
	}
	public static void copy(char[] srcArray, int srcPos, char[] destArray, int destPos, int count) {
		System.arraycopy(srcArray, srcPos, destArray, destPos, count);
	}
	
	public static Object deepCloneArray(Object multidimensionalArray) {
		if (multidimensionalArray == null) return null;
		Class<?> c = multidimensionalArray.getClass();
		if (!c.isArray()) throw new IllegalArgumentException();
		
		int length = Array.getLength(multidimensionalArray);
		Object result = Array.newInstance(multidimensionalArray.getClass().getComponentType(), length);
		if (!c.getComponentType().isArray()) {
			System.arraycopy(multidimensionalArray, 0, result, 0, length);
		} else {
			for (int i = 0; i < length; i++) {
				Array.set(result, i, deepCloneArray(Array.get(multidimensionalArray, i)));
			}
		}
		return result;
	}
	
	public static Object deepCloneList(Object multidimensionalList) {
		if (multidimensionalList == null) return null;
		if (!(multidimensionalList instanceof List<?>)) throw new IllegalArgumentException();
		
		List<?> list = (List<?>)multidimensionalList;
		int length = list.size();
		ArrayList<Object> result;
		if (list.size() == 0 || !(list.get(0) instanceof List<?>)) {
			result = new ArrayList<>(list);
		} else {
			result = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				result.add(deepCloneList(list.get(i)));
			}
		}
		return result;
	}
	
	public static <T> Iterable<T> iteratorToIterable(final Iterator<T> it) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {return it;}
		};
	}
	
}
