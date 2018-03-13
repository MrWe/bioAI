import java.util.ArrayList;

public class Node implements Comparable<Node> {

    private int x, y;
    private Node parent;
    private boolean isRoot = false;
    private boolean isTreeRoot = false;
    public final ArrayList<Node> children;
    public ArrayList<Node> neighbours;
    private int cost = Integer.MAX_VALUE;
    private int color;
    private Segment segment;
    private boolean isEdge = false;

    private ArrayList<Edge> edges;

    private Centroid belongsToCentroid;
    private String hash;
    private boolean closed = false;

    Node(int x, int y, int c) {
        this.x = x;
        this.y = y;
        this.color = c;
        this.hash = hashNode();
        this.children = new ArrayList<>();
        this.edges = new ArrayList<>();

    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    private int concatenate() {
        return Integer.parseInt((int) this.x + "" + (int) this.y);
    }

    @Override
    public int hashCode() {
        return concatenate();
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {

        synchronized(this.children) {
            this.children.add(child);
        }

    }

    public int getColor() {
        return color;
    }

    public void setColor(int c) {
        this.color = c;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.getCost(), o.getCost());
    }

    public String hashNode() {
        return "" + this.x + "" + this.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = (int) cost;
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

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public boolean isTreeRoot() {
        return isTreeRoot;
    }

    public void setTreeRoot(boolean treeRoot) {
        isTreeRoot = treeRoot;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public boolean isEdge() {
        return isEdge;
    }

    public void setEdge(boolean edge) {
        isEdge = edge;
    }
}
