import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
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
        //img = scale(img, img.getType(), (int)(img.getWidth()*0.1), (int)(img.getHeight()*0.1), 0.1, 0.1);
        int[][] imgArray = Helpers.convertTo2DWithoutUsingGetRGB(img);





        ArrayList<ArrayList<Node>> nodes = Helpers.initNodes(imgArray);

        MST mst = new MST(nodes.get(0).get(0));
        nodes = mst.prim(nodes);









        nodes.get(0).get(0).setRoot(true);

        nodes.get(10).get(0).setRoot(true);

        System.out.println(img.getWidth() * img.getHeight());
        final long startTime = System.currentTimeMillis();

        ArrayList<Node> closed =  BFS.BFS(nodes.get(0).get(0));




        System.out.println(closed.size());

        closed =  BFS.BFS(nodes.get(10).get(0));
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) );
        System.out.println(closed.size());



        System.exit(1);

       /* Population population = new Population(img, numCentroids, numIndividuals);



        for (int i = 0; i < numPopulations; i++) {
            population = new Population(img, numCentroids, GA.doGA(img, population));
        }

        for (Centroid c : population.getIndividuals().get(0).getCentroids()) {
            for (Node n : c.getcurrentlyAssignedNodes()) {
                img = changeImage(img, n, c);
            }
        }

        writeImage(path, img);*/

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





    /*static BufferedImage changeImage(BufferedImage img, Node node, Centroid centroid) {
        //Color c = Color.WHITE;
        //if(node.getColor() == Color.BLACK) {
            Color c = new Color(centroid.getColor());
          //  c = Color.BLACK;
        //}

        img.setRGB((int) node.getX(), (int) node.getY(), c.getRGB());
        return img;
    }
    */


    static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if(sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }
}
