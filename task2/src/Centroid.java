import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Centroid {

    private ArrayList<Node>  currentlyAssignedNodes;
    private double x;
    private double y;
    private Color color;
    private Color avgColor;
    private double overallDeviation;
    private double edgeValue;
    private String hash;



    public Centroid(double x, double y, Color c){
        this.x = x;
        this.y = y;
        this.color = c;
        this.currentlyAssignedNodes = new ArrayList<>();
        this.hash = createHash();
    }

    public void updateCentroid() {

    }

    private String createHash(){
        return (int)this.x+""+(int)this.y;
    }

    public ArrayList<Node> getcurrentlyAssignedNodes() {
        return currentlyAssignedNodes;
    }

    public void addNode(Node node) {
        this.currentlyAssignedNodes.add(node);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getColor() {
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

    public Color getAvgColor() {
        return avgColor;
    }

    public void setAvgColor(Color avgColor) {
        this.avgColor = avgColor;
    }
}


