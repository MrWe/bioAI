import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        /*
        Change these
         */
        String path = "1";
        int numCentroids = 10;
        int numPopulations = 20;
        int numIndividuals = 20;


        BufferedImage img = readImage(path);


        Population population = new Population(img, numCentroids, numIndividuals);


        for (int i = 0; i < numPopulations; i++) {
            population = new Population(img, numCentroids, GA.doGA(img, population));
        }

        for (Centroid c : population.getIndividuals().get(0).getCentroids()) {
            for (Node n : c.getcurrentlyAssignedNodes()) {
                img = changeImage(img, n, c);
            }
        }

        writeImage(path, img);

    }



    static BufferedImage readImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("TestImages/"+path+"/Test image.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    static void writeImage(String path, BufferedImage img) {
        File outputfile = new File("OutFiles/"+path+".jpg");
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    static BufferedImage changeImage(BufferedImage img, Node node, Centroid centroid) {
        //Color c = Color.WHITE;
        //if(node.getColor() == Color.BLACK) {
            Color c = new Color(centroid.getColor().getRGB());
          //  c = Color.BLACK;
        //}

        img.setRGB((int) node.getX(), (int) node.getY(), c.getRGB());
        return img;
    }
}
