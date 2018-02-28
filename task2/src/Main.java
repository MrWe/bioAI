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
        int numPopulations = 1;


        BufferedImage img = readImage(path);


        ArrayList<Population> populations = new ArrayList<>();

        for (int i = 0; i < numPopulations; i++) {
            populations.add(new Population(img, numCentroids));
        }


        for(Population p : populations) {
            for (Individual i : p.getIndividuals()) {
                for (Centroid c : i.getCentroids()) {
                    for (Node n : c.getcurrentlyAssignedNodes()) {
                        img = changeImage(img, n, c);
                    }
                }
            }
        }

        writeImage(path, img);

    }



    static BufferedImage readImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(String.format("/Users/agnetedjupvik/Desktop/Skolearbeid/8. semester/Bio-AI/bioAI/task2/src/TestImages/3/Test image.jpg", path)));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    static void writeImage(String path, BufferedImage img) {
        File outputfile = new File("/Users/agnetedjupvik/Desktop/Skolearbeid/8. semester/Bio-AI/bioAI/task2/src/OutFiles/3.jpg");
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
