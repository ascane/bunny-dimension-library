package bunny.structure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph<V, E> {
	
	private Set<Node<V, E>> nodes;
	private Set<Edge<V, E>> edges;
	
	public Graph() {
		nodes = new HashSet<Node<V, E>>();
		edges = new HashSet<Edge<V, E>>();
	}
	
	public Set<Node<V, E>> getNodes() {
		return nodes;
	}
	
	public Node<V, E> createNode(V value) {
		Node<V, E> newNode = new Node<V, E>(value);
		nodes.add(newNode);
		return newNode;
	}
	
	public boolean removeNode(Node<V, E> node) {
		for (Edge<V, E> edge : node.getInboundEdges().values()) {
			removeEdge(edge.from, edge.to);
		}
		for (Edge<V, E> edge : node.getOutboundEdges().values()) {
			removeEdge(edge.from, edge.to);
		}
		return nodes.remove(node);
	}
	
	public boolean hasEdge(Node<V, E> from, Node<V, E> to) {
		return getEdge(from, to) != null;
	}
	
	/**
	 * Returns the edge value from one node to another.
	 * 
	 * <p>The value may be null.
	 */
	public Edge<V, E> getEdge(Node<V, E> from, Node<V, E> to) {
		return from.outboundEdges.get(to);
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
		Edge<V, E> edge = new Edge<V, E>(value, from, to);
		from.getOutboundEdges().put(to, edge);
		to.getInboundEdges().put(from, edge);
		edges.add(edge);
	}
	
	public void createBidirectionalEdge(E value, Node<V, E> n1, Node<V, E> n2) {
		createEdge(value, n1, n2);
		createEdge(value, n2, n1);
	}
	
	public void removeEdge(Node<V, E> from, Node<V, E> to) {
		Edge<V, E> edge = getEdge(from, to);
		if (edge != null) {
			from.getOutboundEdges().remove(to);
			to.getInboundEdges().remove(from);
			edges.remove(edge);
		}
	}
	
	public void removeBidirectionalEdge(Node<V, E> n1, Node<V, E> n2) {
		removeEdge(n1, n2);
		removeEdge(n2, n1);
	}
	
	public static class Node<V, E> {
		
		private V value;
		private Map<Node<V, E>, Edge<V, E>> inboundEdges, outboundEdges; // From neighbor node to edge value.
		
		public Node(V value) {
			this.value = value;
			this.inboundEdges = new HashMap<Node<V, E>, Edge<V, E>>(4);
			this.outboundEdges = new HashMap<Node<V, E>, Edge<V, E>>(4);
		}
		
		public V getValue() {
			return value;
		}
		
		public void setValue(V value) {
			this.value = value;
		}
		
		public Map<Node<V, E>, Edge<V, E>> getInboundEdges() {
			return inboundEdges;
		}
		
		public Map<Node<V, E>, Edge<V, E>> getOutboundEdges() {
			return outboundEdges;
		}
	}
	
	public static class Edge<V, E> {
		
		private E value;
		private Node<V, E> from, to;
		
		public Edge(E value, Node<V, E> from, Node<V, E> to) {
			this.value = value;
			this.from = from;
			this.to = to;
		}
		
		public E getValue() {
			return value;
		}
		
		public void setValue(E value) {
			this.value = value;
		}
		
		public Node<V, E> from() {
			return from;
		}
		
		public Node<V, E> to() {
			return to;
		}
	}
}
