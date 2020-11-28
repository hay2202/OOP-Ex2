import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph graph;
    HashMap<Integer, Double> dist; //  Length from src -> dest
    HashMap<Integer, Integer> paht; // path from src -> dest .

    public DWGraph_Algo() {
        graph = new DWGraph_DS();
        dist = new HashMap<Integer, Double>();
        paht = new HashMap<Integer, Integer>();
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        graph = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
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
                    copyGraph.connect(e.getSrc(), e.getDest(), e.getWeight());
                }
            }
        }
        return copyGraph;
    }


    /**
     * Goes through all the vertices and sides of the graph.
     * First check if we have passed the vertex in each operation of the recursion.
     * Then marks the vertex we went through.
     * He then goes through a loop on all of his neighbors.
     * Performs on any vertex that houses the recursion.
     * This action in recursion we always go back to the next neighbor
     * and from there continue until finally he returns to the starting point.
     * The time complexity of the algorithm is O(E+V) ,Because it goes through
     * all the vertices and edges of the graph.
     * <p>
     * "p"- This is a marking we have passed.
     *
     * @param key-vertex start.
     */
    public void dfs(int key) { //to is connect
        if (graph.getNode(key).getInfo() == "p") // "p"- This is a marking we have passed.
            return;
        graph.getNode(key).setInfo("p");

        for (edge_data next : graph.getE(key))
            dfs(next.getDest());

    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        for (node_data i : graph.getV()) {
            i.setInfo(null);
        }
        if (graph != null)
            dfs(0);

        for (node_data i : graph.getV()) {
            if (i.getInfo() == null)
                return false;
        }
        return true;
    }

    /**
     * dist - contains the map of the vertices and each value contains the route from the source to the vertex.
     * paht- contains the route map which each vertex contains its father.
     * This algorithm we get a vertex and start going over all the other vertices in the graph.
     * In the first loop you go through all the existing vertices in the graph.
     * Take a vertex from the graph and ask if we have already gone over it, if so, move on to the next vertex,
     * If not go ahead and get the edge list he has which leads to his neighbors.
     * Calculate the length of the neighboring vertex trajectory with the weight of the edge and the vertex from which it comes.
     * If the length of a route already exists, check whether the vertex that contains the length is larger than the route we calculated, if so, we will replace the length on the map.
     * Keeping track is put in that each vertex contains its father (where it came from) and thus the track is saved.
     * The complexity of time is O (E + V) because we go through all the vertices in the graph and also go over the whole edge of the graph.
     *
     * @param src
     * @param dest
     */
    public void _short_Path(int src, int dest) {
        dist.put(src, 0.0); // Enter the distance of the first vertex
       // graph.getNode(src).setTag(0);

        //Go over all the vertices in the graph.
        for (node_data vertex : graph.getV()) {

           //Checks if we have worked on the vertex if not you will enter and perform the actions.
            if (dist.get(vertex.getKey()) != null) {

                for (edge_data e : graph.getE(vertex.getKey())) { // Beyond all the neighbors.

                   //Calculating the weight from the source vertex to the neighboring vertex
                    // includes the distance we traveled to the source.
                    double newDist = dist.get(vertex.getKey()) + e.getWeight();

                    //If the neighbor first came to him, update the distance and add it to the route list
                    if (dist.get(e.getDest()) == null) {
                        dist.put(e.getDest(), newDist);
                        paht.put(e.getDest(), vertex.getKey());
                    }

                    else {// If we have already passed, check whether the new distance is small
                        // and the existing one, then replace the values.
                        dist.put(e.getDest(), (Math.min(dist.get(e.getDest()), newDist)));
                        double s = Math.min(dist.get(e.getDest()), newDist);
                        if (s == newDist)
                            paht.put(e.getDest(), e.getSrc());
                    }
                }
            }
        }
        for (int i : dist.keySet()) { //Enter the distance of each vertex
            double x = dist.get(i);
            graph.getNode(i).setTag((int) x); // i dont know if need cast ???????
        }

    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * ---------------------------------------------
     * Returns the lowest total weight from the source to the vertex with consideration of direction.
     * First check if the vertices exist in the graph.
     * Run the algorithm of finding the distance between the vertices by weight and direction.
     * Finally return the distance from the source if not, return -1.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        if (graph.getNode(src) != null && graph.getNode(dest) != null) {
            if (dest == src)
                return 0;
            _short_Path(src, dest);
            return dist.get(dest); // Runs the algorithm with the source and checks what the weight of the final vertex is

        }
        return -1;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * <p>
     * --------------------------------------------------------
     * Returns a list of the route from the source to the final vertex.
     * First check if the vertices exist in the graph.
     * The algorithm is then performed to find the source distance from the end according to the smallest weight.
     * Inside the algorithm the path is kept in a path which for each vertex is kept in its key
     * its father through which we can reach it and thus keep the path.
     * Within this function we find the route from the end to the beginning and insert in the order of entry that the source is first and the final destination last.
     * If not, return null.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (graph.getNode(src) != null && graph.getNode(dest) != null) {
            _short_Path(src, dest);
            List<node_data> list = new LinkedList<node_data>();
            int[] path2 = new int[graph.nodeSize() + 1];
            int count = 0;
            if (dist.get(dest) != null) {
                //Go over the vertices, from the final vertex to the beginning.
                //Inserting each vertex into an array that maintains the entire shortest vertex path.
                for (int i = dest; i != src; i = paht.get(i)) {
                    if (count < path2.length && dist.get(dest) != null)
                        path2[count] = i;
                    count++;
                }
            }
            if (count <= graph.nodeSize()) {//Insertion of the first organ
                path2[count] = src;
            }
            for (int i = count; i >= 0; i--) { // Adding organs to the list from beginning to end
                list.add(graph.getNode(path2[i]));
            }
            return list;
        }
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
