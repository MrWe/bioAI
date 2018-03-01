import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class Helpers {

    static double PlanarEuclideanDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt((Math.pow(x0 - x1, 2)) + Math.pow(y0 - y1, 2));
    }

    static Comparator<Individual> edgeValueComparator = new Comparator<Individual>(){
        public int compare(Individual i, Individual o){
            return (int)(i.getEdgeValue() - o.getEdgeValue());
        }
    };

    static Comparator<Individual> overallDeviationComparator = new Comparator<Individual>(){
        public int compare(Individual i, Individual o){
            return (int)(i.getOverallDeviation() - o.getOverallDeviation());
        }
    };

    static Comparator<Individual> crowdingDistanceComparator = new Comparator<Individual>(){
        public int compare(Individual i, Individual o){
            return (int)(o.getCrowdingDistance() - i.getCrowdingDistance());
        }
    };

    public static ArrayList<Individual> crowdingDistance(ArrayList<Individual> individuals, int num_individuals_to_keep){
        //Sort individuals by edge value and get best and worst
        ArrayList<Individual> edgeValueRanks = new ArrayList<Individual>(individuals);
        Collections.sort(edgeValueRanks, edgeValueComparator);
        edgeValueRanks.get(0).setCrowdingDistance(Integer.MAX_VALUE);
        edgeValueRanks.get(edgeValueRanks.size() - 1).setCrowdingDistance(Integer.MAX_VALUE);

        //Sort individuals by overall deviation and get best and worst
        ArrayList<Individual> overallDeviationRanks = new ArrayList<Individual>(individuals);
        Collections.sort(overallDeviationRanks, overallDeviationComparator);
        overallDeviationRanks.get(0).setCrowdingDistance(Integer.MAX_VALUE);
        overallDeviationRanks.get(overallDeviationRanks.size() - 1).setCrowdingDistance(Integer.MAX_VALUE);

        //Establish FMin and FMax values
        double edgeValueFMax = edgeValueRanks.get(edgeValueRanks.size() - 1).getEdgeValue();
        double edgeValueFMin = edgeValueRanks.get(0).getEdgeValue();

        double overallDeviationFMax = overallDeviationRanks.get(overallDeviationRanks.size() - 1).getOverallDeviation();
        double overallDeviationFMin = overallDeviationRanks.get(0).getOverallDeviation();

        //Add edge value crowding distance
        for (int i = 1; i < edgeValueRanks.size()-1; i++) {
            double distance = (edgeValueRanks.get(i-1).getEdgeValue() - edgeValueRanks.get(i+1).getEdgeValue()) / (edgeValueFMax-edgeValueFMin);

            edgeValueRanks.get(i).setCrowdingDistance(edgeValueRanks.get(i).getCrowdingDistance() + distance);
        }

        //Add overall deviation crowding distance
        for (int i = 1; i < overallDeviationRanks.size()-1; i++) {
            double distance = (overallDeviationRanks.get(i-1).getOverallDeviation() - overallDeviationRanks.get(i+1).getOverallDeviation()) / (overallDeviationFMax-overallDeviationFMin);

            edgeValueRanks.get(i).setCrowdingDistance(edgeValueRanks.get(i).getCrowdingDistance() + distance);
        }

        //Sort by crowding distance
        Collections.sort(individuals, crowdingDistanceComparator);

        return new ArrayList<Individual>(individuals.subList(0, num_individuals_to_keep));
    }

    public static Individual tournamentSelection(Individual a, Individual b){
        if (a.getRank() == b.getRank()){
            return a.getCrowdingDistance() > b.getCrowdingDistance() ? a : b;
        } else {
            return a.getRank() > b.getRank() ? a: b;
        }

    }

    static double ColorEuclideanDistance(Color c0, Color c1) {
        return Math.sqrt((Math.pow(c0.getRed()- c1.getRed(), 2)) + (Math.pow(c0.getGreen() - c1.getGreen(), 2)) + (Math.pow(c0.getBlue()- c1.getBlue(), 2)));
    }

    static void setAvgColor(ArrayList<Centroid> centroids){

        for(Centroid c : centroids){
            double r = 0;
            double g = 0;
            double b = 0;

            for(Node n : c.getcurrentlyAssignedNodes()){
                r += n.getColor().getRed();
                g += n.getColor().getGreen();
                b += n.getColor().getBlue();
            }

            r /= c.getcurrentlyAssignedNodes().size();
            g /= c.getcurrentlyAssignedNodes().size();
            b /= c.getcurrentlyAssignedNodes().size();

            Color color = new Color((int)r, (int) g, (int) b);
            c.setAvgColor(color);
        }
    }
}
