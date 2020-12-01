public class GeoLocation implements geo_location {
    private double x,y,z;

    public GeoLocation(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(geo_location g) {
        double powX=Math.pow(this.x-g.x(),2);
        double powY=Math.pow(this.y-g.y(),2);
        double powZ=Math.pow(this.z-g.z(),2);
        return Math.sqrt((powX+powY+powZ));
    }
}
