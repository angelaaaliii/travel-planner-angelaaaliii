package test;

import org.junit.Test;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTest {
    private SimpleGraph graph;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;

    private SimpleEdge edgeAB;
    private SimpleEdge edgeBC;
    private SimpleEdge edgeCA;
    private SimpleEdge edgeAC;

    private TravelGraph graph1;
    private City franklin;
    private City atlanta;
    private City nashville;
    private Transport edgeFA;
    private Transport edgeFN;
    private Transport edgeAF;
    private Transport edgeNF;


    /**
     * Creates a simple graph.
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("A");
        this.b = new SimpleVertex("B");
        this.c = new SimpleVertex("C");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);

        // create and insert edges
        this.edgeAB = new SimpleEdge(1, this.a, this.b);
        this.edgeBC = new SimpleEdge(1, this.b, this.c);
        this.edgeCA = new SimpleEdge(1, this.c, this.a);
        this.edgeAC = new SimpleEdge(1, this.a, this.c);

        this.graph.addEdge(this.a, this.edgeAB);
        this.graph.addEdge(this.b, this.edgeBC);
        this.graph.addEdge(this.c, this.edgeCA);
        this.graph.addEdge(this.a, this.edgeAC);
    }

    @Test
    public void testGetVertices() {
        this.createSimpleGraph();
        assertEquals(this.graph.getVertices().size(), 3);
        assertTrue(this.graph.getVertices().contains(this.a));
        assertTrue(this.graph.getVertices().contains(this.b));
        assertTrue(this.graph.getVertices().contains(this.c));
    }

    private void createGraph() {
        this.graph1 = new TravelGraph();
        this.franklin = new City("Franklin");
        this.graph1.addVertex(this.franklin);
        this.atlanta = new City("Atlanta");
        this.graph1.addVertex(this.atlanta);
        this.nashville = new City("Nashville");
        this.graph1.addVertex(this.nashville);

        this.edgeFA = new Transport(this.franklin, this.atlanta,
                TransportType.BUS, 15.0, 300.0);
        this.graph1.addEdge(this.franklin, this.edgeFA);
        this.edgeFN = new Transport(this.franklin, this.nashville,
                TransportType.BUS, 10.0, 30.0);
        this.graph1.addEdge(this.franklin, this.edgeFN);
        this.edgeNF = new Transport(this.nashville, this.franklin,
                TransportType.PLANE, 100.0, 3.0);
        this.graph1.addEdge(this.nashville, this.edgeNF);
        this.edgeAF = new Transport(this.atlanta, this.franklin,
                TransportType.TRAIN, 30.0, 200.0);
        this.graph1.addEdge(this.atlanta, this.edgeAF);
    }

    //get vertices on graph
    @Test
    public void testGetVertices1() {
        this.createGraph();
        assertEquals(this.graph1.getVertices().size(), 3);
        assertTrue(this.graph1.getVertices().contains(this.franklin));
        assertTrue(this.graph1.getVertices().contains(this.atlanta));
        assertTrue(this.graph1.getVertices().contains(this.nashville));
    }

    //get vertices on empty graph
    @Test
    public void testGetVertices2() {
        TravelGraph tg = new TravelGraph();
        assertEquals(tg.getVertices().size(), 0);
    }

    //get edgesource on graph
    @Test
    public void testGetEdgeSource1() {
        this.createGraph();
        assertEquals(this.graph1.getEdgeSource(this.edgeFA).toString(),
                "Franklin");
        assertEquals(this.graph1.getEdgeSource(this.edgeAF).toString(),
                "Atlanta");
        assertEquals(this.graph1.getEdgeSource(this.edgeNF).toString(),
                "Nashville");
        assertEquals(this.graph1.getEdgeSource(this.edgeFN).toString(),
                "Franklin");
    }

    //get edge target on graph
    @Test
    public void testGetEdgeTarget() {
        this.createGraph();
        assertEquals(this.graph1.getEdgeTarget(this.edgeFA).toString(),
                "Atlanta");
        assertEquals(this.graph1.getEdgeTarget(this.edgeAF).toString(),
                "Franklin");
        assertEquals(this.graph1.getEdgeTarget(this.edgeNF).toString(),
                "Franklin");
        assertEquals(this.graph1.getEdgeTarget(this.edgeFN).toString(),
                "Nashville");
    }

    //get outgoing edges on graph
    @Test
    public void testGetOutgoingEdges() {
        this.createGraph();
        assertEquals(this.graph1.getOutgoingEdges(this.franklin).size(), 2);
        assertEquals(true, this.graph1.
                getOutgoingEdges(this.atlanta).contains(this.edgeAF));
        assertEquals(true, this.graph1.
                getOutgoingEdges(this.nashville).contains(this.edgeNF));
    }

    //get outgoing edges on graph
    @Test
    public void testGetOutgoingEdges2() {
        TravelGraph tg = new TravelGraph();
        City c = new City("hi");
        tg.addVertex(c);
        assertEquals(tg.getOutgoingEdges(c).size(), 0);
    }

    //get city on graph and city
    @Test
    public void testGetCity() {
        this.createGraph();
        assertEquals(this.graph1.getCity("Franklin"), this.franklin);
        assertEquals(this.graph1.getCity("Atlanta"), this.atlanta);
        assertEquals(this.graph1.getCity("Nashville"), this.nashville);
    }

    //addVertex on same city twice to empty graph
    @Test
    public void testAddVertex1() {
        TravelGraph tg = new TravelGraph();
        City c1 = new City("c1");
        tg.addVertex(c1);
        tg.addVertex(c1);
        assertEquals(tg.getVertices().size(), 1);
    }

    //addVertex on two cities with same name on empty graph
    @Test
    public void testAddVertex2() {
        TravelGraph tg = new TravelGraph();
        City c1 = new City("c1");
        City c2 = new City("c1");
        tg.addVertex(c1);
        tg.addVertex(c2);
        assertEquals(tg.getVertices().size(), 1);
        assertEquals(tg.getVertices().contains(c2), true);
    }

    //addEdge twice on same edge
    @Test
    public void testAddEdge3() {
        TravelGraph tg = new TravelGraph();
        City a = new City("alberta");
        City d = new City("dc");
        tg.addVertex(a);
        tg.addVertex(d);
        Transport edge = new Transport(a, d, TransportType.PLANE, 50.0, 120.0);
        tg.addEdge(a, edge);
        tg.addEdge(a, edge);
        assertEquals(tg.getOutgoingEdges(a).size(), 1);
        assertEquals(tg.getOutgoingEdges(a).contains(edge), true);
    }

    //addEdge where another different edge between same cities exists
    @Test
    public void testAddEdge4() {
        TravelGraph tg = new TravelGraph();
        City a = new City("alberta");
        City d = new City("dc");
        tg.addVertex(a);
        tg.addVertex(d);
        Transport edge = new Transport(a, d, TransportType.PLANE, 50.0, 120.0);
        tg.addEdge(a, edge);
        Transport edge2 = new Transport(a, d, TransportType.TRAIN,
                50.0, 120.0);
        tg.addEdge(a, edge2);
        assertEquals(tg.getOutgoingEdges(a).size(), 2);
        assertEquals(tg.getOutgoingEdges(a).contains(edge), true);
        assertEquals(tg.getOutgoingEdges(a).contains(edge2), true);
    }

    // testing exception for getCity when city name is not in graph
    @Test (expected = IllegalArgumentException.class)
    public void testGetCity1()  {
        TravelGraph tg = new TravelGraph();
        City a = new City("alberta");
        tg.getCity("alberta");
    }

}
