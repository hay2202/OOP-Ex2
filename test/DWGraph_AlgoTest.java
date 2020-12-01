import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    private directed_weighted_graph g;
    private dw_graph_algorithms g2;
    static long start,end;


    @BeforeAll
    static void setStart(){
        start= new Date().getTime();
    }

    @AfterAll
    static void setEnd(){
        end= new Date().getTime();
        double dt =(end-start)/1000.0;
        System.out.println("Run time: "+dt+" seconds");
    }

//    public static void graph_creator(int v_size, int e_size, directed_weighted_graph gr) {
//        for(int i=0;i<v_size;i++) {
//            gr.addNode();
//        }
//        while(gr.edgeSize() < e_size) {
//            int i = (int) (Math.random()*v_size);
//            int j = (int) (Math.random()*v_size);
//            double w = (Math.random()*10)+1;
//            gr.connect(i,j,w);
//        }
//    }

    /**
     * graph creator of 10 nodes 15 edges.
     */
    @BeforeEach
        void Build() {
        g =new DWGraph_DS();
        for (int i=0;i<10;i++){
            node_data n = new Node(i);
            g.addNode(n);
        }
        g.connect(0,1,1);
        g.connect(0,8,2);
        g.connect(9,0,1);
        g.connect(1,5,25);
        g.connect(1,4,3);
        g.connect(4,3,1);
        g.connect(2,4,1.5);
        g.connect(3,2,0.5);
        g.connect(7,6,1);
        g.connect(6,7,1);
        g.connect(5,7,4);
        g.connect(8,5,3);
        g.connect(2,1,5);
        g.connect(7,8,2.5);
        g.connect(5,9,1.5);
        g2=new DWGraph_Algo();
        g2.init(g);
    }


    /**
     *  check copy of a graph
     */
    @Test
    void copy() {
        directed_weighted_graph d = g2.copy();
        edge_data w1=d.getEdge(1,4);
        edge_data w2=g.getEdge(1,4);
        assertEquals(g.nodeSize(), d.nodeSize());
        assertEquals(g.edgeSize(), d.edgeSize());
        assertEquals(g,d);
        assertEquals(w1.getWeight(),w2.getWeight());
    }


    /**
     * after changing origin graph check if the copied graph
     * doesn't changed.
     * if the copy was deep.
     */
    @Test
    void copy2() {
        directed_weighted_graph d = g2.copy();
        g.removeEdge(8,5);
        g.removeNode(0);
        assertEquals(11,g.edgeSize());
        assertEquals(15,d.edgeSize());
        assertEquals(9,g.nodeSize());
        assertEquals(10,d.nodeSize());
        assertNotEquals(g,d);
    }

    /**
     * first the graph is connected
     * then removing a node and make the graph unconnected.
     */
    @Test
    void isConnected() {
        assertTrue(g2.isConnected());
        g.removeEdge(7,6);
        assertFalse(g2.isConnected());
    }

    /**
     * find the shortest path between:
     * 1. vertex to his self.
     * 2. node 9 to node 5
     * 3. find better path after changing weight in edge
     * 4. no path exist.
     */
    @Test
    void shortestPathDist() {
        assertEquals(0,g2.shortestPathDist(9,9));           // #1
        assertEquals(6,g2.shortestPathDist(9,5));            //#2
        g.connect(1,5,1);
        assertEquals(3,g2.shortestPathDist(9,5));           // #3
        g.removeEdge(7,6);
        assertEquals(-1,g2.shortestPathDist(4,6));          //#4
    }

    /**
     * find the shortest path between:
     * 1. vertex to his self.
     * 2. node 0 to node 6
     * 3. find better path after changing weight in edge
     * 4. no path exist.
     */
    @Test
    void shortestPath() {
        List<node_data> p1 =g2.shortestPath(2,2);
        List<node_data> ans =  new ArrayList<>();

        ans.add(g.getNode(2));                              // #1
        assertEquals(ans.toString(),p1.toString());
        ans.clear();

        p1=g2.shortestPath(0,6);
        ans.add(g.getNode(0));
        ans.add(g.getNode(8));
        ans.add(g.getNode(5));                              //#2
        ans.add(g.getNode(7));
        ans.add(g.getNode(6));
        assertEquals(ans.toString(),p1.toString());
        ans.clear();

        g.connect(1,5,1);
        p1=g2.shortestPath(0,6);
        ans.add(g.getNode(0));
        ans.add(g.getNode(1));
        ans.add(g.getNode(5));                           // #3
        ans.add(g.getNode(7));
        ans.add(g.getNode(6));
        assertEquals(ans.toString(),p1.toString());

        g.removeEdge(7,6);
        p1=g2.shortestPath(0,6);                         //#4
        assertNull(p1);
    }
    /**
     * saving and loading graph and compare them
     */
    @Test
    void saveLoad() {
        assertTrue(g2.save("graph_test.json"));
        assertTrue(g2.load("graph_test.json"));
        directed_weighted_graph loadGraph =g2.getGraph();
        assertEquals(g,loadGraph);
        int rnd = (int) (Math.random()*10);
        g.removeNode(rnd);
        assertNotEquals(g,loadGraph);
    }


}