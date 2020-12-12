package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph graph;
    private HashMap<Integer, Integer> path; // path from src -> dest .

    public DWGraph_Algo() {
        graph = new DWGraph_DS();
        path = new HashMap<>();
    }
    public DWGraph_Algo(directed_weighted_graph g){
        init(g);
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
     * Compute a deep copy of this graph.
     * the node_id stay the same as the original.
     * The method getting a set of all the nodes in the graph,
     * with all their neighbours and copying them to a new node in a new graph.
     *
     * @return deep copy of the graph.
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
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        resetTI(graph);
        directed_weighted_graph transposeGraph = new DWGraph_DS();
        if (graph != null) {
            Iterator<node_data> itr = graph.getV().iterator();   //random node in the graph
            dfs(itr.next().getKey(), graph);
            transposeGraph = transpose(graph);                   //transpose the graph
            Iterator<node_data> itr2 = transposeGraph.getV().iterator();
            dfs(itr2.next().getKey(), transposeGraph);           //check dfs on the transpose graph
        }

        for (node_data i : graph.getV()) {
            if (i.getInfo() == null)
                return false;
        }

        for (node_data i : transposeGraph.getV()) {
            if (i.getInfo() == null)
                return false;
        }
        return true;
    }

    /**
     * The method go through the vertices from the start node to the destination node
     * by using Dijkstra's Algorithm.
     * returns the length of the shortest path between src to dest
     * this method is for weighted graph.
     * if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (graph.getNode(src) == null || graph.getNode(dest) == null)
            return -1;
        if (src == dest)
            return 0;
        resetTI(graph);
        dijkstra(src, dest);
        if (graph.getNode(dest).getWeight() == 0)        // true only if we didn't visit dest node
            return -1;
        return graph.getNode(dest).getWeight();
    }

    /**
     * The method go through the vertices from the start node to the destination node
     * by using Dijkstra's Algorithm.
     * returns the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * if no such path --> returns null;
     * this method is for weighted graph.
     *
     * @param src  - start node
     * @param dest - end (target) node
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> ans = new ArrayList<>();
        if (src == dest) {
            ans.add(graph.getNode(src));
            return ans;
        }
        if (shortestPathDist(src, dest) == -1)
            return null;
        for (Integer i = dest; i != -1; i = path.get(i)) {
            ans.add(graph.getNode(i));
        }
        Collections.reverse(ans);
        return ans;
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
        //Making json object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(graph);

        //write json to file
        try {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        //Making json object
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DWGraph_DS.class, new GraphJsonDeserializer());
        Gson gson = builder.create();

        //read from json
        try {
            FileReader fr = new FileReader(file);
            directed_weighted_graph loadedGraph = gson.fromJson(fr, DWGraph_DS.class);
            this.init(loadedGraph);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
    private void dfs(int key, directed_weighted_graph g) { //to is connect
        if (g.getNode(key).getInfo() == "p") // "p"- This is a marking we have passed.
            return;
        g.getNode(key).setInfo("p");

        for (edge_data next : g.getE(key))
            dfs(next.getDest(), g);
    }

    /**
     * Dijkstra's algorithm useful to find the shortest path between two given nodes,
     * in a weighted directed graph. It picks the unvisited vertex with the lowest distance,
     * calculates the distance through it to each unvisited neighbor, and updates
     * the neighbor's distance if smaller. Mark visited when done with neighbors.
     * 'weight' - is for calculate the distance to this vertex.
     * 'info' - is for mark if the vertex visited.
     * 'path' - HashMap for each vertex to save his parent and rebuild into a list.
     * Time complexity: O(E+VLOGV)
     *
     * @param start
     * @param end
     */
    private void dijkstra(int start, int end) {
        PriorityQueue<node_data> q = new PriorityQueue<node_data>(new weightComp());
        q.add(graph.getNode(start));
        path.put(start, -1);                 //adding the src to the path and queue
        while (!q.isEmpty()) {
            node_data curr = q.poll();
            if (curr.getInfo() == null) {       //true if we didn't visit this node
                curr.setInfo("v");
                if (curr.getKey() == end)     //when we get to dest node
                    return;
                for (edge_data i : graph.getE(curr.getKey())) {     //moving on each neighbour of curr
                    node_data temp = graph.getNode(i.getDest());
                    if (temp.getInfo() == null) {                //true if we didn't visit this node
                        double w = i.getWeight();
                        w += curr.getWeight();
                        if (temp.getWeight() != 0) {
                            if (w < temp.getWeight()) {             //if the new weight is less then the exist
                                temp.setWeight(w);
                                path.put(i.getDest(), curr.getKey());
                            }
                        } else {                                    //if it's first time we reach to this node
                            temp.setWeight(w);
                            path.put(i.getDest(), curr.getKey());
                        }
                        q.add(temp);
                    }
                }
            }
        }
    }


    /**
     * reset 'tag','info' and 'weight' fields of each node in a given graph.
     * to reuse them for next time.
     *
     * @param g
     */
    private void resetTI(directed_weighted_graph g) {
        Collection<node_data> map = g.getV();       //set of all the nodes in the graph
        for (node_data i : map) {                   //reset info to 'null'
            i.setInfo(null);
        }
        for (node_data i : map) {                   //reset tag to '0'
            i.setTag(0);
        }
        for (node_data i : map) {                   //reset weight to '0'
            i.setWeight(0);
        }
    }

    /**
     * transpose the given graph by deep copy and flip the edges.
     * use for isConnected algorithms
     *
     * @param g
     * @return transGraph
     */
    private directed_weighted_graph transpose(directed_weighted_graph g) {
        Collection<node_data> nodeSet = g.getV();   //all nodes in the graph
        HashMap<Integer, Collection<edge_data>> neighbor = new HashMap<>();
        directed_weighted_graph transGraph = new DWGraph_DS();

        for (node_data n : nodeSet) {             //copying a set of all the neighbors
            neighbor.put(n.getKey(), g.getE(n.getKey()));
        }
        for (node_data n : nodeSet) {           //copying the nodes to the new graph
            node_data temp = new Node(n);
            transGraph.addNode(temp);
        }
        for (node_data n : nodeSet) {              //flip the edges
            if (neighbor.get(n.getKey()) != null) {
                for (edge_data e : neighbor.get(n.getKey())) {
                    transGraph.connect(e.getDest(), e.getSrc(), e.getWeight());
                }
            }
        }
        resetTI(transGraph);        //reset info , for dfs .
        return transGraph;
    }

    /**
     * inner class for using Comparator. this is for manage priority queue in bfsW method.
     * compare the weight of each node.
     */
    private static class weightComp implements Comparator<node_data> {
        @Override
        public int compare(node_data o1, node_data o2) {
            if (o1.getWeight() == o2.getWeight())
                return 0;
            if (o1.getWeight() > o2.getWeight())
                return 1;
            return -1;
        }
    }
}
