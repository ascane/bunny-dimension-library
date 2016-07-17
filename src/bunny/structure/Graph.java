package bunny.structure;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import bunny.wrap.SetView;

public class Graph<V, E> {
	
	private Set<Node<V, E>> nodes;
	
	public Set<Node<V, E>> getNodes() {
		return nodes;
	}
	
	public void createNode(V value) {
		nodes.add(new Node<V, E>(value));
	}
	
	public Edge<V, E> getEdge(Node<V, E> from, Node<V, E> to) {
		throw new UnsupportedOperationException();
		// TODO(chiaman): Think of a better data structure.
	}
	
	public void createEdge(E value, Node<V, E> from, Node<V, E> to) {
		Edge<V, E> newEdge = new Edge<V, E>(value, from, to);
		if (!from.outBoundEdges.contains(newEdge)) {
			from.outBoundEdges.add(newEdge);
			to.inBoundEdges.add(newEdge);
		}
	}
	
	public void createBidirectionalEdge(E value, Node<V, E> n1, Node<V, E> n2) {
		createEdge(value, n1, n2);
		createEdge(value, n2, n1);
	}
	
	public void removeEdge(Node<V, E> from, Node<V, E> to) {
		
	}
	
	public void removeBidirectionalEdge(Node<V, E> n1, Node<V, E> n2) {
		
	}
	
	public static class Node<V, E> {
		
		private V value;
		private Set<Edge<V, E>> inBoundEdges, outBoundEdges;
		
		public Node(V value) {
			this.value = value;
			this.inBoundEdges = new HashSet<Edge<V, E>>(4);
			this.outBoundEdges = new HashSet<Edge<V, E>>(4);
		}
		
		public V getValue() {
			return value;
		}
		
		public Set<Edge<V, E>> getInBoundEdges() {
			return new SetView<Edge<V, E>>(inBoundEdges).readOnly();
		}
		
		public Set<Edge<V, E>> getOutBoundEdges() {
			return new SetView<Edge<V, E>>(outBoundEdges).readOnly();
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
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Edge<?, ?>)) {
				return false;
			}
			Edge<?, ?> e = (Edge<?, ?>)o;
			return from().equals(e.from()) && to().equals(e.to());
		}
		
		@Override
		public int hashCode() {
			return Arrays.hashCode(new Object[]{from(), to()});
		}
	}
}
