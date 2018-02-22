import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Centroid {

    private ArrayList<Node>  currentlyAssignedNodes;
    private double x;
    private double y;
    private Color color;
    private double overallDeviation;


    public Centroid(double x, double y, Color c){
        this.x = x;
        this.y = y;
        this.color = c;
        this.currentlyAssignedNodes = new ArrayList<>();
    }

    public void updateCentroid() {

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
}


