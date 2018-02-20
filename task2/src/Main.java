import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

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



    static ArrayList<Centroid> initCentroids(BufferedImage img, int num_centroids){

        Random r = null;

        ArrayList<Centroid> centroids = new ArrayList<>();
        for (int n = 0; n < num_centroids; n++){

            centroids.add(new Centroid(new Point2D.Double(r.nextInt(img.getHeight()-1), r.nextInt(img.getHeight()-1))));
        }
        return centroids;
    }

    static ArrayList<ArrayList<Integer>> getWeights(BufferedImage img, ArrayList<Centroid> centroids){
        ArrayList<Point2D> seen = new ArrayList<>();
        for(Centroid c : centroids){
            c.addPixel(new Gene(c.getPosition()));

        }


        while(seen.size() < img.getHeight() * img.getWidth()){
            for(Centroid c : centroids){


            }
        }

        
    }



}
