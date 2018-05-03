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

    public void evaporate(){
        pheromoneValue = pheromoneValue > Constants.pheromoneMin ? pheromoneValue * (1 - Constants.pheromoneP) : pheromoneValue;
    }

    public void updateSingle(){
        this.pheromoneValue =  pheromoneValue < Constants.pheromoneMax ? pheromoneValue * Constants.pheromoneUpdateValue : pheromoneValue;
    }




}
