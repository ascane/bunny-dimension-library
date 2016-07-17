package bunny.structure;

import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class BinaryTree<T> {
	
	private enum IterateState {FROM_PARENT, FROM_LEFT, FROM_RIGHT;};
	private enum OrderType {PREORDER, INORDER, POSTORDER};
	
	private T value;
	private BinaryTree<T> parent, left, right;
	
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
	
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public BinaryTree<T> getParent() {
		return parent;
	}
	public void setParent(BinaryTree<T> parent) {
		this.parent = parent;
	}
	public BinaryTree<T> getLeft() {
		return left;
	}
	public void setLeft(BinaryTree<T> left) {
		this.left = left;
	}
	public BinaryTree<T> getRight() {
		return right;
	}
	public void setRight(BinaryTree<T> right) {
		this.right = right;
	}
	
	public boolean isLeaf() {
		return getLeft() == null && getRight() == null;
	}
	
	public boolean isRoot() {
		return getParent() == null;
	}
	
	public Iterator<BinaryTree<T>> getPreorderIterator() {
		return new BinaryTreeXorderIterator(OrderType.PREORDER, this);
	}
	
	public Iterator<BinaryTree<T>> getInorderIterator() {
		return new BinaryTreeXorderIterator(OrderType.INORDER, this);
	}
	
	public Iterator<BinaryTree<T>> getPostorderIterator() {
		return new BinaryTreeXorderIterator(OrderType.POSTORDER, this);
	}
	
	public Iterator<BinaryTree<T>> getBFSIterator() {
		return new BinaryTreeBFSIterator(this);
	}
	
	public BinaryTree<T> clone() {
		BinaryTree<T> leftClone = (getLeft() == null ? null : getLeft().clone());
		BinaryTree<T> rightClone = (getRight() == null ? null : getRight().clone());
		return new BinaryTree<T>(value, leftClone, rightClone);
	}
	
	public Tree<T> toTree() {
		ArrayList<Tree<T>> children = new ArrayList<>(2); 
		if (getLeft() != null) {
			children.add(getLeft().toTree());
		}
		if (getRight() != null) {
			children.add(getRight().toTree());
		}
		return new Tree<T>(value, children);
	}
	
	public Tree<T> asTree() {
		return new BinaryTreeAsTree<T>(this);
	}
	
	public static <T> BinaryTree<T> from(Tree<T> tree) {
		BinaryTree<T> left = null, right = null;
		List<Tree<T>> children = tree.getChildren();
		if (children.size() > 2) {
			throw new IllegalArgumentException("The tree is not a binary tree!");
		}
		if (children.size() > 0) {
			left = from(children.get(0));
		}
		if (tree.getChildren().size() > 1) {
			right = from(children.get(1));
		}
		return new BinaryTree<T>(tree.getValue(), left, right);
	}
	
	@Override
	public String toString() {
		if (isLeaf()) {
			return value.toString();
		}
		return String.format("%s(%s,%s)", 
				value.toString(),
				getLeft() == null ? "" : getLeft().toString(),
				getRight() == null ? "" : getRight().toString());
	}
	
	private class BinaryTreeXorderIterator implements Iterator<BinaryTree<T>> {
		
		private OrderType type;
		private BinaryTree<T> root;
		private BinaryTree<T> current;
		
		private IterateState state;
		boolean finished = false;
		
		public BinaryTreeXorderIterator(OrderType type, BinaryTree<T> root) {
			this.type = type;
			this.root = root;
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
					if (current.getLeft() != null) {
						current = current.getLeft();
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
					if (current.getRight() != null) {
						current = current.getRight();
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
						current = current.getParent();
						if (current.getLeft() == last) {
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
		
		public BinaryTreeBFSIterator(BinaryTree<T> root) {
			toCheck = new ArrayDeque<>();
			toCheck.add(root);
		}

		@Override
		public boolean hasNext() {
			return !toCheck.isEmpty();
		}

		@Override
		public BinaryTree<T> next() {
			BinaryTree<T> current = toCheck.remove();
			if (current.left != null) {
				toCheck.add(current.getLeft());
			}
			if (current.right != null) {
				toCheck.add(current.getRight());
			}
			return current;
		}
	}
	
	private static class BinaryTreeAsTree<T> extends Tree<T> {

		private BinaryTree<T> original;
		
		public BinaryTreeAsTree(BinaryTree<T> original) {
			super(original.getValue());
			this.original = original;
		}
		@Override
		public T getValue() {
			return original.getValue();
		}
		@Override
		public void setValue(T value) {
			original.setValue(value);
		}
		@Override
		public Tree<T> getParent() {
			if (original.getParent() == null) {
				return null;
			}
			return new BinaryTreeAsTree<T>(original.getParent());
		}
		@Override
		public void setParent(Tree<T> parent) {
			throw new UnsupportedOperationException("Tree view of a binary tree cannot change structure.");
		}
		@Override
		public List<Tree<T>> getChildren() {
			return new BinaryTreeAsTreeChildrenView();
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof BinaryTreeAsTree<?>)) return false;
			return original.equals(((BinaryTreeAsTree<?>)o).original);
		}
		
		@Override
		public int hashCode() {
			return original.hashCode();
		}
		
		private class BinaryTreeAsTreeChildrenView extends AbstractList<Tree<T>> {

			@Override
			public int size() {
				int size = 0;
				if (original.getLeft() != null) size++;
				if (original.getRight() != null) size++;
				return size;
			}
			
			@Override
			public Tree<T> get(int index) {
				if (index == 0) {
					if (original.getLeft() != null) {
						return new BinaryTreeAsTree<T>(original.getLeft());
					} else if (original.getRight() != null) {
						return new BinaryTreeAsTree<T>(original.getRight());
					}
				}
				if (index == 1) {
					if (original.getLeft() != null && original.getRight() != null) {
						return new BinaryTreeAsTree<T>(original.getRight());
					}
				}
				throw new IndexOutOfBoundsException();
			}
			
			@Override
			public Tree<T> remove(int index) {
				Tree<T> temp;
				if (index == 0) {
					if (original.getLeft() != null) {
						temp = new BinaryTreeAsTree<T>(original.getLeft());
						original.setLeft(null);
						return temp;
					} else if (original.getRight() != null) {
						temp = new BinaryTreeAsTree<T>(original.getRight());
						original.setRight(null);
						return temp;
					}
				}
				if (index == 1) {
					if (original.getLeft() != null && original.getRight() != null) {
						temp = new BinaryTreeAsTree<T>(original.getRight());
						original.setRight(null);
						return temp;
					}
				}
				throw new IndexOutOfBoundsException();
			}
			
			@Override
			public void clear() {
				original.setLeft(null);
				original.setRight(null);
			}
		}
	}
}
