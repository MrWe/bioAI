import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
}
