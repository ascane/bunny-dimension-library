package bunny.structure;

import java.util.HashMap;

/**
 * The classical Union-find algorithm.
 * 
 * <p>Usage: Use UnionFind<T> with objects or directly use UnionFind.Node
 * and make the correspondence.
 */
public class UnionFind<T> {
	
	private HashMap<T, Node> map;
	
	public UnionFind() {
		map = new HashMap<T, Node>();
	}
	
	public void makeSet(T element) {
		map.put(element, new Node());
	}
	
	public Node find(T element) {
		return map.get(element).find();
	}
	
	public void union(T e1, T e2) {
		map.get(e1).union(map.get(e2));
	}
	
	
	public static class Node {
		
		public Node parent;
		
		private int level;
		
		public Node() {
			this.parent = this;
			level = 0;
		}
		
		public Node find() {
			if (parent != this) {
				Node root = parent.find();
				this.parent = root;
				return root;
			} else {
				return this;
			}
		}
		
		public void union(Node other) {
			Node myRoot = find();
			Node yourRoot = other.find();
			if (myRoot.level > yourRoot.level) {
				yourRoot.parent = myRoot;
			} else if (yourRoot.level > myRoot.level) {
				myRoot.parent = yourRoot;
			} else {
				yourRoot.parent = myRoot;
				myRoot.level++;
			}
		}
	}
}
