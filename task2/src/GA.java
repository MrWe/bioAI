import java.util.ArrayList;
import java.util.Random;

public class GA {
    public static ArrayList<ArrayList<Integer>> crossover(Individual a, Individual b, int[][] imgArray){
        ArrayList<ArrayList<Integer>> newRoots = new ArrayList<>();

        for (int i = 0; i < a.getSegments().size(); i++) {

            ArrayList<Integer> currRootCoords = new ArrayList<>();

            //Calculate average coords
            int newX = (a.getSegments().get(i).getRootNode().getX() + b.getSegments().get(i).getRootNode().getX() ) / 2;
            int newY = (a.getSegments().get(i).getRootNode().getY() + b.getSegments().get(i).getRootNode().getY() ) / 2;

            currRootCoords.add(newX);
            currRootCoords.add(newY);

            newRoots.add(currRootCoords);

        }

        return newRoots;
    }

    public static Individual tournamentSelection(Individual a, Individual b){
        if (a.getRank() == b.getRank()){
            return a.getCrowdingDistance() > b.getCrowdingDistance() ? a : b;
        } else {
            return a.getRank() > b.getRank() ? a: b;
        }

    }

    public static ArrayList<ArrayList<ArrayList<Integer>>> mutate(ArrayList<ArrayList<ArrayList<Integer>>> children, int[][] imgArray){
        Random r = new Random();

        for (int i = 0; i < children.size(); i++) {
            for (int j = 0; j < children.get(i).size(); j++) {
                if(r.nextDouble() < 0.1){
                    int newX = r.nextInt(imgArray[0].length);
                    int newY = r.nextInt(imgArray.length);

                    children.get(i).get(j).set(0, newY);
                    children.get(i).get(j).set(1, newX);

                }
            }
        }

        return children;
    }


    public static ArrayList<ArrayList<ArrayList<Integer>>> doGA(int[][] imgArray, Population parentPopulation, int numIndividuals){
        ArrayList<Individual> parents = parentPopulation.getIndividuals();
        ArrayList<ArrayList<ArrayList<Integer>>> children = new ArrayList<>();

        for (int i = 0; i < numIndividuals * 3; i++) {
            Random r = new Random();

            //TODO: Ensure that we cannot get the same two individuals
            Individual crossover_individual_a = GA.tournamentSelection(parents.get(r.nextInt(parents.size())), parents.get(r.nextInt(parents.size())));
            Individual crossover_individual_b = GA.tournamentSelection(parents.get(r.nextInt(parents.size())), parents.get(r.nextInt(parents.size())));

            children.add((GA.crossover(crossover_individual_a, crossover_individual_b, imgArray)));
        }

        children = mutate(children, imgArray);

        return children;
    }
}
