import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Centroid {

    private ArrayList<Node>  currentlyAssignedNodes;
    private double x;
    private double y;
    private Color color;


    public Centroid(double x, double y, Color c){
        this.x = x;
        this.y = y;
        this.color = c;
        currentlyAssignedNodes = new ArrayList<>();
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

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}


