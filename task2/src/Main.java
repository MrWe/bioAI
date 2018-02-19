import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        /*
        Change these
         */
        String path = "9";
        int numCentroids = 16;
        int kmeansIterations = 30;

        /*
        Init image and centroids
         */
        BufferedImage img = readImage(path);

        //writeImage(path, img);
        ArrayList<ArrayList<Integer>> gene = constructInitialGene(img, 4);

        for (ArrayList<Integer> line : gene) {
            System.out.println(line);
        }
    }

    static BufferedImage readImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/Users/agnetedjupvik/Desktop/Skolearbeid/8. semester/Bio-AI/bioAI/task2/src/TestImages/7/Test image.jpg"));
            //TestImages/7/Test image.jpg

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    static ArrayList<ArrayList<Integer>> constructInitialGene(BufferedImage img, int num_segments) {
        ArrayList<ArrayList<Integer>> gene = new ArrayList<>();


        int row_segments = img.getHeight() / num_segments; // Number of rows an initial gene should cover
        int current_segment = 0;

        for (int i = 0; i < img.getHeight(); i++) {
            ArrayList<Integer> line = new ArrayList<Integer>(Collections.nCopies(img.getWidth() - 1, current_segment));
            gene.add(line);
            if (i % row_segments == 0) {
                current_segment++;
            }
        }
        return gene;
    }

    static void writeImage(String path, BufferedImage img) {
        File outputfile = new File("OutFiles/" + path + ".jpg");
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static double euclideanDistance(Color c0, Color c1) {
        return Math.sqrt((Math.pow(c0.getRed(), 2) - Math.pow(c1.getRed(), 2)) + (Math.pow(c0.getGreen(), 2) - Math.pow(c1.getGreen(), 2)) + (Math.pow(c0.getBlue(), 2) - Math.pow(c1.getBlue(), 2)));
    }
}
