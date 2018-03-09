import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        /*
        Change these
         */
        String path = "3";
        int numSegments = 10;
        int numPopulations = 20;
        int numIndividuals = 20;


        BufferedImage img = readImage(path);
        //img = scale(img, img.getType(), (int)(img.getWidth()*0.1), (int)(img.getHeight()*0.1), 0.1, 0.1);
        int[][] imgArray = Helpers.convertTo2DWithoutUsingGetRGB(img);




        for (int i = 0; i < 3; i++) {

            ArrayList<ArrayList<Node>> nodes = Helpers.initNodes(imgArray);

            ArrayList<Node> rootNodes = Helpers.initRootNodes(nodes, numSegments);

            nodes = MST.prim(nodes, rootNodes);


        for(Node rootNode : rootNodes){
            ArrayList<Node> segment = BFS.BFS(rootNode);

            for(Node n : segment){
                img = changeImage(img, n, rootNode);
            }
        }

        writeImage(path, img);
        }

    }


    static BufferedImage readImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("TestImages/" + path + "/Test image.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    static void writeImage(String path, BufferedImage img) {
        File outputfile = new File("OutFiles/" + path + ".jpg");
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    static BufferedImage changeImage(BufferedImage img, Node node, Node rootNode) {
        //Color c = Color.WHITE;
        //if(node.getColor() == Color.BLACK) {
            Color c = new Color(rootNode.getColor());
          //  c = Color.BLACK;
        //}



        img.setRGB(node.getY(), node.getX(), c.getRGB());
        return img;
    }



    static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }
}
