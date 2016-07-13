package bunny.wrap;

import java.util.AbstractList;

public class PrimitiveArrayView {

	private PrimitiveArrayView() {}
	
	public static class IntArrayView extends AbstractList<Integer> {

		private int[] original;
		
		public IntArrayView(int[] array) {
			original = array;
		}
		
		@Override
		public Integer get(int i) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i];
		}
		@Override
		public Integer set(int i, Integer value) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i] = value;
		}
		@Override
		public int size() {
			return original.length;
		}
		
	}


	public static class LongArrayView extends AbstractList<Long> {

		private long[] original;
		
		public LongArrayView(long[] array) {
			original = array;
		}
		
		@Override
		public Long get(int i) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i];
		}
		@Override
		public Long set(int i, Long value) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i] = value;
		}
		@Override
		public int size() {
			return original.length;
		}
		
	}


	public static class FloatArrayView extends AbstractList<Float> {

		private float[] original;
		
		public FloatArrayView(float[] array) {
			original = array;
		}
		
		@Override
		public Float get(int i) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i];
		}
		@Override
		public Float set(int i, Float value) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i] = value;
		}
		@Override
		public int size() {
			return original.length;
		}
		
	}


	public static class DoubleArrayView extends AbstractList<Double> {

		private double[] original;
		
		public DoubleArrayView(double[] array) {
			original = array;
		}
		
		@Override
		public Double get(int i) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i];
		}
		@Override
		public Double set(int i, Double value) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i] = value;
		}
		@Override
		public int size() {
			return original.length;
		}
		
	}


	public static class BooleanArrayView extends AbstractList<Boolean> {

		private boolean[] original;
		
		public BooleanArrayView(boolean[] array) {
			original = array;
		}
		
		@Override
		public Boolean get(int i) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i];
		}
		@Override
		public Boolean set(int i, Boolean value) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i] = value;
		}
		@Override
		public int size() {
			return original.length;
		}
		
	}


	public static class CharArrayView extends AbstractList<Character> {

		private char[] original;
		
		public CharArrayView(char[] array) {
			original = array;
		}
		
		@Override
		public Character get(int i) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i];
		}
		@Override
		public Character set(int i, Character value) {
			if (i < 0 || i >= original.length) throw new IndexOutOfBoundsException();
			return original[i] = value;
		}
		@Override
		public int size() {
			return original.length;
		}
		
	}
	
}
