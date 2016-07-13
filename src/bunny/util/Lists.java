package bunny.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lists {
	
	public static <T> void swap(List<T> list, int index1, int index2) {
		if (index1 == index2) return;
		T temp = list.get(index1);
		list.set(index1, list.get(index2));
		list.set(index2, temp);
	}
	
	public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
		return Collections.binarySearch(list, key);
	}
	public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c) {
		return Collections.binarySearch(list, key, c);
	}
	
	public static <T extends Comparable<? super T>> int binarySearchSmallestGreaterThen(List<T> list, T key) {
		return binarySearchSmallestGreaterThen(list, key, Ordering.natural());
	}
	public static <T> int binarySearchSmallestGreaterThen(List<? extends T> list, T key, Comparator<? super T> c) {
		int n = list.size();
		if (c.compare(list.get(n - 1), key) < 0) return -1;
		int st = 0, ed = n - 1;
		while (ed - st > 0) {
			int mid = (st + ed) / 2;
			T element = list.get(mid);
			if (c.compare(element, key) >= 0) {
				ed = mid;
			} else {
				st = mid + 1;
			}
		}
		return st;
	}
	
	public static <T extends Comparable<? super T>> int binarySearchGreatestSmallerThen(List<T> list, T key) {
		return binarySearchGreatestSmallerThen(list, key, Ordering.natural());
	}
	public static <T> int binarySearchGreatestSmallerThen(List<? extends T> list, T key, Comparator<? super T> c) {
		int n = list.size();
		if (c.compare(list.get(0), key) > 0) return -1;
		int st = 0, ed = n - 1;
		while (ed - st > 0) {
			int mid = (st + ed + 1) / 2;
			T element = list.get(mid);
			if (c.compare(element, key) <= 0) {
				st = mid;
			} else {
				ed = mid - 1;
			}
		}
		return st;
	}
	
	public static <T extends Comparable<? super T>> T quickSelect(List<T> list, int k) {
		return quickSelect(list, 0, list.size() - 1, k, Ordering.natural());
	}
	public static <T> T quickSelect(List<T> list, int k, Comparator<? super T> c) {
		return quickSelect(list, 0, list.size() - 1, k, c);
	}
	private static <T> T quickSelect(List<T> list, int left, int right, int k, Comparator<? super T> c) {
		if (right == left) return list.get(left);
		int pivotIndex = medianOf3Pivot(list, left, right, c);
		pivotIndex = partition(list, left, right, pivotIndex, c);
		if (k < pivotIndex) {
			return quickSelect(list, left, pivotIndex - 1, k, c);
		} else if (k > pivotIndex) {
			return quickSelect(list, pivotIndex + 1, right, k, c);
		} else {
			return list.get(k);
		}
	}
	
	public static <T extends Comparable<? super T>> void quickPartialSort(List<T> list, int k) {
		quickPartialSort(list, 0, list.size() - 1, k, Ordering.natural());
	}
	public static <T> void quickPartialSort(List<T> list, int k, Comparator<? super T> c) {
		quickPartialSort(list, 0, list.size() - 1, k, c);
	}
	private static <T> void quickPartialSort(List<T> list, int left, int right, int k, Comparator<? super T> c) {
		if (right <= left) return;
		int pivotIndex = medianOf3Pivot(list, left, right, c);
		pivotIndex = partition(list, left, right, pivotIndex, c);
		quickPartialSort(list, left, pivotIndex - 1, k, c);
		if (k > pivotIndex) {
			quickPartialSort(list, pivotIndex + 1, right, k, c);
		}
	}
	
	private static <T> int medianOf3Pivot(List<T> list, int left, int right, Comparator<? super T> c) {
		int mid = (left + right) / 2;
		if (c.compare(list.get(right), list.get(left)) < 0)
	        swap(list, left, right);      
	    if (c.compare(list.get(mid), list.get(left)) < 0)
	        swap(list, mid, left);
	    if (c.compare(list.get(right), list.get(mid)) < 0)
	        swap(list, right, mid);
	    return mid;
	}
	
	private static <T> int partition(List<T> list, int left, int right, int pivotIndex, Comparator<? super T> c) {
		T pivotValue = list.get(pivotIndex);
		swap(list, pivotIndex, right);  // Move pivot to end
		int storeIndex = left;
		for (int i = left; i < right; i++) {
			if (c.compare(list.get(i), pivotValue) < 0) {
				swap(list, storeIndex++, i);
			}
		}
		swap(list, right, storeIndex);  // Move pivot to its final place
		return storeIndex;
	}
	
}
