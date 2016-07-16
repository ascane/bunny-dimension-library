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
	 * Sorts the first k elements and returns the pointer to the (k + 1)th
	 *  element if it exists, otherwise null.
	 */
	private static <T extends Comparable<? super T>> BasicLinkedList<T> mergeSort(BasicLinkedList<T> list, int k) {
		if (k == 1) {
			return list.next;
		}
		int k1 = k / 2;
		BasicLinkedList<T> list2 = mergeSort(list, k1);
		int k2 = k - k1;
		mergeSort(list2, k2);
		return merge(list, list2, k1, k2);
	}
	
	// k1, k2 >= 1.
	private static <T extends Comparable<? super T>> BasicLinkedList<T> merge(
			BasicLinkedList<T> l1, BasicLinkedList<T> l2, int k1, int k2) {
		int count1 = 0;
		int count2 = 0;
		BasicLinkedList<T> current1 = l1;
		BasicLinkedList<T> current2 = l2;
		// First, make sure that current2.value > current1.value.
		if (l2.value.compareTo(l1.value) < 0) {
			current2 = l2.next;
			l2.next = l1.next;
			l1.next = l2;
			T temp = l1.value;
			l1.value = l2.value;
			l2.value = temp;
			k1++;
			k2--;
		}
		
		while (count2 < k2) {
			while (count1 < k1 - 1 && current1.next.value.compareTo(current2.value) < 0) {
				current1 = current1.next;
				count1++;
			}
			BasicLinkedList<T> temp2 = current2.next;
			if (count1 < k1 - 1) {
				current2.next = current1.next;
				current1.next = current2;
				current1 = current2;
			} else if (count1 == k1 - 1) {
				current1.next = current2;
				count1++;
			}
			current2 = temp2;
			count2++;
		}
		
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
