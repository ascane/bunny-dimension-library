package bunny.algo;

import bunny.structure.BasicLinkedList;

public class Sort {
	
	public static <T extends Comparable<T>> void mergeSort(BasicLinkedList<T> list) {
		BasicLinkedList<T> current = list;
		int count = 0;
		while (current != null) {
			current = current.next;
			count++;
		}
		mergeSort(list, count);
	}
	
	/**
	 * Sorts the first k elements and returns the pointer to the (k + 1)th element
	 * if it exists, otherwise null.
	 */
	private static <T extends Comparable<? super T>> BasicLinkedList<T> mergeSort(
			BasicLinkedList<T> list, int k) {
		if (k == 1) {
			return list.next;
		}
		int k1 = k / 2;
		BasicLinkedList<T> list2 = mergeSort(list, k1);
		int k2 = k - k1;
		mergeSort(list2, k2);
		return merge(list, list2, k1, k2);
	}
	
	/**
	 * Merges two {@link BasicLinkedList} instances and returns the pointer to the next
	 * value of the last element of the merged list. The returned value can be null if
	 * there is no element left.
	 * 
	 * @param l1 The first list to merge, sorted in its natural order.
	 * @param l2 The second list to merge, right after l1, sorted in its natural order.
	 * @param k1 The number of elements of l1, >= 1.
	 * @param k2 The number of elements of l2, >= 1.
	 */
	private static <T extends Comparable<? super T>> BasicLinkedList<T> merge(
			BasicLinkedList<T> l1, BasicLinkedList<T> l2, int k1, int k2) {
		int count1 = 0;
		int count2 = 0;
		BasicLinkedList<T> current1 = l1;
		BasicLinkedList<T> current2 = l2;
		// First, make sure that current2.value > current1.value.
		if (l2.value.compareTo(l1.value) < 0) {
			// Insert l2 after l1.
			current2 = l2.next;
			l2.next = l1.next;
			l1.next = l2;
			// Swap their value.
			T temp = l1.value;
			l1.value = l2.value;
			l2.value = temp;
			k1++;
			k2--;
		}
		
		// Insert every element of the second list to the first list.
		while (count2 < k2) {
			// Find the position of the first list to insert an element of the second list
			// such that current1.value < current2.value < current1.next.value.
			while (count1 < k1 - 1 && current1.next.value.compareTo(current2.value) < 0) {
				current1 = current1.next;
				count1++;
			}
			BasicLinkedList<T> temp2 = current2.next;
			// Normal case: Stop at one valid position of the first list, which is not the last element.
			if (count1 < k1 - 1) {
				current2.next = current1.next;
				current1.next = current2;
				current1 = current2;
			// Edge case: Stop at the last element of the first list.
			} else if (count1 == k1 - 1) {
				current1.next = current2;
				count1++;
			}
			current2 = temp2;
			count2++;
		}
		
		// After inserting every element of the second list to the first list, if currnet1
		// does not point to the last element of the first list, point it to the last one.
		if (count1 < k1 - 1) {
			while (count1 < k1 - 1) {
				current1 = current1.next;
				count1++;
			}
			current1.next = current2;
		}
		return current2;
	}
}
