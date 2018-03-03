import java.awt.*;
import java.util.ArrayList;

public class Node implements Comparable<Node>{

    private double x, y;
    private Node parent;
    private boolean isRoot;
    private ArrayList<Node> children;
    private ArrayList<Node> neighbours;
    private double g = 1;
    private int cost = Integer.MAX_VALUE;
    private int color;

    private Centroid belongsToCentroid;
    private String hash;
    private boolean closed = false;

    Node(double x, double y, int c){
        this.x = x;
        this.y = y;
        this.color = c;
        this.hash = hashNode();
        this.children = new ArrayList<>();

    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    private int concatenate() {
        return Integer.parseInt((int)this.x+""+(int)this.y);
    }

    @Override
    public int hashCode(){
        return concatenate();
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int c){
        this.color = c;
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

    public double getY() {
        return y;
    }

    public void setG(double g) {
        this.g = g;
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

    public Centroid getBelongsToCentroid() {
        return belongsToCentroid;
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

    public Node getParent() {
        return parent;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }
}
