package bunny.structure;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> extends AbstractSequentialList<T> {
	
	private int size;
	private Node head, tail;
	
	public DoubleLinkedList() {
		this.size = 0;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public ListIterator<T> listIterator(int index) {
		return new DoubleLinkedListIterator();
	}
	
	public Node getHeadNode() {
		return head;
	}
	
	public Node getTailNode() {
		return tail;
	}
	
	public class Node {
		public Node prev, next;
		public T value;
		
		public Node(T value) {
			this.value = value;
		}
		
		public void insertAfter(T value) {
			Node newNode = new Node(value);
			if (next != null) {
				next.prev = newNode;
				newNode.next = next;
			}
			next = newNode;
			newNode.prev = this;
			size++;
		}
		
		public void insertBefore(T value) {
			Node newNode = new Node(value);
			if (prev != null) {
				prev.next = newNode;
				newNode.prev = prev;
			}
			prev = newNode;
			newNode.next = this;
			size++;
		}
		
		public void remove() {
			if (next != null) {
				next.prev = prev;
			}
			if (prev != null) {
				prev.next = next;
			}
			size--;
		}
	}
	
	private class DoubleLinkedListIterator implements ListIterator<T> {

		private Node nextNode;
		private int nextIndex = 0;
		private Node lastReturned;
		
		public DoubleLinkedListIterator() {
			this.nextNode = head;
		}
		
		private Node getPrevNode() {
			if (nextNode != null) {
				return nextNode.prev;
			}
			return tail;
		}
		
		@Override
		public void add(T value) {
			Node prevNode = getPrevNode();
			Node newNode = new Node(value);
			if (nextNode != null) {
				newNode.next = nextNode;
				nextNode.prev = newNode;
			}
			if (prevNode != null) {
				newNode.prev = prevNode;
				prevNode.next = newNode;
			}
			nextIndex++;
			size++;
			lastReturned = null;
		}

		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public boolean hasPrevious() {
			return getPrevNode() != null;
		}

		@Override
		public T next() {
			if (nextNode == null) {
				throw new NoSuchElementException();
			}
			lastReturned = nextNode;
			nextNode = nextNode.next;
			nextIndex++;
			return lastReturned.value;
		}

		@Override
		public int nextIndex() {
			return nextIndex;
		}

		@Override
		public T previous() {
			Node prevNode = getPrevNode();
			if (prevNode == null) {
				throw new NoSuchElementException();
			}
			lastReturned = prevNode;
			prevNode = prevNode.prev;
			nextIndex--;
			return lastReturned.value;
		}

		@Override
		public int previousIndex() {
			return nextIndex - 1;
		}

		@Override
		public void remove() {
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			if (lastReturned.next != null) {
				lastReturned.next.prev = lastReturned.prev;
			}
			if (lastReturned.prev != null) {
				lastReturned.prev.next = lastReturned.next;
			}
			if (lastReturned == nextNode) {
				nextNode = lastReturned.next;
			} else {
				nextIndex--;
			}
			size--;
			lastReturned = null;
		}

		@Override
		public void set(T value) {
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			lastReturned.value = value;
		}
	}
}
