package bunny.wrap;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

// When using the view, please do not modify the size of the original list
public class ListView<T> extends AbstractList<T> {

	private List<T> original;
	private int startIndex;
	private int count;
	private int step;
	private int originalSize;
	private boolean isFull;
	private boolean readOnly = false;
	
	public static <T> ListView<T> of(List<T> list) {
		return new ListView<T>(list);
	}
	@SafeVarargs
	public static <T> ListView<T> of(T... array) {
		return new ListView<T>(Arrays.asList(array));
	}
	public static ListView<Integer> of(int[] array) {
		return new ListView<Integer>(new PrimitiveArrayView.IntArrayView(array));
	}
	public static ListView<Long> of(long[] array) {
		return new ListView<Long>(new PrimitiveArrayView.LongArrayView(array));
	}
	public static ListView<Float> of(float[] array) {
		return new ListView<Float>(new PrimitiveArrayView.FloatArrayView(array));
	}
	public static ListView<Double> of(double[] array) {
		return new ListView<Double>(new PrimitiveArrayView.DoubleArrayView(array));
	}
	public static ListView<Boolean> of(boolean[] array) {
		return new ListView<Boolean>(new PrimitiveArrayView.BooleanArrayView(array));
	}
	public static ListView<Character> of(char[] array) {
		return new ListView<Character>(new PrimitiveArrayView.CharArrayView(array));
	}
	
	public ListView(List<T> list) {
		original = list;
		originalSize = list.size();
		startIndex = 0;
		count = originalSize;
		step = 1;
		isFull = true;
	}
	public ListView(List<T> list, int startIndex, int count, int step) {
		original = list;
		originalSize = list.size();
		this.startIndex = startIndex;
		this.count = count;
		this.step = step;
		isFull = false;
	}
	
	private int index(int i) {
		int result = (startIndex + i * step) % originalSize;
		return result < 0 ? result + originalSize : result;
	}

	
	@Override
	public T get(int i) {
		if (i < 0 || i >= count) throw new IndexOutOfBoundsException();
		return original.get(index(i));
	}
	@Override
	public T set(int i, T value) {
		if (readOnly) throw new UnsupportedOperationException();
		if (i < 0 || i >= count) throw new IndexOutOfBoundsException();
		return original.set(index(i), value);
	}
	@Override
	public int size() {
		return count;
	}
	
	
	public ListView<T> reverse() {
		startIndex = (startIndex + (count - 1) * step) % originalSize;
		step = -step;
		return this;
	}
	public ListView<T> rotate(int shiftLeft) {
		if (isFull) {
			startIndex = (startIndex + shiftLeft * step) % originalSize;
			return this;
		} else {
			return new ListView<T>(this, shiftLeft, count, 1);
		}
	}
	public ListView<T> even() {
		return every(0, 2);
	}
	public ListView<T> odd() {
		return every(1, 2);
	}
	public ListView<T> every(int i, int divisor) {
		startIndex = (startIndex + i * step) % originalSize;
		step *= divisor;
		count = (count + divisor - 1 - i) / divisor;
		return this;
	}
	public ListView<T> range(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > count || fromIndex > toIndex) {
			throw new IndexOutOfBoundsException();
		}
		startIndex = (startIndex + fromIndex * step) % originalSize;
		int newCount = toIndex - fromIndex;
		if (newCount < count) {
			isFull = false;
			count = newCount;
		}
		return this;
	}
	public ListView<T> readOnly() {
		readOnly = true;
		return this;
	}
	
}



