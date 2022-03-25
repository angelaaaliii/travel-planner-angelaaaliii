package test;

import org.junit.Test;
import sol.BFS;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;
    private SimpleVertex f;
    private SimpleGraph graph;


    /**
     * Creates a simple graph.
     */
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);

        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b, new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c, new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f, new SimpleEdge(100, this.f, this.e));
    }

    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        List<SimpleEdge> path = bfs.getPath(this.graph, this.a, this.e);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path), 200.0, DELTA);
        assertEquals(path.size(), 2);
    }

    // bfs source to source
    @Test
    public void testBFS1() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.mostDirectRoute("Philly", "Philly");
        assertEquals(path.size(), 0);
    }

    // bfs most direct nyc to boston
    @Test
    public void testBFS2() {
        TravelController t = new TravelController();
        t.load("data/ourCities2.csv", "data/ourTransport2.csv");
        List<Transport> path = t.mostDirectRoute("NYC", "Boston");
        assertEquals(path.size(), 1);
        assertEquals(path.get(0).getTarget().toString(), "Boston");
    }

    // bfs most direct nyc to philly - two same routes
    @Test
    public void testBFS3() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.mostDirectRoute("NYC", "Philly");
        assertEquals(path.size(), 2);
    }

    // bfs most direct trenton to dc
    @Test
    public void testBFS4() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.mostDirectRoute("Trenton", "DC");
        assertEquals(path.size(), 2);
    }

    // bfs most direct dc to trenton
    @Test
    public void testBFS5() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.mostDirectRoute("DC", "Trenton");
        assertEquals(path.size(), 2);
    }

    // bfs most direct complex
    @Test
    public void testBFS6() {
        TravelController t = new TravelController();
        t.load("data/ourCities3.csv", "data/ourTransport3.csv");
        List<Transport> path = t.mostDirectRoute("a", "d");
        assertEquals(path.size(), 2);
    }

    // bfs most direct complex 2
    @Test
    public void testBFS7() {
        TravelController t = new TravelController();
        t.load("data/ourCities3.csv", "data/ourTransport3.csv");
        List<Transport> path = t.mostDirectRoute("a", "b");
        assertEquals(path.size(), 1);
    }

    // bfs most direct complex 2
    @Test
    public void testBFS8() {
        TravelController t = new TravelController();
        t.load("data/ourCities3.csv", "data/ourTransport3.csv");
        List<Transport> path = t.mostDirectRoute("a", "e");
        assertEquals(path.size(), 3);
    }

    // bfs city to a city name that does not exist
    @Test (expected = IllegalArgumentException.class)
    public void testBFS9() {
        TravelController t = new TravelController();
        t.load("data/ourCities.csv", "data/ourTransport.csv");
        List<Transport> path = t.mostDirectRoute("Philly", "Phill");
    }

    // bfs city to a city with no path
    @Test
    public void testBFS10() {
        TravelController t = new TravelController();
        t.load("data/ourCities3.csv", "data/ourTransport3.csv");
        List<Transport> path = t.mostDirectRoute("a", "f");
        assertEquals(path.size(), 0);
    }

    // check BFS
    @Test
    public void testBFS12() {
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
        Transport edgeFN =
                new Transport(franklin, nashville, TransportType.BUS,
                        10.0, 30.0);
        graph1.addEdge(franklin, edgeFN);
        Transport edgeNF =
                new Transport(nashville, franklin, TransportType.PLANE,
                        100.0, 3.0);
        graph1.addEdge(nashville, edgeNF);
        Transport edgeAF = new Transport(atlanta, franklin, TransportType.TRAIN,
                30.0, 200.0);
        graph1.addEdge(atlanta, edgeAF);


        BFS<City, Transport> b = new BFS<City, Transport>();

        List<Transport> path = b.getPath(graph1, atlanta, nashville);
        assertEquals(path.size(), 2);
        assertEquals(path.contains(edgeAF), true);
        assertEquals(path.contains(edgeFN), true);
    }
}
