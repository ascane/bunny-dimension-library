package bunny.algo;

import bunny.structure.DoubleLinkedList;

public class Sort {
	
	public static <T> void mergeSort(DoubleLinkedList<T> list) {
		mergeSort(list.getHeadNode(), list.size());
	}
	public static <T> void mergeSort(DoubleLinkedList<T>.Node head, int count) {
		throw new UnsupportedOperationException();
		// TODO(chiaman): Implement this.
	}
}
