public class Edge implements Comparable<Edge>{

    private Node n1;
    private Node n2;
    private double edgeCost;
    private boolean closed;

    public Edge(Node n1, Node n2, double edgeCost){
        this.n1 = n1;
        this.n2 = n2;
        this.edgeCost = edgeCost;
    }

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }

    public double getEdgeCost() {
        return edgeCost;
    }

    public boolean contains(Node n){
        return n1 == n || n2 == n;
    }

    public boolean equals(Edge other){
        return(n1 == other.n1 && n2 == other.n2) || (n1 == other.n2 && n2 == other.n1);
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
}
