package sol;

import src.City;
import src.ITravelController;
import src.TravelCSVParser;
import src.Transport;
import src.TransportType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * The TravelController class implements the ITravelController<City, Transport>
 * interface. The class will calculate the route desired and use those for
 * the output of the model
 */
public class TravelController implements ITravelController<City, Transport> {

    private TravelGraph graph;

    /**
     * makes a TravelController object
     */
    public TravelController() {
    }

    /**
     * loads data into a IGraph
     * @param citiesFile    the filename of the cities csv
     * @param transportFile the filename of the transportations csv
     * @return String relaying if method was successful
     */
    @Override
    public String load(String citiesFile, String transportFile) {
        this.graph = new TravelGraph();
        TravelCSVParser parser = new TravelCSVParser();

        Function<Map<String, String>, Void> addVertex = map -> {
            this.graph.addVertex(new City(map.get("name")));
            return null;
        };

        try {
            // pass in string for CSV and function to create City (vertex) using city name
            parser.parseLocations(citiesFile, addVertex);
        } catch (IOException e) {
            return "Error parsing file: " + citiesFile;
        }

        Function<Map<String, String>, Void> addEdge = map -> {
            this.graph.addEdge(this.graph.getCity(map.get("origin")),
                    new Transport(
                            this.graph.getCity(map.get("origin")),
                            this.graph.getCity(map.get("destination")),
                            TransportType.fromString(map.get("type")),
                            Double.parseDouble(map.get("price")),
                            Double.parseDouble(map.get("duration")))
            );
            return null;
        };



        try {
            // pass in string for CSV and function to create City (vertex) using city name
            parser.parseTransportation(transportFile, addEdge);
        } catch (IOException e) {
            return "Error parsing file: " + transportFile;
        }

        return "Successfully loaded cities and transportation files.";
    }

    /**
     * calculates the fastest Route from the source to the destination
     * in the graph
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the list representing the fastest edges to get from the source
     * to the destination
     */
    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        Function<Transport, Double> getEdgeWeight = t -> {
            return t.getMinutes();
        };
        Dijkstra<City, Transport> dijkstra = new Dijkstra<City, Transport>();
        return dijkstra.getShortestPath(this.graph, this.graph.getCity(source),
                this.graph.getCity(destination), getEdgeWeight);
    }

    /**
     * calculates the cheapest Route from the source to the destination
     * in the graph
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the list representing the cheapest edges to get from the
     * source to the destination
     */
    @Override
    public List<Transport> cheapestRoute(String source, String destination) {
        Function<Transport, Double> getEdgeWeight = t -> {
            return t.getPrice();
        };
        Dijkstra<City, Transport> dijkstra = new Dijkstra<City, Transport>();
        return dijkstra.getShortestPath(this.graph, this.graph.getCity(source),
                this.graph.getCity(destination), getEdgeWeight);
    }

    /**
     * calculates the most direct Route from the source to the destination
     * in the graph
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the list representing the most direct (least amount of)
     * edges to get from the source to the destination
     */
    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        BFS<City, Transport> bfs = new BFS<City, Transport>();
        return bfs.getPath(this.graph, this.graph.getCity(source),
                this.graph.getCity(destination));
    }
}
