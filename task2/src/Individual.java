import java.util.ArrayList;

public class Individual implements Comparable<Individual> {

    private ArrayList<Segment> segments;
    private double overallDeviation;
    private double edgeValue;
    private int rank;
    private double crowdingDistance;


    public Individual(ArrayList<Segment> segments, ArrayList<ArrayList<Node>> nodes){
        this.segments = segments;
        this.overallDeviation = 0.1 * sumOverallDeviation(segments);
        this.edgeValue = (-1) * sumEdgeValue(segments, nodes);
        this.rank = 0;
        this.crowdingDistance = 0;
    }

    double sumOverallDeviation(ArrayList<Segment> segments){

        double totalFitness = 0;
        for (Segment segment : segments) {
            totalFitness += overallDeviation(segment);
        }

        return totalFitness;

    }

    double sumEdgeValue(ArrayList<Segment> segments, ArrayList<ArrayList<Node>> nodes){
        double totalFitness = 0;
        for(Segment segment : segments) {
            totalFitness -= edgeValue(segment, nodes);  //Trying to minimize this value, therefore it is the minus value. TODO: Test if we are correct.
        }
        return totalFitness;

    }


    /*
   We want to minimize this
    */
    private double overallDeviation(Segment segment){

        double fitness = 0;
        for (Node node : segment.getNodes()) {
            fitness += Helpers.rgbDistance(node.getColor(), segment.getNodes().get(0).getColor());
        }
        //segment.setOverallDeviation(fitness);
        return fitness;

    }

    /*
    We want to maximize this
     */
    private double edgeValue(Segment segment, ArrayList<ArrayList<Node>> nodes) {
        double fitness = 0;



        for (Node node : segment.getNodes()) {

            for (Node neighbour : Helpers.getNodeNeighbours(node, nodes)) {

                if (node.getSegment() == neighbour.getSegment()) {
                    continue;
                }

                node.setEdge(true);

                //node.setColor(Color.BLACK);
                fitness += Helpers.rgbDistance(node.getColor(), neighbour.getColor());
            }
        }
        return fitness;
    }


    public double getEdgeValue() {
        return edgeValue;
    }

    public double getOverallDeviation() {
        return overallDeviation;
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

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }
}
