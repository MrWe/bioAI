public class Centroid {

    private int x;
    private int y;
    private int color;
    private double overallDeviation;
    private double edgeValue;
    private String hash;


    public Centroid(int x, int y, int c) {
        this.x = x;
        this.y = y;
        this.color = c;
        this.hash = createHash();
    }

    public void updateCentroid() {

    }

    private String createHash() {
        return (int) this.x + "" + (int) this.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }

    public double getOverallDeviation() {
        return overallDeviation;
    }

    public void setOverallDeviation(double overallDeviation) {
        this.overallDeviation = overallDeviation;
    }

    public String getHash() {
        return hash;
    }

    public double getEdgeValue() {
        return edgeValue;
    }

    public void setEdgeValue(double edgeValue) {
        this.edgeValue = edgeValue;
    }

}


