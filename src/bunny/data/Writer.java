package bunny.data;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

public class Writer extends PrintWriter {
	
	public String delimiter = " ";
	
	public Writer() {
		super(System.out);
	}
	public Writer(String path) throws FileNotFoundException {
		super(path);
	}
	
	public void print(String[] array) {
		if (array == null || array.length == 0) return;
		print(array[0]);
		for (int i = 1; i < array.length; i++) {
			print(delimiter);
			print(array[i]);
		}
	}
	public void print(int[] array) {
		if (array == null || array.length == 0) return;
		print(array[0]);
		for (int i = 1; i < array.length; i++) {
			print(delimiter);
			print(array[i]);
		}
	}
	public void print(long[] array) {
		if (array == null || array.length == 0) return;
		print(array[0]);
		for (int i = 1; i < array.length; i++) {
			print(delimiter);
			print(array[i]);
		}
	}
	public void print(float[] array) {
		if (array == null || array.length == 0) return;
		print(array[0]);
		for (int i = 1; i < array.length; i++) {
			print(delimiter);
			print(array[i]);
		}
	}
	public void print(double[] array) {
		if (array == null || array.length == 0) return;
		print(array[0]);
		for (int i = 1; i < array.length; i++) {
			print(delimiter);
			print(array[i]);
		}
	}
	public void print(boolean[] array) {
		if (array == null || array.length == 0) return;
		print(array[0]);
		for (int i = 1; i < array.length; i++) {
			print(delimiter);
			print(array[i]);
		}
	}
	public void print(Object... array) {
		if (array == null || array.length == 0) return;
		print(array[0]);
		for (int i = 1; i < array.length; i++) {
			print(delimiter);
			print(array[i]);
		}
	}
	public <T> void print(Iterable<T> iterable) {
		print(iterable.iterator());
	}
	public <T> void print(Iterator<T> iterator) {
		boolean first = true;
		for (;iterator.hasNext();) {
			if (first)
				first = false;
			else
				print(delimiter);
			print(iterator.next());
		}
	}
	public void println(String[] array) {
		print(array);
		println();
	}
	public void println(int[] array) {
		print(array);
		println();
	}
	public void println(long[] array) {
		print(array);
		println();
	}
	public void println(float[] array) {
		print(array);
		println();
	}
	public void println(double[] array) {
		print(array);
		println();
	}
	public void println(boolean[] array) {
		print(array);
		println();
	}
	public void println(Object... array) {
		print(array);
		println();
	}
	public <T> void println(Iterable<T> iterable) {
		print(iterable);
		println();
	}
	public <T> void println(Iterator<T> iterator) {
		print(iterator);
		println();
	}
}
