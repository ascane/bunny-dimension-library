package bunny.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BasicLinkedList<T> implements Iterable<T> {
	
	public BasicLinkedList<T> next;
	public T value;
	
	public BasicLinkedList(T value) {
		this.value = value;
	}
	
	public static <T> BasicLinkedList<T> from(Iterable<? extends T> data) {
		Iterator<? extends T> it = data.iterator();
		if (!it.hasNext()) {
			return null;
		}
		BasicLinkedList<T> head = new BasicLinkedList<T>(it.next());
		BasicLinkedList<T> current = head;
		while (it.hasNext()) {
			current.next = new BasicLinkedList<T>(it.next());
			current = current.next;
		}
		return head;
	}

	@Override
	public Iterator<T> iterator() {
		return new BasicLinkedListIterator();
	}
	
	private class BasicLinkedListIterator implements Iterator<T> {
		
		private BasicLinkedList<T> current = BasicLinkedList.this;
		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T result = current.value;
			current = current.next;
			return result;
		}
		
	}
}
