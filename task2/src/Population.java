import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class Population {

    private ArrayList<Node> closedList = new ArrayList<>();
    private ArrayList<Individual> individuals;

    public Individual getChildIndividual() {
        return childIndividual;
    }

    public void setChildIndividual(Individual childIndividual) {
        this.childIndividual = childIndividual;
    }

    private Individual childIndividual;

    public Population(BufferedImage img, int numCentroids){
        individuals = new ArrayList<>();
        createIndividuals(img, numCentroids, 10);

    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    private void createIndividuals(BufferedImage img, int numCentroids, int numIndividuals){
        for (int i = 0; i < 3*numIndividuals; i++) {
            ArrayList<ArrayList<Node>> nodes = initNodes(img);

            ArrayList<Centroid> centroids = initCentroids(img, numCentroids);
            ArrayList<Node> startNodes = getStartNodes(centroids, nodes);
            ArrayList<SearchPath> searches = initSearches(startNodes);
            boolean runMore = true;
            while(runMore) {
                runMore = false;
                for (int j = 0; j < searches.size(); j++) {
                    if(searches.get(j).runOneStep(closedList, img, centroids.get(j), nodes)){
                        runMore = true;
                    }
                }
            }
            Helpers.setAvgColor(centroids);

            this.individuals.add(new Individual(centroids));
        }

        for(Individual i : this.individuals){
            setRank(i);
        }

        Collections.sort(this.individuals);

        ArrayList<Individual> acceptedIndividuals = new ArrayList<>();

        int rankCounter = 1;

        while (true) {
            ArrayList<Individual> individualsToAdd = getAllIndividualsOfRankN(rankCounter);
            if (acceptedIndividuals.size() + individualsToAdd.size() > numIndividuals * 2) {
                acceptedIndividuals.addAll(Helpers.crowdingDistance(individualsToAdd, acceptedIndividuals.size() + individualsToAdd.size() - numIndividuals * 2));
                break;
            } else {
                acceptedIndividuals.addAll(individualsToAdd);
                if(acceptedIndividuals.size() == numIndividuals * 2){ //We added exactly as many as we needed
                    break;
                }
            }
            rankCounter++;
        }

        ArrayList<Individual> final_individuals = Helpers.crowdingDistance(acceptedIndividuals, numIndividuals); //Reduce number of individuals from 2N to N=numIndividuals
        this.individuals = final_individuals;
        Random r = new Random();

        //TODO: Ensure that we cannot get the same two individuals
        Individual crossover_individual_a = Helpers.tournamentSelection(this.individuals.get(r.nextInt(this.individuals.size())), this.individuals.get(r.nextInt(this.individuals.size())));
        Individual crossover_individual_b = Helpers.tournamentSelection(this.individuals.get(r.nextInt(this.individuals.size())), this.individuals.get(r.nextInt(this.individuals.size())));


        setChildIndividual(GA.crossover(crossover_individual_a, crossover_individual_b, img));


        ArrayList<ArrayList<Node>> nodes = initNodes(img);


        ArrayList<Node> startNodes = getStartNodes(this.getChildIndividual().getCentroids(), nodes);
        ArrayList<SearchPath> searches = initSearches(startNodes);
        boolean runMore = true;
        while(runMore) {
            runMore = false;
            for (int j = 0; j < searches.size(); j++) {
                if(searches.get(j).runOneStep(closedList, img, this.getChildIndividual().getCentroids().get(j), nodes)){
                    runMore = true;
                }
            }
        }
        Helpers.setAvgColor(this.getChildIndividual().getCentroids());
    }

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



    private ArrayList<ArrayList<Node>> initNodes(BufferedImage img){
        ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
        for (int i = 0; i < img.getWidth(); i++) {
            nodes.add(new ArrayList<Node>());
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = new Color(img.getRGB(i,j));
                nodes.get(i).add(new Node(i,j,c));
            }
        }
        return nodes;
    }

    private ArrayList<Centroid> initCentroids(BufferedImage img, int num_centroids){

        Random r = new Random();

        HashSet<String> selected = new HashSet<>();

        ArrayList<Centroid> centroids = new ArrayList<>();
        for (int n = 0; n < num_centroids; n++){
            int x = r.nextInt(img.getWidth());
            int y = r.nextInt(img.getHeight());
            String s = x+""+y;
            int counter = 0;
            while(selected.contains(s) && counter < 1000){
                x = r.nextInt(img.getWidth());
                y = r.nextInt(img.getHeight());
                s = x+""+y;
                counter += 1;
            }
            if(counter >= 999){
                break;
            }

            selected.add(s);

            Color c = new Color(img.getRGB(x,y));
            centroids.add(new Centroid(x,y,c));
        }
        return centroids;
    }

    private ArrayList<Node> getStartNodes(ArrayList<Centroid> centroids, ArrayList<ArrayList<Node>> nodes){
        ArrayList<Node> startNodes = new ArrayList<>();

        for(Centroid c : centroids){
            startNodes.add(nodes.get((int) c.getX()).get((int) c.getY()));
        }
        return startNodes;
    }

    private ArrayList<SearchPath> initSearches(ArrayList<Node> startNodes){
        ArrayList<SearchPath> searches = new ArrayList<>();
        for (Node n : startNodes){
            searches.add(new SearchPath(n));
        }
        return searches;
    }
}
