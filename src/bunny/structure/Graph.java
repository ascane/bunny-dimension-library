package bunny.structure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph<V, E> {
	
	private ArrayList<Node<V, E>> nodes;
	private ArrayList<Edge<V, E>> edges;
	
	public Graph() {
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public Node<V, E> getNode(int index) {
		return nodes.get(index);
	}
	
	public List<Node<V, E>> getNodes() {
		return nodes;
	}
	
	public Node<V, E> createNode(V value) {
		Node<V, E> newNode = new Node<>(value);
		newNode.index = nodes.size();
		nodes.add(newNode);
		return newNode;
	}
	
	public boolean containsNode(Node<V, E> node) {
		return node.index >= 0 && node.index <= nodes.size() && nodes.get(node.index) == node;
	}
	
	public boolean removeNode(Node<V, E> node) {
		if (!containsNode(node)) {
			return false;
		}
		for (Edge<V, E> edge : node.getInboundEdges().values()) {
			removeEdge(edge.from, edge.to);
		}
		for (Edge<V, E> edge : node.getOutboundEdges().values()) {
			removeEdge(edge.from, edge.to);
		}
		Node<V, E> lastNode = nodes.get(nodes.size() - 1);
		nodes.set(node.index, lastNode);
		nodes.remove(nodes.size() - 1);
		lastNode.index = node.index;
		node.index = -1;
		return true;
	}
	
	public Edge<V, E> getEdge(int index) {
		return edges.get(index);
	}
	
	/**
	 * Returns the edge value from one node to another.
	 * 
	 * <p>The value may be null.
	 */
	public Edge<V, E> getEdge(Node<V, E> from, Node<V, E> to) {
		return from.outboundEdges.get(to);
	}
	
	public Edge<V, E> getInverseEdge(Edge<V, E> edge) {
		return edge.to.outboundEdges.get(edge.from);
	}
	
	public List<Edge<V, E>> getEdges() {
		return edges;
	}
	
	public boolean containsEdge(Edge<V, E> edge) {
		return edge.index >= 0 && edge.index <= edges.size() && edges.get(edge.index) == edge;
	}
	
	public boolean containsEdge(Node<V, E> from, Node<V, E> to) {
		return getEdge(from, to) != null;
	}
	
	/**
	 * Creates an edge if it does not already exist, otherwise update the edge value.
	 */
	public void createEdge(E value, Node<V, E> from, Node<V, E> to) {
		if (!containsNode(from) || !containsNode(to)) {
			throw new IllegalArgumentException("Nodes are not in the graph!");
		}
		Edge<V, E> edge = new Edge<>(value, from, to);
		from.getOutboundEdges().put(to, edge);
		to.getInboundEdges().put(from, edge);
		edge.index = edges.size();
		edges.add(edge);
	}
	
	public void createBidirectionalEdge(E value, Node<V, E> n1, Node<V, E> n2) {
		createEdge(value, n1, n2);
		createEdge(value, n2, n1);
	}
	
	public boolean removeEdge(Edge<V, E> edge) {
		if (!containsEdge(edge)) {
			return false;
		}
		edge.from.getOutboundEdges().remove(edge.to);
		edge.to.getInboundEdges().remove(edge.from);
		Edge<V, E> lastEdge = edges.get(edges.size() - 1);
		edges.set(edge.index, lastEdge);
		edges.remove(edges.size() - 1);
		lastEdge.index = edge.index;
		edge.index = -1;
		return true;
	}
	
	public boolean removeEdge(Node<V, E> from, Node<V, E> to) {
		Edge<V, E> edge = getEdge(from, to);
		if (edge == null) {
			return false;
		}
		return removeEdge(edge);
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
		for (Node<V, E> node : nodes) {
			copy.createNode(node.value);
		}
		for (Edge<V, E> edge : edges) {
			copy.createEdge(edge.value, copy.nodes.get(edge.from.index), copy.nodes.get(edge.to.index));
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
		protected int index = -1;
		public V value;
		private Map<Node<V, E>, Edge<V, E>> inboundEdges, outboundEdges; // From neighbor node to edge value.
		
		protected Node(V value) {
			this.value = value;
			this.inboundEdges = new HashMap<>(4);
			this.outboundEdges = new HashMap<>(4);
		}
		
		public int getIndex() {
			return index;
		}
		
		public Map<Node<V, E>, Edge<V, E>> getInboundEdges() {
			return inboundEdges;
		}
		
		public Map<Node<V, E>, Edge<V, E>> getOutboundEdges() {
			return outboundEdges;
		}
	}
	
	public static class Edge<V, E> {
		protected int index = -1;
		public E value;
		private Node<V, E> from, to;
		
		protected Edge(E value, Node<V, E> from, Node<V, E> to) {
			this.value = value;
			this.from = from;
			this.to = to;
		}
		
		public int getIndex() {
			return index;
		}
		
		public Node<V, E> from() {
			return from;
		}
		
		public Node<V, E> to() {
			return to;
		}
	}
}
