package bunny.structure;

import java.util.HashMap;

public class UnionFind<T> {
	private HashMap<T, UnionFindNode> map;
	
	public UnionFind() {
		map = new HashMap<T, UnionFindNode>();
	}
	
	public void MakeSet(T element) {
		map.put(element, new UnionFindNode());
	}
	
	public UnionFindNode Find(T element) {
		return map.get(element).find();
	}
	
	public void Union(T e1, T e2) {
		map.get(e1).union(map.get(e2));
	}
	
	
	public static class UnionFindNode {
		public UnionFindNode parent;
		private int level;
		
		public UnionFindNode() {
			this.parent = this;
			level = 0;
		}
		
		public UnionFindNode find() {
			if (parent != this) {
				UnionFindNode root = parent.find();
				this.parent = root;
				return root;
			} else {
				return this;
			}
		}
		
		public void union(UnionFindNode other) {
			UnionFindNode myRoot = find();
			UnionFindNode yourRoot = other.find();
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


