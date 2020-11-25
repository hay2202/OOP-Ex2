import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms{
    private directed_weighted_graph graph;

    public DWGraph_Algo(){
        graph=new DWGraph_DS();
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
       graph=g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        Collection<node_data> nodeSet = graph.getV();   //all nodes in the graph
        HashMap<Integer, Collection<edge_data>> neighbor = new HashMap<>();
        directed_weighted_graph copyGraph = new DWGraph_DS();

        for (node_data n : nodeSet) {             //copying a set of all the neighbors
            neighbor.put(n.getKey(), graph.getE(n.getKey()));
        }
        for (node_data n : nodeSet) {           //copying the nodes to the new graph
            node_data temp = new Node(n);
            copyGraph.addNode(temp);
        }
        for (node_data n : nodeSet) {              //copying the edges
            if (neighbor.get(n.getKey()) != null) {
                for (edge_data e : neighbor.get(n.getKey())) {
                    copyGraph.connect(e.getSrc(),e.getDest(),e.getWeight());
                }
            }
        }
        return copyGraph;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        return false;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        return false;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        return false;
    }
}
