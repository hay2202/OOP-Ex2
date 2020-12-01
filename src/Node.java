public class Node implements node_data {

    private int key, tag;
    private double weight;
    private String info;
    private geo_location location;
//    private static int counter=0;

//    public Node(){
//        this.key=counter++;
//        this.tag=0;
//        this.weight=0;
//        this.info=null;
//        this.location=null;
//    }

        public Node(int key){
        this.key=key;
        this.tag=0;
        this.weight=0;
        this.info=null;
        this.location=null;
    }

//copy constructor
    public Node(node_data n ){
        this.key=n.getKey();
        this.tag=n.getTag();
        this.weight=n.getWeight();
        this.info=n.getInfo();
        this.location=n.getLocation();
    }

    public Node(int key, int tag, double weight){
        this.key=key;
        this.tag=tag;
        this.weight=weight;
    }
    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     * @return
     */
    @Override
    public geo_location getLocation() {
        return this.location;
    }

    /**
     * Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.location=p;
    }

    /**
     * Returns the weight associated with this node.
     * @return
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight=w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
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
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag=t;
    }

    @Override
    public String toString() {
        return "Node # "+key;
    }
}
