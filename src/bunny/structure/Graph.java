package bunny.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph<V, E> {
	
	private ArrayList<Node<V, E>> nodes;
	
	public Graph() {
		nodes = new ArrayList<Node<V, E>>();
	}
	
	public ArrayList<Node<V, E>> getNodes() {
		return nodes;
	}
	
	public Node<V, E> createNode(V value) {
		Node<V, E> newNode = new Node<V, E>(value);
		nodes.add(newNode);
		return newNode;
	}
	
	public boolean removeNode(Node<V, E> node) {
		return nodes.remove(node);
	}
	
	/**
	 * Returns the edge value from one node to another.
	 * 
	 * <p>The value may be null.
	 */
	public E getEdgeValue(Node<V, E> from, Node<V, E> to) {
		return from.outBoundEdges.get(to);
	}
	
	/**
	 * Creates an edge if it does not already exist, otherwise update the edge value.
	 */
	public void createEdge(E value, Node<V, E> from, Node<V, E> to) {
		if (!nodes.contains(from)) {
			nodes.add(from);
		}
		if (!nodes.contains(to)) {
			nodes.add(to);
		}
		from.getOutBoundEdges().put(to, value);
		to.getInBoundEdges().put(from, value);
	}
	
	public void createBidirectionalEdge(E value, Node<V, E> n1, Node<V, E> n2) {
		createEdge(value, n1, n2);
		createEdge(value, n2, n1);
	}
	
	// Assume that both the from and to nodes exist.
	public void removeEdge(Node<V, E> from, Node<V, E> to) {
		from.getOutBoundEdges().remove(to);
		to.getInBoundEdges().remove(from);
	}
	
	public void removeBidirectionalEdge(Node<V, E> n1, Node<V, E> n2) {
		removeEdge(n1, n2);
		removeEdge(n2, n1);
	}
	
	public static class Node<V, E> {
		
		private V value;
		private Map<Node<V, E>, E> inBoundEdges, outBoundEdges; // From neighbor node to edge value.
		
		public Node(V value) {
			this.value = value;
			this.inBoundEdges = new HashMap<Node<V, E>, E>(4);
			this.outBoundEdges = new HashMap<Node<V, E>, E>(4);
		}
		
		public V getValue() {
			return value;
		}
		
		public void setValue(V value) {
			this.value = value;
		}
		
		public Map<Node<V, E>, E> getInBoundEdges() {
			return inBoundEdges;
		}
		
		public Map<Node<V, E>, E> getOutBoundEdges() {
			return outBoundEdges;
		}
	}
}
