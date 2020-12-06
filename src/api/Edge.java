package api;

public class Edge implements edge_data {

    private int src,dest,tag;
    private double w;
    private String info;

    public Edge (int src, int dest, double weight){
        this.src=src;
        this.dest=dest;
        this.w =weight;
        info=null;
        tag=0;
    }

    /**
     * The id of the source node of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return this.src;
    }

    /**
     * The id of the destination node of this edge
     * @return
     */
    @Override
    public int getDest() {
        return this.dest;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return this.w;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info=s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag=t;
    }

    @Override
    public String toString() {
        return "Edge{" +"src= " + src + ", dest=" + dest + '\'' + '}';
    }
}
