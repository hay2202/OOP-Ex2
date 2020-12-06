package api;

import java.util.*;


public class DWGraph_DS implements directed_weighted_graph {

    private int mc, numOfEdges, numOfNodes;
    private HashMap<Integer, node_data> Nodes;          //hashmap of all the nodes.
    private HashMap<Integer, HashMap<Integer, edge_data>> Edges;   //every node has hashmap of his going out edges.
    private HashMap<Integer, HashMap<Integer, Double>> destToSrc; // a map of incoming edges to the node .

    public DWGraph_DS() {
        mc = numOfEdges = numOfNodes = 0;
        Nodes = new HashMap<>();
        Edges = new HashMap<>();
        destToSrc = new HashMap<>();
    }

    /**
     * returns the api.node_data by the node_id,
     * @param key - the node_id
     * @return the api.node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (Nodes.containsKey(key))
            return Nodes.get(key);
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (Edges.containsKey(src)) {
            if (Edges.get(src).containsKey(dest)) {
                return Edges.get(src).get(dest);
            }
        }
        return null;
    }

    /**
     * adds a new node to the graph with the given api.node_data.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!Nodes.containsKey(n.getKey())) {
            HashMap<Integer, edge_data> edge = new HashMap<>();
            Edges.put(n.getKey(), edge);
            Nodes.put(n.getKey(), n);
            destToSrc.put(n.getKey(), new HashMap<Integer, Double>());
            numOfNodes++;
            mc++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (getNode(src) != null && getNode(dest) != null) {
            if (src != dest && getEdge(src, dest) == null) {
                edge_data edge = new Edge(src, dest, w);
                Edges.get(src).put(dest, edge);
                destToSrc.get(dest).put(src, w);
                numOfEdges++;
                mc++;
            } else
                if (getEdge(src, dest) != null && getEdge(src, dest).getWeight() != w)  //the edge exist, update only the weight
            {
                edge_data edge = new Edge(src, dest, w);
                Edges.get(src).replace(dest, edge);
                mc++;
            }
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<api.node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return Nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @param node_id
     * @return Collection<api.edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (Edges.containsKey(node_id))
            return Edges.get(node_id).values();
        return null;
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (Nodes.containsKey(key) && Edges.containsKey(key)) { //check if the key is contains
            node_data save = Nodes.get(key);
            HashMap<Integer, edge_data> nei = Edges.get(key); // make map for move on edge
            for (Integer i : nei.keySet()) {        // remove edge from vertex(src) to other node(dest)
                destToSrc.get(i).remove(key);
                numOfEdges--;
                mc++;
            }
            if (destToSrc.containsKey(key))            // remove all the edge from the other node to vertex
            {
                for (int i : destToSrc.get(key).keySet()) {      //Who entered me
                    Edges.get(i).remove(key);
                    numOfEdges--;             // remove all the edge from other node to vertex
                    mc++;
                }
            }
            destToSrc.remove(key);
            Edges.remove(key);
            Nodes.remove(key);
            numOfNodes--;
            mc--;
            return save;
        }
        return null;
    }

    /**
     * Deletes the edge from the graph,
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (getEdge(src, dest) == null)
            return null;
        edge_data edge = Edges.get(src).get(dest);
        Edges.get(src).remove(dest);
        numOfEdges--;
        mc++;
        return edge;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return numOfNodes;
    }

    /**
     * Returns the number of edges (assume directional graph).
     * @return
     */
    @Override
    public int edgeSize() {
        return numOfEdges;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public String toString() {
        return "DWGraph_DS{" +
                "mc=" + mc +
                ", numOfEdges=" + numOfEdges +
                ", numOfNodes=" + numOfNodes +
                ", mapNodes=" + Nodes +
                ", mapEdges=" + Edges +
                '}';
    }

    /**
     * this method if the given Object is equals to this graph.
     * @param o- checking if it's equals graph
     * @return true or false
     */
    @Override
    public boolean equals(Object o){
        if(this==o)
            return true;
        if (o instanceof directed_weighted_graph){
            directed_weighted_graph temp = (directed_weighted_graph) o;
            if (this.nodeSize()!=temp.nodeSize() || this.edgeSize()!= temp.edgeSize())
                return false;
            for (node_data i : this.getV()){
                if(temp.getNode(i.getKey())== null)
                    return false;
                if (this.getE(i.getKey()).size() != temp.getE(i.getKey()).size())
                    return false;
            }
            return true;
        }
        return false;
    }

}
