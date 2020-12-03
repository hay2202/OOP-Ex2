import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    directed_weighted_graph g;

    /**
     * graph creator of 10 nodes 12 edges.
     */
    @BeforeEach
    void testBuild() {
        g =new DWGraph_DS();
        for (int i=0;i<10;i++){
            node_data n = new Node(i);
            g.addNode(n);
        }
        g.connect(0,1,1);
        g.connect(0,8,2);
        g.connect(9,0,1);
        g.connect(1,5,2.5);
        g.connect(1,4,3);
        g.connect(4,3,1);
        g.connect(2,4,1.5);
        g.connect(3,2,0.5);
        g.connect(7,6,1);
        g.connect(6,7,1);
        g.connect(5,7,4);
        g.connect(8,5,3);
    }

    @Test
    void testGetNode() {
        int key=(int)(Math.random()*10);
        node_data n = g.getNode(key);
        assertEquals(n.getKey(), key);
        assertNull(g.getNode(-1));
    }

    @Test
    void testGetEdge() {
        edge_data e = g.getEdge(2,4);
        assertEquals(1.5,e.getWeight());
        edge_data e1 = g.getEdge(4,2);      //not exist
        assertNull(e1);
        edge_data e3 = g.getEdge(2,2);      //edge to itself
        assertNull(e3);
    }

    @Test
    void addNode() {
        node_data n1 = g.getNode(5);
        node_data n2 = new Node(10);
        node_data n3 = new Node(12);
        g.addNode(n1);      //add exist node
        g.addNode(n2);      //add twice the same node
        g.addNode(n2);
        g.addNode(n3);
        assertEquals(12, g.nodeSize());
    }

    @Test
    void connect() {
        g.connect(1,2,1);       //add new edge
        g.connect(1,2,3);       //update edge weight
        g.connect(5,5,1);       //edge to itself SHOULD NOT add
        g.connect(8,5,3);       //exist edge
        assertEquals(13, g.edgeSize());
        assertEquals(3, g.getEdge(1,2).getWeight());
    }

    @Test
    void testGetV() {
        Collection<node_data> v = g.getV();
        assertEquals(10,v.size());
    }

    @Test
    void testGetE() {
        Collection<edge_data> e = g.getE(1);
        assertEquals(2,e.size());
    }

    @Test
    void removeNode() {
        g.removeNode(7);
        g.removeNode(7);
        g.removeNode(20);
        assertEquals(9, g.nodeSize());
        assertEquals(9, g.edgeSize());
    }

    @Test
    void removeEdge() {
        g.removeEdge(0,1);
        g.removeEdge(0,1);
        g.removeEdge(0,0);
        g.removeEdge(0,8);
        g.removeEdge(7,6);
        assertEquals(9, g.edgeSize());
    }
}