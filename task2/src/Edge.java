public class Edge implements Comparable<Edge>{

    private int n1X;
    private int n2X;
    private int n1Y;
    private int n2Y;
    private double edgeCost;
    private boolean closed;

    public Edge(int n1X, int n2X, int n1Y, int n2Y, double edgeCost){
        this.n1X = n1X;
        this.n2X = n2X;
        this.n1Y = n1Y;
        this.n2Y = n2Y;
        this.edgeCost = edgeCost;
    }



    public double getEdgeCost() {
        return edgeCost;
    }

    public boolean contains(Node n) {
        return ((n1X == n.getX() && n1Y == n.getY()) || (n2X == n.getX() && n2Y == n.getY()));
    }

    public boolean equals(Edge other){
        return ((n1X == other.n1X && n1Y == other.n1Y) || (n2X == other.n2X && n2Y == other.n2Y));
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.getEdgeCost(), o.getEdgeCost());
    }

    public int getN1X() {
        return n1X;
    }

    public void setN1X(int n1X) {
        this.n1X = n1X;
    }

    public int getN2X() {
        return n2X;
    }

    public void setN2X(int n2X) {
        this.n2X = n2X;
    }

    public int getN1Y() {
        return n1Y;
    }

    public void setN1Y(int n1Y) {
        this.n1Y = n1Y;
    }

    public int getN2Y() {
        return n2Y;
    }

    public void setN2Y(int n2Y) {
        this.n2Y = n2Y;
    }
}
