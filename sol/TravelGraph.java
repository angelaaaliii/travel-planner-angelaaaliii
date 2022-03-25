package sol;

import src.City;
import src.IGraph;
import src.Transport;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

/**
 * The TravelGraph class implements IGraph<City, Transport> interface. It is
 * used to represent graphs, specifically with City vertices and Transport
 * edges.
 */
public class TravelGraph implements IGraph<City, Transport> {

    private HashMap<String, City> cities;

    /**
     * constructs a TravelGraph object
     */
    public TravelGraph() {
        this.cities = new HashMap<String, City>();
    }

    /**
     * adds a vertex to the graph (cities)
     * @param vertex the vertex
     */
    @Override
    public void addVertex(City vertex) {
        this.cities.put(vertex.toString(), vertex);
    }

    /**
     * adds an edge (transport) to the graph (cities)
     * @param origin the origin of the edge.
     * @param edge the edge to be added
     */
    @Override
    public void addEdge(City origin, Transport edge) {
        this.cities.get(origin.toString()).addOut(edge);
    }

    /**
     * gets the vertices from the graph
     * @return the vertices in a Set
     */
    @Override
    public Set<City> getVertices() {
        HashSet<City> c = new HashSet<City>();
        for (String s : this.cities.keySet()) {
            c.add(this.cities.get(s));
        }
        return c;
    }

    /**
     * gets the source of the inputted edge
     * @param edge the edge
     * @return the source city
     */
    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
    }

    /**
     * gets the target of the inputted edge
     * @param edge the edge
     * @return the destination city
     */
    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();
    }

    /**
     * gets the edges that come out of the inputted city
     * @param fromVertex the vertex
     * @return the set of outgoing edges
     */
    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        return fromVertex.getOutgoing();
    }

    /**
     * gets the City based on the inputted city name
     * @param name a String representing the name of the city
     * @return the City of the inputted String
     * @throws IllegalArgumentException if the String for the city name
     *                                  does not represent a City in the graph
     */
    public City getCity(String name) {
        if (this.cities.containsKey(name)) {
            return this.cities.get(name);
        }
        else {
            throw new IllegalArgumentException("City is not in the graph.");
        }
    }

}