package sol;


import src.IBFS;
import src.IGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * The BFS class implements the IBSF<V, E> interface. It uses the breadth
 * first search algorithm that will calculates the most direct route to the
 * destination from the source involving the least number of vertices
 * @param <V> represents a vertex in the graph
 * @param <E> represents an edge in the graph
 */
public class BFS<V, E> implements IBFS<V, E> {

    /**
     * gets the most direct path from start to end using the inputted graph
     * using Breadth-For-Search
     *
     * @param graph the graph including the vertices
     * @param start the start vertex
     * @param end   the end vertex
     * @return a List<E> representing the path from start to end in graph
     */
    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {

        ArrayList<E> toCheck = new ArrayList<E>();
        toCheck.addAll(graph.getOutgoingEdges(start));
        HashMap<V, E> routes = new HashMap<V, E>();
        ArrayList<E> visited = new ArrayList<E>();

        while (!toCheck.isEmpty()) {
            E checkingEdge = toCheck.remove(0);
            if (!routes.containsKey(graph.getEdgeTarget(checkingEdge))) {
            routes.put(graph.getEdgeTarget(checkingEdge), checkingEdge);}
            if (graph.getEdgeTarget(checkingEdge).equals(end)) {
                return this.backtrack(graph, routes, start, end,
                        new ArrayList<E>());
            }
            visited.add(checkingEdge);

            for (E edge :
                    graph.getOutgoingEdges(graph.getEdgeTarget(checkingEdge))){
                if (!visited.contains(edge)) {
                    toCheck.add(edge);
                }
            }
        }
        return new ArrayList<E>();

    }

    /**
     * A helper method which compiles the list representing the path in getPath
     * @param graph - the graph to use
     * @param routes - a hashmap containing city and the route that can be used
     * @param start - start of path
     * @param end - end of path
     * @param finalRoute - final path  from start to end
     *                   (recursively gets bigger)
     * @return the final path from start to end
     */
    private List<E> backtrack(IGraph<V, E> graph, HashMap<V, E> routes, V start,
                             V end, ArrayList<E> finalRoute) {

        if (!end.equals(start)) {
            E edge = routes.get(end);
            finalRoute.add(0, edge);
            return this.backtrack(graph, routes, start,
                    graph.getEdgeSource(edge),
                    finalRoute);
        }
        return finalRoute;

    }
}
