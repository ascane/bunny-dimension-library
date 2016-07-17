package bunny.structure;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph<V, E> {
	
	private Set<Node<V, E>> nodes;
	private Set<Edge<V, E>> edges;
	
	public Graph() {
		nodes = new HashSet<>();
		edges = new HashSet<>();
	}
	
	public Set<Node<V, E>> getNodes() {
		return nodes;
	}
	
	public Set<Edge<V, E>> getEdges() {
		return edges;
	}
	
	public Node<V, E> createNode(V value) {
		Node<V, E> newNode = new Node<>(value);
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
		Edge<V, E> edge = new Edge<>(value, from, to);
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
	
	public Iterator<Node<V, E>> getDFSIterator(Node<V, E> startNode) {
		return new GraphDFSIterator(startNode);
	}
	
	public Iterator<Node<V, E>> getBFSIterator(Node<V, E> startNode) {
		return new GraphBFSIterator(startNode);
	}
	
	public Graph<V, E> clone() {
		Graph<V, E> copy = new Graph<>();
		HashMap<Node<V, E>, Node<V, E>> map = new HashMap<>();
		for (Node<V, E> node : nodes) {
			map.put(node, copy.createNode(node.value));
		}
		for (Edge<V, E> edge : edges) {
			copy.createEdge(edge.value, map.get(edge.from), map.get(edge.to));
		}
		return copy;
	}
	
	private class GraphDFSIterator implements Iterator<Node<V, E>> {
		
		private Deque<Node<V, E>> toCheck;
		private Set<Node<V, E>> visited;
		
		public GraphDFSIterator(Node<V, E> startNode) {
			toCheck = new ArrayDeque<>();
			toCheck.addLast(startNode);
			visited = new HashSet<>();
			visited.add(startNode);
		}

		@Override
		public boolean hasNext() {
			return !toCheck.isEmpty();
		}

		@Override
		public Node<V, E> next() {
			Node<V, E> current = toCheck.removeLast();
			for (Node<V, E> neighbor : current.getOutboundEdges().keySet()) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					toCheck.addLast(neighbor);
				}
			}
			return current;
		}
	}
	
	private class GraphBFSIterator implements Iterator<Node<V, E>> {
		
		private Queue<Node<V, E>> toCheck;
		private Set<Node<V, E>> visited;
		
		public GraphBFSIterator(Node<V, E> startNode) {
			toCheck = new ArrayDeque<>();
			toCheck.add(startNode);
			visited = new HashSet<>();
			visited.add(startNode);
		}

		@Override
		public boolean hasNext() {
			return !toCheck.isEmpty();
		}

		@Override
		public Node<V, E> next() {
			Node<V, E> current = toCheck.remove();
			for (Node<V, E> neighbor : current.getOutboundEdges().keySet()) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					toCheck.add(neighbor);
				}
			}
			return current;
		}
	}
	
	public static class Node<V, E> {
		
		private V value;
		private Map<Node<V, E>, Edge<V, E>> inboundEdges, outboundEdges; // From neighbor node to edge value.
		
		public Node(V value) {
			this.value = value;
			this.inboundEdges = new HashMap<>(4);
			this.outboundEdges = new HashMap<>(4);
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
