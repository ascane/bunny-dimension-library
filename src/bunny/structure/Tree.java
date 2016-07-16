package bunny.structure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class Tree<T> {
	
	private enum OrderType {PREORDER, POSTORDER};
	
	public T value;
	public Tree<T> parent;
	public ArrayList<Tree<T>> children;
	
	public Tree(T value) {
		this.value = value;
		this.children = new ArrayList<Tree<T>>(2);
	}
	
	@SafeVarargs
	public Tree(T value, Tree<T>... children) {
		this.value = value;
		this.children = new ArrayList<Tree<T>>(children.length);
		for (Tree<T> child : children) {
			this.children.add(child);
			child.parent = this;
		}
	}
	
	public Tree(T value, Collection<Tree<T>> children) {
		this.value = value;
		this.children = new ArrayList<Tree<T>>(children.size());
		for (Tree<T> child : children) {
			this.children.add(child);
			child.parent = this;
		}
	}
	
	public boolean isLeaf() {
		return children.size() == 0;
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public Iterator<Tree<T>> getPreorderIterator() {
		return new TreeXorderIterator(OrderType.PREORDER);
	}
	
	public Iterator<Tree<T>> getPostorderIterator() {
		return new TreeXorderIterator(OrderType.POSTORDER);
	}
	
	public Iterator<Tree<T>> getBFSIterator() {
		return new TreeBFSIterator();
	}
	
	@Override
	public String toString() {
		if (isLeaf()) {
			return value.toString();
		}
		StringBuilder str = new StringBuilder();
		str.append(value + "(");
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
		
		public TreeXorderIterator(OrderType type) {
			this.type = type;
			this.root = Tree.this;
			this.current = root;
			this.states = new ArrayList<Integer>();
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
				if (currentState < current.children.size()) {
					current = current.children.get(currentState);
					level++;
					states.add(0);
					if (type == OrderType.PREORDER) {
						break;
					}
				} else {
					current = current.parent;
					if (current == null) {
						finished = true;
						break;
					}
					states.remove(level);
					level--;
					states.set(level, states.get(level) + 1);
					if (type == OrderType.POSTORDER) {
						break;
					}
				}
			}
		}
	}
	
	private class TreeBFSIterator implements Iterator<Tree<T>> {
		
		private Queue<Tree<T>> toCheck;
		
		public TreeBFSIterator() {
			toCheck = new ArrayDeque<>();
			toCheck.add(Tree.this);
		}

		@Override
		public boolean hasNext() {
			return !toCheck.isEmpty();
		}

		@Override
		public Tree<T> next() {
			Tree<T> current = toCheck.remove();
			toCheck.addAll(current.children);
			return current;
		}
	}
}
