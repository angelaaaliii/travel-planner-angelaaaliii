package test;

import org.junit.Test;
import sol.Dijkstra;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.IDijkstra;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DijkstraTest {

    private static final double DELTA = 0.001;

    private SimpleGraph graph;
    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;

    /**
     * Creates a simple graph.
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a, new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c, new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c, new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d, new SimpleEdge(5, this.e, this.d));
    }

    private void createCityGraph(String citiesFile, String transportFile) {
        TravelController t = new TravelController();
        t.load(citiesFile, transportFile);
    }

    @Test
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
                dijkstra.getShortestPath(this.graph, this.a, this.b,
                        edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        path = dijkstra.getShortestPath(this.graph, this.c, this.b,
                edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(2, path.size());
    }

    // cheapest from philly to nyc
    @Test
    public void testDijkstra() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.cheapestRoute("Philly", "NYC");
        assertEquals(2, path.size());
    }

    // cheapest from nyc to boston
    @Test
    public void testDijkstra1() {
        TravelController t = new TravelController();
        t.load("data/ourCities2.csv", "data/ourTransport2.csv");
        List<Transport> path = t.cheapestRoute("NYC", "Boston");
        assertEquals(path.size(), 2);
        assertEquals(path.get(0).getTarget().toString(), "Providence");
        assertEquals(path.get(1).getTarget().toString(), "Boston");
        assertEquals(path.get(0).getPrice() + path.get(1).getPrice(),
                50.0, 0.01);
    }

    // fastest from boston to providence
    @Test
    public void testDijkstra2() {
        TravelController t = new TravelController();
        t.load("data/ourCities2.csv", "data/ourTransport2.csv");
        List<Transport> path =
                t.fastestRoute("Boston", "Providence");
        assertEquals(path.size(), 2);
        assertEquals(path.get(1).getTarget().toString(), "Providence");
        assertEquals(path.get(0).getTarget().toString(), "NYC");
        assertEquals(path.get(0).getMinutes() + path.get(1).getMinutes(),
                240.0, 0.01);
    }

    // cheapest from philly to nyc
    @Test
    public void testDijkstra3() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.cheapestRoute("Philly", "NYC");
        assertEquals(path.size(), 2);
        assertEquals(path.get(1).getTarget().toString(), "NYC");
        assertEquals(path.get(0).getTarget().toString(), "Trenton");
        assertEquals(path.get(0).getPrice() + path.get(1).getPrice(),
                20.0, 0.01);
    }

    // cheapest route to itself
    @Test
    public void testDijkstra4() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.cheapestRoute("Philly", "Philly");
        assertEquals(path.size(), 0);
    }

    // cheapest route to itself
    @Test
    public void testDijkstra5() {
        TravelController t = new TravelController();
        t.load("data/ourCities2.csv", "data/ourTransport2.csv");
        List<Transport> path = t.cheapestRoute("Boston", "Philly");
        assertEquals(path.size(), 0);
    }

    // check getShortestPath
    @Test
    public void testDijkstra6() {
        TravelGraph graph1 = new TravelGraph();
        City franklin = new City("Franklin");
        graph1.addVertex(franklin);
        City atlanta = new City("Atlanta");
        graph1.addVertex(atlanta);
        City nashville = new City("Nashville");
        graph1.addVertex(nashville);

        Transport edgeFA = new Transport(franklin, atlanta, TransportType.BUS,
                15.0, 300.0);
        graph1.addEdge(franklin, edgeFA);
        Transport edgeFN = new Transport(franklin, nashville, TransportType.BUS,
                10.0, 30.0);
        graph1.addEdge(franklin, edgeFN);
        Transport edgeNF = new Transport(nashville, franklin, TransportType.PLANE,
                100.0, 3.0);
        graph1.addEdge(nashville, edgeNF);
        Transport edgeAF = new Transport(atlanta, franklin, TransportType.TRAIN,
                30.0, 200.0);
        graph1.addEdge(atlanta, edgeAF);

        Function<Transport, Double> getEdgeWeight = t -> {
            return t.getMinutes();
        };

        Dijkstra<City, Transport> d = new Dijkstra<City, Transport>();

        List<Transport> path =
                d.getShortestPath(graph1, atlanta, nashville, getEdgeWeight);
        assertEquals(path.size(), 2);
        assertEquals(path.contains(edgeAF), true);
        assertEquals(path.contains(edgeFN), true);
    }

    // dijkstra city to a city name that does not exist
    @Test(expected = IllegalArgumentException.class)
    public void testBFS9() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.fastestRoute("Philly", "Phill");
    }

    // dijkstra city to a city with no path
    @Test
    public void testBFS10() {
        TravelController t = new TravelController();
        t.load("data/ourCities3.csv", "data/ourTransport3.csv");
        List<Transport> path = t.fastestRoute("a", "f");
        assertEquals(path.size(), 0);
    }

    // dijkstra city to itself
    @Test
    public void testBFS11() {
        TravelController t = new TravelController();
        t.load("data/ourCities3.csv", "data/ourTransport3.csv");
        List<Transport> path = t.fastestRoute("a", "a");
        assertEquals(path.size(), 0);
    }
}
