import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GA {
    public static Individual crossover(Individual a, Individual b, BufferedImage img){
        ArrayList<Centroid> newCentroids = new ArrayList<>();

        for (int i = 0; i < a.getCentroids().size(); i++) {
            //Calculate average coords
            double new_x = (a.getCentroids().get(i).getX() + b.getCentroids().get(i).getX()) / 2;
            double new_y = (a.getCentroids().get(i).getY() + b.getCentroids().get(i).getY()) / 2;

            Color c = new Color(img.getRGB((int)new_x,(int)new_y));

            newCentroids.add(new Centroid(new_x, new_y, c));
        }
        return new Individual(newCentroids);
    }

    public static Individual tournamentSelection(Individual a, Individual b){
        if (a.getRank() == b.getRank()){
            return a.getCrowdingDistance() > b.getCrowdingDistance() ? a : b;
        } else {
            return a.getRank() > b.getRank() ? a: b;
        }

    }

    public static ArrayList<Individual> doGA(BufferedImage img, Population parentPopulation){
        ArrayList<Individual> parents = parentPopulation.getIndividuals();
        ArrayList<Individual> children = new ArrayList<>();

        for (int i = 0; i < parents.size(); i++) {
            Random r = new Random();

            //TODO: Ensure that we cannot get the same two individuals
            Individual crossover_individual_a = GA.tournamentSelection(parents.get(r.nextInt(parents.size())), parents.get(r.nextInt(parents.size())));
            Individual crossover_individual_b = GA.tournamentSelection(parents.get(r.nextInt(parents.size())), parents.get(r.nextInt(parents.size())));

            children.add(GA.crossover(crossover_individual_a, crossover_individual_b, img));
        }
        return children;
    }
}
