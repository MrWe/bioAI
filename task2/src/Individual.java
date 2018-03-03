import java.awt.*;
import java.util.ArrayList;

public class Individual implements Comparable<Individual>{

    private ArrayList<Centroid> centroids;
    private double overallDeviation;
    private double edgeValue;
    private int rank;
    private double crowdingDistance;


    public Individual(ArrayList<Centroid> centroids){
        this.centroids = centroids;
        this.overallDeviation = sumOverallDeviation(centroids);
        this.edgeValue = sumEdgeValue(centroids);
        this.rank = 0;
        this.crowdingDistance = 0;
    }

    double sumOverallDeviation(ArrayList<Centroid> centroids){

        double totalFitness = 0;
        for (Centroid centroid : centroids) {
            totalFitness += overallDeviation(centroid);
        }

        return totalFitness;

    }

    double sumEdgeValue(ArrayList<Centroid> centroids){
        double totalFitness = 0;
        for(Centroid centroid : centroids) {
            totalFitness -= edgeValue(centroid);  //Trying to minimize this value, therefore it is the minus value. TODO: Test if we are correct.
        }
        return totalFitness;

    }


    /*
   We want to minimize this
    */
    private double overallDeviation(Centroid centroid){

        double fitness = 0;
        for (Node node : centroid.getcurrentlyAssignedNodes()) {
            fitness += Helpers.ColorEuclideanDistance(node.getColor(), centroid.getColor());
        }
        centroid.setOverallDeviation(fitness);
        return fitness;

    }

    /*
    We want to maximize this
     */
    private double edgeValue(Centroid centroid){
        double fitness = 0;
        for(Node node : centroid.getcurrentlyAssignedNodes()){
            for(Node neighbour : node.getNeighbours()){
                if(node.getBelongsToCentroid().getHash().equals(neighbour.getBelongsToCentroid().getHash())){
                    continue;
                }
                //node.setColor(Color.BLACK);
                fitness += Helpers.ColorEuclideanDistance(node.getColor(), neighbour.getColor());
            }
        }
        centroid.setEdgeValue(fitness);
        return fitness;
    }


    public double getEdgeValue() {
        return edgeValue;
    }

    public double getOverallDeviation() {
        return overallDeviation;
    }

    public ArrayList<Centroid> getCentroids() {
        return centroids;
    }


    public int compareTo(Individual o) {
        return this.getRank() - o.getRank();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }
}
