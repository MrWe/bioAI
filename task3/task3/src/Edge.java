public class Edge {

    private int to;
    private int from;
    private double pheromoneValue;

    public Edge(int to, int from){
        this.to = to;
        this.from = from;

        this.pheromoneValue = Constants.initialPheromoneValue;
    }

    public double getPheromoneValue() {
        return pheromoneValue;
    }

    public void setPheromoneValue(double pheromoneValue) {
        this.pheromoneValue = pheromoneValue;
    }
}
