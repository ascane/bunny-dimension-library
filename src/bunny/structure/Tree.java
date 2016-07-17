package bunny.structure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class Tree<T> {
	
	private enum OrderType {PREORDER, POSTORDER};
	
	private T value;
	private Tree<T> parent;
	private ArrayList<Tree<T>> children;
	
	public Tree(T value) {
		this.value = value;
		this.children = new ArrayList<>(2);
	}
	
	@SafeVarargs
	public Tree(T value, Tree<T>... children) {
		this.value = value;
		this.children = new ArrayList<>(children.length);
		for (Tree<T> child : children) {
			this.children.add(child);
			child.parent = this;
		}
	}
	
	public Tree(T value, Collection<Tree<T>> children) {
		this.value = value;
		this.children = new ArrayList<>(children.size());
		for (Tree<T> child : children) {
			this.children.add(child);
			child.parent = this;
		}
	}
	
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public Tree<T> getParent() {
		return parent;
	}
	public void setParent(Tree<T> parent) {
		this.parent = parent;
	}
	public List<Tree<T>> getChildren() {
		return children;
	}
	
	public boolean isLeaf() {
		return getChildren().size() == 0;
	}
	
	public boolean isRoot() {
		return getParent() == null;
	}
	
	public Iterator<Tree<T>> getPreorderIterator() {
		return new TreeXorderIterator(OrderType.PREORDER, this);
	}
	
	public Iterator<Tree<T>> getPostorderIterator() {
		return new TreeXorderIterator(OrderType.POSTORDER, this);
	}
	
	public Iterator<Tree<T>> getBFSIterator() {
		return new TreeBFSIterator(this);
	}
	
	public Tree<T> clone() {
		ArrayList<Tree<T>> childrenClone = new ArrayList<>();
		for (Tree<T> child : getChildren()) {
			childrenClone.add(child.clone());
		}
		return new Tree<T>(value, childrenClone);
	}
	
	@Override
	public String toString() {
		if (isLeaf()) {
			return getValue().toString();
		}
		StringBuilder str = new StringBuilder();
		str.append(getValue() + "(");
		List<Tree<T>> children = getChildren();
		for (int i = 0; i < children.size() - 1; i++) {
			str.append(children.get(i).toString() + ",");
		}
		str.append(children.get(children.size() - 1) + ")");
		return str.toString();
	}
	
	private class TreeXorderIterator implements Iterator<Tree<T>> {
		
		private OrderType type;
		private Tree<T> root;
		private Tree<T> current;
		
		private ArrayList<Integer> states; // Track of the next child to visit.
		private int level = 0;
		private boolean finished = false;
		
		public TreeXorderIterator(OrderType type, Tree<T> root) {
			this.type = type;
			this.root = root;
			this.current = root;
			this.states = new ArrayList<>();
			this.states.add(0);
			if (type == OrderType.POSTORDER) {
				prepareForNext();
			}
		}

		@Override
		public boolean hasNext() {
			return !finished;
		}

		@Override
		public Tree<T> next() {
			Tree<T> result = current;
			prepareForNext();
			return result;
		}
		
		private void prepareForNext() {
			while (true) {
				int currentState = states.get(level);
				if (currentState < current.getChildren().size()) {
					current = current.getChildren().get(currentState);
					level++;
					states.add(0);
					if (type == OrderType.PREORDER) {
						break;
					}
					if (type == OrderType.POSTORDER && current.getChildren().size() == 0) {
						break;
					}
				} else {
					if (current.equals(root)) {
						finished = true;
						break;
					}
					current = current.getParent();
					states.remove(level);
					level--;
					states.set(level, states.get(level) + 1);
					if (type == OrderType.POSTORDER && states.get(level) == current.getChildren().size()) {
						break;
					}
				}
			}
		}
	}
	
	private class TreeBFSIterator implements Iterator<Tree<T>> {
		
		private Queue<Tree<T>> toCheck;
		
		public TreeBFSIterator(Tree<T> root) {
			toCheck = new ArrayDeque<>();
			toCheck.add(root);
		}

		@Override
		public boolean hasNext() {
			return !toCheck.isEmpty();
		}

		@Override
		public Tree<T> next() {
			Tree<T> current = toCheck.remove();
			toCheck.addAll(current.getChildren());
			return current;
		}
	}
}
