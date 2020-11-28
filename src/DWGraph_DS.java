import java.util.*;


public class DWGraph_DS implements directed_weighted_graph {

    private int mc, numOfEdges, numOfNodes;
    private HashMap<Integer, node_data> mapNodes;          //hashmap of all the nodes.
    private HashMap<Integer, HashMap<Integer, edge_data>> mapEdges;   //every node has hashmap of his going out edges.
    private HashMap<Integer, HashMap<Integer, Double>> destToSrc; // a map of incoming edges to the node .

    public DWGraph_DS() {
        mc = numOfEdges = numOfNodes = 0;
        mapNodes = new HashMap<>();
        mapEdges = new HashMap<>();
        destToSrc = new HashMap<Integer, HashMap<Integer, Double>>();
    }

    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (mapNodes.containsKey(key))
            return mapNodes.get(key);
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
        if (mapEdges.containsKey(src)) {
            if (mapEdges.get(src).containsKey(dest)) {
                return mapEdges.get(src).get(dest);
            }
        }
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!mapNodes.containsKey(n.getKey())) {
            HashMap<Integer, edge_data> edge = new HashMap<>();
            mapEdges.put(n.getKey(), edge);
            mapNodes.put(n.getKey(), n);
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
                mapEdges.get(src).put(dest, edge);
                destToSrc.get(dest).put(src, w);
                numOfEdges++;
                mc++;
            } else
                if (getEdge(src, dest) != null && getEdge(src, dest).getWeight() != w)  //the edge exist, update only the weight
            {
                edge_data edge = new Edge(src, dest, w);
                mapEdges.get(src).replace(dest, edge);
                mc++;
            }
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return mapNodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (mapEdges.containsKey(node_id))
            return mapEdges.get(node_id).values();
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
        if (mapNodes.containsKey(key) && mapEdges.containsKey(key)) { //check if the key is contains
            node_data save = mapNodes.get(key);
            HashMap<Integer, edge_data> nei = mapEdges.get(key); // make map for move on edge
            for (Integer i : nei.keySet()) {        // remove edge from vertex(src) to other node(dest)
                destToSrc.get(i).remove(key);
                numOfEdges--;
                mc++;
            }
            if (destToSrc.containsKey(key))            // remove all the edge from the other node to vertex
            {
                for (int i : destToSrc.get(key).keySet()) {      //Who entered me
                    mapEdges.get(i).remove(key);
                    numOfEdges--;             // remove all the edge from other node to vertex
                    mc++;
                }
            }
            destToSrc.remove(key);
            mapEdges.remove(key);
            mapNodes.remove(key);
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
        edge_data edge = mapEdges.get(src).get(dest);
        mapEdges.get(src).remove(dest);
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
                ", mapNodes=" + mapNodes +
                ", mapEdges=" + mapEdges +
                '}';
    }
}
