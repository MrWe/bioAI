import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;

public class Population {

    private ArrayList<Node> closedList = new ArrayList<>();
    private ArrayList<Individual> individuals;

    public Population(int[][] imgArray, ArrayList<ArrayList<ArrayList<Edge>>> edges, int numSegments, int numIndividuals){
        this.individuals = initRandomPopulation(imgArray, edges, numSegments, numIndividuals);

        setRanks();
        sortIndividuals();
        ArrayList<Individual> fronts = selectFronts(numIndividuals);
        this.individuals = reduceFronts(fronts, numIndividuals);

    }

    public Population(int[][] imgArray, ArrayList<ArrayList<ArrayList<Edge>>> edges, ArrayList<ArrayList<ArrayList<Integer>>> newIndividualRoots, int numIndividuals){
        this.individuals = initChildPopulation(imgArray, edges, newIndividualRoots);

        setRanks();
        sortIndividuals();
        ArrayList<Individual> fronts = selectFronts(numIndividuals);
        this.individuals = reduceFronts(fronts, numIndividuals);

    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    private void setRanks(){
        for(Individual i : this.individuals){
            setRank(i);
        }
    }

    private void sortIndividuals(){
        Collections.sort(this.individuals);
    }

    private ArrayList<Individual> selectFronts(int numIndividuals){
        ArrayList<Individual> acceptedIndividuals = new ArrayList<>();

        int rankCounter = 1;

        while (acceptedIndividuals.size() < numIndividuals*2) {
            ArrayList<Individual> individualsToAdd = getAllIndividualsOfRankN(rankCounter);

            if (acceptedIndividuals.size() + individualsToAdd.size() > numIndividuals * 2) {

                ArrayList<Individual> toAdd = Helpers.crowdingDistance(individualsToAdd, ((acceptedIndividuals.size() + individualsToAdd.size()) - (numIndividuals * 2)));

                for(Individual i : toAdd){
                    acceptedIndividuals.add(i);
                    if(acceptedIndividuals.size() == numIndividuals*2){
                        break;
                    }
                }

                break;

            } else {
                acceptedIndividuals.addAll(individualsToAdd);
                if(acceptedIndividuals.size() == numIndividuals * 2){ //We added exactly as many as we needed
                    break;

                }
            }
            rankCounter++;
        }



        return acceptedIndividuals;
    }

    private ArrayList<Individual> reduceFronts(ArrayList<Individual> fronts, int numIndividuals){
        return Helpers.crowdingDistance(fronts, numIndividuals); //Reduce number of individuals from 2N to N=numIndividuals
    }

    private ArrayList<Individual> initRandomPopulation(int[][] imgArray, ArrayList<ArrayList<ArrayList<Edge>>> edges, int numSegments, int numIndividuals){
        ArrayList<Node> rootNodes;
        ArrayList<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < numIndividuals * 3; i++) {

            ArrayList<ArrayList<Node>> nodes = Helpers.initNodes(imgArray);

            rootNodes = Helpers.initRootNodes(nodes, numSegments);

            MST.prim(rootNodes, nodes, edges, numSegments);

            ArrayList<Segment> segments = BFS.BFS(rootNodes);

            individuals.add(new Individual(segments, nodes));

        }

        return individuals;
    }

    private ArrayList<Individual> initChildPopulation(int[][] imgArray, ArrayList<ArrayList<ArrayList<Edge>>> edges, ArrayList<ArrayList<ArrayList<Integer>>> newIndividualRoots){
        ArrayList<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < newIndividualRoots.size(); i++) {

                ArrayList<ArrayList<Node>> nodes = Helpers.initNodes(imgArray);
                ArrayList<Node> rootNodes = new ArrayList<>();

                for(ArrayList<Integer> rootCoord : newIndividualRoots.get(i)){
                    nodes.get(rootCoord.get(0)).get(rootCoord.get(1)).setRoot(true);
                    rootNodes.add(nodes.get(rootCoord.get(0)).get(rootCoord.get(1)));

                }

                MST.prim(rootNodes, nodes, edges, rootNodes.size());

                ArrayList<Segment> segments = BFS.BFS(rootNodes);

                individuals.add(new Individual(segments, nodes));

        }
        return individuals;
    }

    /*private void createRandomIndividuals(BufferedImage img, int numCentroids, int numIndividuals){
        for (int i = 0; i < 3*numIndividuals; i++) {
            ArrayList<ArrayList<Node>> nodes = initNodes(img);

            ArrayList<Centroid> centroids = initCentroids(img, numCentroids);
            ArrayList<Node> startNodes = getStartNodes(centroids, nodes);
            ArrayList<SearchPath> searches = initSearches(startNodes);

            dijkstra(img, nodes, searches, centroids);

            Helpers.setAvgColor(centroids);

            this.individuals.add(new Individual(centroids));
        }

    }*/

    /*private void runIndividuals(BufferedImage img, int numCentroids, ArrayList<Individual> individuals) {
        for (int i = 0; i < individuals.size(); i++) {
            Individual currentIndividual = individuals.get(i);

            ArrayList<ArrayList<Node>> nodes = initNodes(img);

            ArrayList<Node> startNodes = getStartNodes(currentIndividual.getCentroids(), nodes);
            ArrayList<SearchPath> searches = initSearches(startNodes);

            dijkstra(img, nodes, searches, currentIndividual.getCentroids());
            Helpers.setAvgColor(currentIndividual.getCentroids());
        }
        this.individuals = individuals;
    }*/




    public ArrayList<Individual> getAllIndividualsOfRankN(int n){
        ArrayList<Individual> returned_individuals = new ArrayList<Individual>();
        for(Individual i : this.individuals){
            if (i.getRank() == n){
                returned_individuals.add(i);
            }
        }
        return returned_individuals;
    }

    public void setRank(Individual i){
        int rank = 1;
        for (Individual individual : this.individuals){
            if(individual != i){
                if(isDominated(i, individual)){
                    rank++;
                }
            }
        }
        i.setRank(rank);

    }

    public boolean isDominated(Individual i, Individual o){
        if(i.getEdgeValue() > o.getEdgeValue() && i.getOverallDeviation() > o.getOverallDeviation()){
            return true;
        }
        return false;
    }

}
