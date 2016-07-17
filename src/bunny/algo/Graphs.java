package bunny.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

import bunny.structure.Graph;
import bunny.structure.Graph.Edge;
import bunny.structure.Graph.Node;
import bunny.structure.UnionFind;
import bunny.util.Ordering;

public class Graphs {

	public static <V, E> List<Graph.Edge<V, E>> Dijkstra(
			Graph<V, E> g, Graph.Node<V, E> src, Graph.Node<V, E> dest, Function<E, Long> length) {
		// state: false = visiting, true = visited, does not containKey = unvisited.
		Map<Graph.Node<V, E>, Boolean> state = new HashMap<>();
		Map<Graph.Node<V, E>, Graph.Node<V, E>> parent = new HashMap<>();
		// Estimated distance from the src node to a certain node.
		final Map<Graph.Node<V, E>, Long> estimatedDist = new HashMap<>();
		Queue<Graph.Node<V, E>> toVisit = new PriorityQueue<>(
				new Comparator<Node<V, E>>() {
					@Override
					public int compare(Node<V, E> n1, Node<V, E> n2) {
						return estimatedDist.get(n1).compareTo(estimatedDist.get(n2));
					}
				});
		state.put(src, false);
		estimatedDist.put(src, (long) 0);
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
						long newEstimatedDist = length.apply(adjacentEdge.getValue()) +  estimatedDist.get(currentNode);
						if (newEstimatedDist < estimatedDist.get(adjacentNode)) {
							parent.put(adjacentNode, currentNode);
							estimatedDist.put(adjacentNode, newEstimatedDist);
						}
					}
				} else {
					// state = unvisitied
					state.put(adjacentNode, false);
					parent.put(adjacentNode, currentNode);
					estimatedDist.put(adjacentNode, length.apply(adjacentEdge.getValue()) +  estimatedDist.get(currentNode));
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
	
	public static <V, E extends Comparable<? super E>> Graph<V, E> mininumSpanningTreeKruskal(Graph<V, E> g) {
		Comparator<? super E> c = Ordering.natural();
		return minimumSpanningTreeKruskal(g, c);
	}
	
	/**
	 * Returns a minimum spanning tree determined by Kruskal's algorithm.
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
				newGraph.createEdge(edge.getValue(), newNodeByOldNode.get(edge.from()), newNodeByOldNode.get(edge.to()));
				count++;
			}
			if (count == g.getNodes().size() - 1) {
				break;
			}
		}
		return newGraph;
	}
}
