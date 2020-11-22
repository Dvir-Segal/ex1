package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    private static Random _rnd = new Random(1);
    /**
     * Build graph with 15 nodes, connect 7 edges, and remove 2 of them.
     * We will get graph with 15 nodes, 4 edges and 23 changes (15+7+2)
     * @return weighted_graph
     */
    static WGraph_DS Build15() {
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 15; i++) {
            g.addNode(i);
        }
        g.connect(0, 9, 2.5);
        g.connect(1, 9, 6.7);
        g.connect(4, 9, 40);
        g.connect(3, 10, 7);
        g.connect(10,3,7);
        g.connect(14, 5, 21);
        g.connect(11, 2, 17);

        g.removeEdge(14, 5);
        g.removeEdge(4, 15);//this edge is not exist
        g.removeEdge(1, 9);
        return g;
    }
    /**
     * Build graph with 1,000,000 nodes, connect 10,000,000 edges.
     * @return weighted_graph
     */
    static WGraph_DS Build1m() {
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 1000*1000; i++) {
            g.addNode(i);
        }
        int[] vertexes =  nodesArray(g);
        while(g.edgeSize() < 10*1000*1000){
            int a = nextRnd(0,1000*1000);
            int b = nextRnd(0,1000*1000);
            int i = vertexes[a];
            int j = vertexes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
    private static int[] nodesArray(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] v = new node_info[size];
        V.toArray(v);
        int[] array = new int[size];
        for(int i=0;i<size;i++) {array[i] = v[i].getKey();}
        Arrays.sort(array);
        return array;
    }
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Test for copy methode on original Build15, initialization of counterChanges
     */
    @Test
    void testCopy() {
        WGraph_DS copy = new WGraph_DS(Build15());
        assertEquals(15, copy.nodeSize());
        assertNotEquals(37, copy.nodeSize());
        assertEquals(4, copy.edgeSize());
        assertNotEquals(59, copy.edgeSize());
        assertEquals(0, copy.getMC());
        assertNotEquals(27, copy.getMC());
    }
    /**
     * Test for hasEdge methode, for real edges and for nodes without edge between of them
     */
    @Test
    void testHasEdge() {
        assertTrue(Build15().hasEdge(11, 2));
        assertTrue(Build15().hasEdge(2, 11));
        assertTrue(Build15().hasEdge(9, 0));
        assertTrue(Build15().hasEdge(0, 9));
        assertTrue(Build15().hasEdge(9, 4));
        assertTrue(Build15().hasEdge(4, 9));
        assertTrue(Build15().hasEdge(9, 0));
        assertTrue(Build15().hasEdge(3, 10));
        assertTrue(Build15().hasEdge(10, 3));

        assertFalse(Build15().hasEdge(6, 7));
        assertFalse(Build15().hasEdge(12, 8));
        assertFalse(Build15().hasEdge(0, 12));
        assertFalse(Build15().hasEdge(5, 4));
        assertFalse(Build15().hasEdge(7, 6));
        assertFalse(Build15().hasEdge(8, 12));
        assertFalse(Build15().hasEdge(12, 0));
        assertFalse(Build15().hasEdge(5, 4));
        assertFalse(Build15().hasEdge(6, 6));

        assertFalse(Build15().hasEdge(14, 5));
        assertFalse(Build15().hasEdge(5, 14));
        assertFalse(Build15().hasEdge(1, 9));
        assertFalse(Build15().hasEdge(9, 1));
        assertFalse(Build15().hasEdge(1, 1));
        assertFalse(Build15().hasEdge(24, 1));
    }
    /**
     * Test for nodeSize methode
     */
    @Test
    void testNodeSize() {
        assertEquals(15, Build15().nodeSize());
        assertNotEquals(101, Build15().nodeSize());
    }
    /**
     * Test for edgeSize methode
     */
    @Test
    void testEdgeSize() {
        assertEquals(4, Build15().edgeSize());
        assertNotEquals(91, Build15().edgeSize());
    }
    /**
     * Test for number of changes
     */
    @Test
    void testGetMC() {
        assertEquals(23, Build15().getMC());
        assertNotEquals(20, Build15().getMC());
    }
    /**
     * In original Build15: remove node 9, whose has 2 edges (3 changes).
     * We will get graph with 14 nodes, 2 edges and 26 changes (15+7+2)
     */
    @Test
    void testRemoveNode() {
        WGraph_DS g = Build15();
        g.removeNode(9);
        assertEquals(14, g.nodeSize());
        assertNotEquals(17, g.nodeSize());
        assertEquals(2, g.edgeSize());
        assertNotEquals(10, g.edgeSize());
        assertEquals(26, g.getMC());
        assertNotEquals(27, g.getMC());
    }
    /**
     * In original Build15: create 1 real edge, update weight for (4,9) and create fake edge
     * (actually, no change).
     * We will get graph with 15 nodes, 5 edges and 25 changes
     */
    @Test
    void testConnect() {
        WGraph_DS g = Build15();
        g.connect(10,13, 0);
        g.connect(0,1, -2);
        g.connect(4, 9, 1);
        assertEquals(1, g.getEdge(9,4));
        assertEquals(15, g.nodeSize());
        assertNotEquals(31, g.nodeSize());
        assertEquals(5, g.edgeSize());
        assertNotEquals(19, g.edgeSize());
        assertEquals(25, g.getMC());
        assertNotEquals(27, g.getMC());
    }
    /**
     * Test for getEdge methode
     */
    @Test
    void testGetEdge(){
        WGraph_DS g = Build15();
        assertEquals(40, g.getEdge(9,4));
        assertEquals(7, g.getEdge(10,3));
        assertEquals(7, g.getEdge(3,10));
        assertNotEquals(101, g.getEdge(4, 9));

    }
    /**
     * Create Build1m graph
     */
    @Test
    void checkGiantGraph(){
        WGraph_DS g = Build1m();
        System.out.println("Number of nodes "+g.nodeSize());
        System.out.println("Number of edges "+g.edgeSize());
    }
}
