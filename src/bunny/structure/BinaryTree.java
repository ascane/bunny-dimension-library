package bunny.structure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

public class BinaryTree<T> {
	
	private enum IterateState {FROM_PARENT, FROM_LEFT, FROM_RIGHT;};
	private enum OrderType {PREORDER, INORDER, POSTORDER};
	
	public T value;
	public BinaryTree<T> parent, left, right;
	
	public BinaryTree(T value) {
		this.value = value;
	}
	
	public BinaryTree(T value, BinaryTree<T> left, BinaryTree<T> right) {
		this.value = value;
		if (left != null) {
			this.left = left;
			left.parent = this;
		}
		if (right != null) {
			this.right = right;
			right.parent = this;
		}
	}
	
	public boolean isLeaf() {
		return left == null && right == null;
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public Iterator<BinaryTree<T>> getPreorderIterator() {
		return new BinaryTreeXorderIterator(OrderType.PREORDER);
	}
	
	public Iterator<BinaryTree<T>> getInorderIterator() {
		return new BinaryTreeXorderIterator(OrderType.INORDER);
	}
	
	public Iterator<BinaryTree<T>> getPostorderIterator() {
		return new BinaryTreeXorderIterator(OrderType.POSTORDER);
	}
	
	public Iterator<BinaryTree<T>> getBFSIterator() {
		return new BinaryTreeBFSIterator();
	}
	
	public Tree<T> toTree() {
		ArrayList<Tree<T>> children = new ArrayList<Tree<T>>(2); 
		if (left != null) {
			children.add(left.toTree());
		}
		if (right != null) {
			children.add(right.toTree());
		}
		return new Tree<T>(value, children);
	}
	
	public static <T> BinaryTree<T> from(Tree<T> tree) {
		BinaryTree<T> left = null, right = null;
		if (tree.children.size() > 2) {
			throw new IllegalArgumentException("The tree is not a binary tree!");
		}
		if (tree.children.size() > 0) {
			left = from(tree.children.get(0));
		}
		if (tree.children.size() > 1) {
			right = from(tree.children.get(1));
		}
		return new BinaryTree<T>(tree.value, left, right);
	}
	
	@Override
	public String toString() {
		if (isLeaf()) {
			return value.toString();
		}
		return String.format("%s(%s,%s)", 
				value.toString(),
				left == null ? "" : left.toString(),
				right == null ? "" : right.toString());
	}
	
	private class BinaryTreeXorderIterator implements Iterator<BinaryTree<T>> {
		
		private OrderType type;
		private BinaryTree<T> root;
		private BinaryTree<T> current;
		
		private IterateState state;
		boolean finished = false;
		
		public BinaryTreeXorderIterator(OrderType type) {
			this.type = type;
			this.root = BinaryTree.this;
			this.current = root;
			this.state = IterateState.FROM_PARENT;
			if (type != OrderType.PREORDER) {
				prepareForNext();
			}
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
				if (state == IterateState.FROM_PARENT) {
					if (current.left != null) {
						current = current.left;
						if (type == OrderType.PREORDER) {
							break;
						}
					} else {
						state = IterateState.FROM_LEFT;
						if (type == OrderType.INORDER) {
							break;
						}
					}
				}
				if (state == IterateState.FROM_LEFT) {
					if (current.right != null) {
						current = current.right;
						state = IterateState.FROM_PARENT;
						if (type == OrderType.PREORDER) {
							break;
						}
					} else {
						state = IterateState.FROM_RIGHT;
						if (type == OrderType.POSTORDER) {
							break;
						}
					}
				}
				if (state == IterateState.FROM_RIGHT) {
					if (current == root) {
						finished = true;
						break;
					} else {
						BinaryTree<T> last = current;
						current = current.parent;
						if (current.left == last) {
							state = IterateState.FROM_LEFT;
							if (type == OrderType.INORDER) {
								break;
							}
						} else {
							state = IterateState.FROM_RIGHT;
							if (type == OrderType.POSTORDER) {
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private class BinaryTreeBFSIterator implements Iterator<BinaryTree<T>> {
		
		private Queue<BinaryTree<T>> toCheck;
		
		public BinaryTreeBFSIterator() {
			toCheck = new ArrayDeque<>();
			toCheck.add(BinaryTree.this);
		}

		@Override
		public boolean hasNext() {
			return !toCheck.isEmpty();
		}

		@Override
		public BinaryTree<T> next() {
			BinaryTree<T> current = toCheck.remove();
			if (current.left != null) {
				toCheck.add(current.left);
			}
			if (current.right != null) {
				toCheck.add(current.right);
			}
			return current;
		}
	}
}
