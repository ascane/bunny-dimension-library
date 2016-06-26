package structure;
import java.util.Iterator;
import java.util.function.Function;

public class BinaryTree<T> {
	public T value;
	public BinaryTree<T> parent, left, right;
	private enum State {
		FROM_PARENT, FROM_LEFT, FROM_RIGHT;
	};
	
	public BinaryTree(T value) {
		this.value = value;
		this.parent = null;
		this.left = null;
		this.right = null;
	}
	
	public BinaryTree(T value, BinaryTree<T> left, BinaryTree<T> right) {
		this.value = value;
		this.parent = null;
		if (left != null) {
			this.left = left;
			left.parent = this;
		}
		if (right != null) {
			this.right = right;
			right.parent = this;
		}
	}
	
	public String toString() {
		return toString((T t) -> t.toString());
	}
	
	public String toString(Function<T, String> F) {
		if (this.isLeaf()) {
			return F.apply(value);
		}
		return String.format("%s(%s,%s)", 
				F.apply(value),
				left == null ? "" : left.toString(F),
				right == null ? "" : right.toString(F));
	}
	
	public Iterator<BinaryTree<T>> getPreorderIterator() {
		return new BinaryTreePreorderIterator<T>(this);
	}
	
	public Iterator<BinaryTree<T>> getInorderIterator() {
		return null;
	}
	
	public Iterator<BinaryTree<T>> getPostorderIterator() {
		return null;
	}
	
	public boolean isLeaf() {
		return this.left == null && this.right == null;
	}
	
	private static class BinaryTreePreorderIterator<T> implements Iterator<BinaryTree<T>> {
		private BinaryTree<T> root;
		private BinaryTree<T> current;
		
		private State state;
		boolean finished;
		
		public BinaryTreePreorderIterator(BinaryTree<T> root) {
			this.root = root;
			this.current = root;
			this.state = State.FROM_PARENT;
			this.finished = false;
		}

		@Override
		public boolean hasNext() {
			return !finished;
		}

		@Override
		public BinaryTree<T> next() {
			BinaryTree<T> result = current;
			prepareForNext();
			return result;
		}
		
		private void prepareForNext() {
			while (true) {
				if (state == State.FROM_PARENT) {
					if (current.left != null) {
						current = current.left;
						break;
					} else {
						state = State.FROM_LEFT;
					}
				}
				if (state == State.FROM_LEFT) {
					if (current.right != null) {
						current = current.right;
						state = State.FROM_PARENT;
						break;
					} else {
						state = State.FROM_RIGHT;
					}
				}
				if (state == State.FROM_RIGHT) {
					if (current == root) {
						finished = true;
						break;
					} else {
						BinaryTree<T> last = current;
						current = current.parent;
						if (current.left == last) {
							state = State.FROM_LEFT;
						} else {
							state = State.FROM_RIGHT;
						}
					}
				}
			}
		}
	}
}
