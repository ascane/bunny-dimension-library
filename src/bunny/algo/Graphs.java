package bunny.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import bunny.structure.Graph;
import bunny.structure.Graph.Edge;
import bunny.structure.Graph.Node;
import bunny.structure.UnionFind;
import bunny.util.Ordering;

public class Graphs {

	/** Returns the shortest directional path determined by Dijkstra's algorithm. */
	public static <V, E> List<Graph.Edge<V, E>> Dijkstra(
			Graph<V, E> g, Graph.Node<V, E> src, Graph.Node<V, E> dest, Function<E, Long> length) {
		// state: false = visiting, true = visited, does not containKey = unvisited.
		Map<Graph.Node<V, E>, Boolean> state = new HashMap<>();
		Map<Graph.Node<V, E>, Graph.Node<V, E>> parent = new HashMap<>();
		// Estimated distance from the src node to a certain node.
		final Map<Graph.Node<V, E>, Long> estimatedDist = new HashMap<>();
		Queue<Graph.Node<V, E>> toVisit = new PriorityQueue<>(new Comparator<Node<V, E>>() {
			@Override
			public int compare(Node<V, E> n1, Node<V, E> n2) {
				return estimatedDist.get(n1).compareTo(estimatedDist.get(n2));
			}
		});
		state.put(src, false);
		estimatedDist.put(src, 0L);
		toVisit.add(src);
		Graph.Node<V, E> currentNode = null;

		while (!toVisit.isEmpty()) {
			currentNode = toVisit.poll();
			if (currentNode.equals(dest)) {
				break;
			}
			for (Entry<Node<V, E>, Edge<V, E>> entry : currentNode.getOutboundEdges().entrySet()) {
				Graph.Node<V, E> adjacentNode = entry.getKey();
				Graph.Edge<V, E> adjacentEdge = entry.getValue();
				if (state.containsKey(adjacentNode)) {
					if (!state.get(adjacentNode)) {
						// state = visiting
						long newEstimatedDist = 
								length.apply(adjacentEdge.getValue()) +  estimatedDist.get(currentNode);
						if (newEstimatedDist < estimatedDist.get(adjacentNode)) {
							parent.put(adjacentNode, currentNode);
							estimatedDist.put(adjacentNode, newEstimatedDist);
						}
					}
				} else {
					// state = unvisitied
					state.put(adjacentNode, false);
					parent.put(adjacentNode, currentNode);
					estimatedDist.put(
							adjacentNode,
							length.apply(adjacentEdge.getValue()) +  estimatedDist.get(currentNode));
					toVisit.add(adjacentNode);
				}
			}
			state.put(currentNode, true);
		}
		
		if (!currentNode.equals(dest)) {
			return null;
		}
		List<Graph.Edge<V, E>> result = new ArrayList<>();
		while (parent.containsKey(currentNode)) {
			Graph.Node<V, E> parentNode = parent.get(currentNode);
			result.add(g.getEdge(parentNode, currentNode));
		}
		Collections.reverse(result);
		return result;
	}
	
	/**
	 * Returns a minimum spanning tree determined by Kruskal's algorithm by considering the edges' natural order.
	 * 
	 * <p>If the graph is not connected, the returned graph will contain every minimum spanning tree.
	 */
	public static <V, E extends Comparable<? super E>> Graph<V, E> mininumSpanningTreeKruskal(Graph<V, E> g) {
		Comparator<? super E> c = Ordering.natural();
		return minimumSpanningTreeKruskal(g, c);
	}
	
	/**
	 * Returns a bi-directional minimum spanning tree determined by Kruskal's algorithm.
	 * 
	 * <p>If the graph is not connected, the returned graph will contain every minimum spanning tree.
	 */
	public static <V, E> Graph<V, E> minimumSpanningTreeKruskal(Graph<V, E> g, final Comparator<? super E> c) {
		Graph<V, E> newGraph = new Graph<>();
		Map<Graph.Node<V, E>, Graph.Node<V, E>> newNodeByOldNode = new HashMap<>();
		for (Graph.Node<V, E> node : g.getNodes()) {
			newNodeByOldNode.put(node, newGraph.createNode(node.getValue()));
		}
		List<Graph.Edge<V, E>> edges = new ArrayList<>(g.getEdges());
		
		Comparator<? super Graph.Edge<V, E>> edgeComparator = new Comparator<Graph.Edge<V, E>>() {
			@Override
			public int compare(Edge<V, E> e1, Edge<V, E> e2) {
				return c.compare(e1.getValue(), e2.getValue());
			}
		};
		Collections.sort(edges, edgeComparator);
		
		UnionFind<Graph.Node<V, E>> uf = new UnionFind<>();
		int count = 0;
		for (Graph.Edge<V, E> edge : edges) {
			if (!uf.find(edge.from()).equals(uf.find(edge.to()))) {
				uf.union(edge.from(), edge.to());
				newGraph.createBidirectionalEdge(
						edge.getValue(),
						newNodeByOldNode.get(edge.from()),
						newNodeByOldNode.get(edge.to()));
				count++;
			}
			if (count == g.getNodes().size() - 1) {
				break;
			}
		}
		return newGraph;
	}
	
	/** Checks if the graph has at least one directional cycle. */
	public static <V, E> boolean hasDirectionalCycle(Graph<V, E> g) {
		throw new UnsupportedOperationException();
		// TODO(chiaman): Implement this.
	}
	
	/** Checks if the graph has at least one undirectional cycle. */
	public static <V, E> boolean hasUndirectionalCycle(Graph<V, E> g) {
		throw new UnsupportedOperationException();
		// TODO(chiaman): Implement this.
	}
	
	/**
	 * Returns the subgraph constructed by the {@code connectedNodes} in the graph.
	 * 
	 * <p>We assume that all the nodes in the {@code connectedNodes} are in the graph.
	 * 
	 * <p>Note: All the nodes in the returned graph are in the {@code connectedNodes}.
	 */
	public static <V, E> Graph<V, E> getSubgraph(Graph<V, E> g, Set<Graph.Node<V, E>> nodes) {
		Graph<V, E> newGraph = new Graph<>();
		Map<Graph.Node<V, E>, Graph.Node<V, E>> newNodeByOldNode = new HashMap<>();
		for (Graph.Node<V, E> node : nodes) {
			newNodeByOldNode.put(node, newGraph.createNode(node.getValue()));
		}
		for (Graph.Node<V, E> node : nodes) {
			for (Graph.Edge<V, E> edge : node.getOutboundEdges().values()) {
				if (nodes.contains(edge.to())) {
					newGraph.createEdge(
							edge.getValue(),
							newNodeByOldNode.get(edge.from()),
							newNodeByOldNode.get(edge.to()));
				}
			}
		}
		return newGraph;
	}
	
	/** 
	 * Returns the nodes that are connected to a specific node in the graph.
	 * 
	 * @throws IllegalArgumentException if the node is not in the graph
	 */
	public static <V, E> Set<Graph.Node<V, E>> getConnectedNodes(Graph<V, E> g, Graph.Node<V, E> node) {
		if (!g.getNodes().contains(node)) {
			throw new IllegalArgumentException("The node is not in the graph!");
		}
		Set<Graph.Node<V, E>> connectedNodes = new HashSet<>();
		Queue<Graph.Node<V, E>> toCheck = new ArrayDeque<>();
		toCheck.add(node);
		while (!toCheck.isEmpty()) {
			Graph.Node<V, E> currentNode = toCheck.poll();
			for (Graph.Node<V, E> neighbor : currentNode.getOutboundEdges().keySet()) {
				if (!connectedNodes.contains(neighbor)) {
					connectedNodes.add(neighbor);
					toCheck.add(neighbor);
				}
			}
			for (Graph.Node<V, E> neighbor : currentNode.getInboundEdges().keySet()) {
				if (!connectedNodes.contains(neighbor)) {
					connectedNodes.add(neighbor);
					toCheck.add(neighbor);
				}
			}
		}
		return connectedNodes;
	}
	
	/**
	 * Returns the connected component of the node in the graph.
	 * 
	 * @throws IllegalArgumentException if the node is not in the graph
	 */
	public static <V, E> Graph<V, E> getConnectedComponent(Graph<V, E> g, Graph.Node<V, E> node) {
		return getSubgraph(g, getConnectedNodes(g, node));
	}
	
	/** Returns a list of nodes of connected components. */
	public static <V, E> List<Set<Graph.Node<V, E>>> getNodesInAllConnectedComponents(Graph<V, E> g) {
		Set<Graph.Node<V, E>> explored = new HashSet<>();
		List<Set<Graph.Node<V, E>>> result = new ArrayList<>();
		for (Graph.Node<V, E> node : g.getNodes()) {
			if (!explored.contains(node)) {
				Set<Graph.Node<V, E>> connectedNodes = getConnectedNodes(g, node);
				result.add(connectedNodes);
				explored.addAll(connectedNodes);
			}
		}
		return result;
	}
	
	/** Returns a list of {@link Graph} instances of connected components. */
	public static <V, E> List<Graph<V, E>> getAllConnectedComponents(Graph<V, E> g) {
		List<Graph<V, E>> result = new ArrayList<>();
		for (Set<Graph.Node<V, E>> nodes : getNodesInAllConnectedComponents(g)) {
			result.add(getSubgraph(g, nodes));
		}
		return result;
	}
	
	/**
	 * Returns the maximum flow graph from the src node to the dest node of the original graph
	 * determined by Ford-Fulkerson's algorithm.
	 * 
	 * <p>The maximum flow can determined by the total flow from the src node in the returned graph.
	 */
	public static <V> Graph<V, Long> MaximumFlowFordFulkerson(
			Graph<V, Long> g, Graph.Node<V, Long> src, Graph.Node<V, Long> dest) {
		throw new UnsupportedOperationException();
		// TODO(chiaman): Implement this.
	}
}
