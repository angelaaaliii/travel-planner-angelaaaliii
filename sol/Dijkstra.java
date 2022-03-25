package sol;

import src.IDijkstra;
import src.IGraph;

import java.util.*;
import java.util.function.Function;

/**
 * The Dijkstra class implements the IDijkstra<V, E> interface.
 * It uses the Dijkstra algorithm that calculates the best route base on
 * either the lowest price or lowest time of travel
 * @param <V> represents a vertex in the graph
 * @param <E> represents an edge in the graph
 */

public class Dijkstra<V, E> implements IDijkstra<V, E> {

    /**
     * gets the path from start to end using the inputted graph and the
     * corresponding weight
     *
     * @param graph       the graph including the vertices
     * @param source      the source vertex
     * @param destination the destination vertex
     * @param edgeWeight weight to prioritize
     * @return a List<E> representing the path from start to end in graph
     */
    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {

        HashMap<V, Double> cameFrom = new HashMap<V, Double>();
        for (V v :  graph.getVertices()) {
            cameFrom.put(v, Double.MAX_VALUE);
        }
        cameFrom.put(source, 0.0);

        Comparator<V> byVertex = (V1, V2) -> {
            return Double.compare(cameFrom.get(V1), cameFrom.get(V2));
        };
        PriorityQueue<V> toCheckQueue = new PriorityQueue<V>(byVertex);
        for (V v :  graph.getVertices()) {
            toCheckQueue.add(v);
        }

        HashMap<V, V> routes = new HashMap<V, V>();

        while (!toCheckQueue.isEmpty()) {
            V checkingV = toCheckQueue.remove();
            for (E edge : graph.getOutgoingEdges(checkingV)) {
                V neighbor = graph.getEdgeTarget(edge);
                if (cameFrom.get(checkingV) + edgeWeight.apply(edge)
                        < cameFrom.get(neighbor)) {
                    cameFrom.put(neighbor,
                            cameFrom.get(checkingV) + edgeWeight.apply(edge));
                    routes.put(neighbor, checkingV);
                    toCheckQueue.remove(neighbor);
                    toCheckQueue.add(neighbor);
                }
            }
        }
        ArrayList<E> lst = new ArrayList<E>();
        if (!routes.keySet().contains(destination)) {
            return new ArrayList<E>();
        }
        else {
            return this.backtrack(graph, routes, source, destination, lst,
                    edgeWeight);
        }

    }

    /**
     * A helper method which compiles the list representing the path in getPath
     * @param graph the graph including the vertices
     * @param routes a hashmap containing city and the route that can be used
     * @param source the source vertex
     * @param destination the destination vertex
     * @param currentRoute the current accumulated route
     * @param edgeWeight the weight which the algorithm cares about
     * @return the final path from start to end
     */
    private List<E> backtrack(IGraph<V, E> graph, HashMap<V, V> routes,
                              V source, V destination,
                              ArrayList<E> currentRoute,
                              Function<E, Double> edgeWeight) {
        Comparator<E> byEdge = (E1, E2) -> {
            return Double.compare(edgeWeight.apply(E1), edgeWeight.apply(E2));
        };
        PriorityQueue<E> queue = new PriorityQueue<E>(byEdge);

        V v = routes.get(destination);
        if (source.equals(destination)) {
            return currentRoute;
        }
        for (E e : graph.getOutgoingEdges(v)) {
            if (graph.getEdgeTarget(e).equals(destination)) {
                queue.add(e);
            }
        }

        currentRoute.add(0, queue.remove());

        return this.backtrack(graph, routes, source, v, currentRoute,
                edgeWeight);
    }
}
