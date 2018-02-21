import java.awt.*;
import java.util.ArrayList;

public class Node implements Comparable<Node>{

    private double x, y;
    private Node parent;
    private double g = 1;
    private int cost = Integer.MAX_VALUE;
    private Color color;
    private ArrayList<Node> neighbours;
    private Centroid belongsToCentroid;
    private String hash;
    private boolean closed = false;

    public Node(double x, double y, Color c){
        this.x = x;
        this.y = y;
        this.color = c;
        this.hash = hashNode();

    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    private int concatenate() {
        int pow = 10;
        while(this.y >= pow)
            pow *= 10;
        return (int)this.x * pow + (int)this.y;
    }

    @Override
    public int hashCode(){
        return concatenate();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean getBelongsToCentroid() {
        return belongsToCentroid == null;
    }

    public void setBelongsToCentroid(Centroid belongsToCentroid) {
        this.belongsToCentroid = belongsToCentroid;
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(ArrayList<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.getCost(), o.getCost());
    }

    public String hashNode() {
        return "" + this.x +""+ this.y;
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

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = (int)cost;
    }
}
