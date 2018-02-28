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
        for (int i = 0; i < numIndividuals; i++) {
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

        System.out.println("Before sort" + this.individuals);

        Collections.sort(this.individuals);

        System.out.println("After sort" + this.individuals);

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
