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

public class Graphs {

	public static <V, E> List<Graph.Edge<V, E>> Dijkstra(
			Graph<V, E> g, Graph.Node<V, E> src, Graph.Node<V, E> dest, Function<E, Long> length) {
		// state: false = visiting, true = visited, does not containKey = unvisited.
		Map<Graph.Node<V, E>, Boolean> state = new HashMap<Graph.Node<V, E>, Boolean>();
		Map<Graph.Node<V, E>, Graph.Node<V, E>> parent = new HashMap<Graph.Node<V, E>, Graph.Node<V, E>>();
		// Estimated distance from the src node to a certain node.
		final Map<Graph.Node<V, E>, Long> estimatedDist = new HashMap<Graph.Node<V, E>, Long>();
		Queue<Graph.Node<V, E>> toVisit = new PriorityQueue<Graph.Node<V, E>>(
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
		List<Graph.Edge<V, E>> result = new ArrayList<Graph.Edge<V, E>>();
		while (parent.containsKey(currentNode)) {
			Graph.Node<V, E> parentNode = parent.get(currentNode);
			result.add(g.getEdge(parentNode, currentNode));
		}
		Collections.reverse(result);
		return result;
	}
}
