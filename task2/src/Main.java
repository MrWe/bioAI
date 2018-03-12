import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        /*
        Change these
         */
        String path = "5";
        int numSegments = 10;
        int numPopulations = 20;
        int numIndividuals = 20;


        BufferedImage img = readImage(path);
        //img = scale(img, img.getType(), (int)(img.getWidth()*0.1), (int)(img.getHeight()*0.1), 0.1, 0.1);
        int[][] imgArray = Helpers.convertTo2DWithoutUsingGetRGB(img);

        ArrayList<ArrayList<Node>> nodes = Helpers.initNodes(imgArray);

        ArrayList<ArrayList<ArrayList<Edge>>> edges = new ArrayList<>();


        for (int i = 0; i < nodes.size(); i++) {
            edges.add(new ArrayList<>());
            for (int j = 0; j < nodes.get(i).size(); j++) {
                edges.get(i).add(new ArrayList<>());
                edges.get(i).get(j).addAll(Helpers.setNodeEdges(nodes.get(i).get(j),nodes));
            }
        }

        ArrayList<Node> rootNodes;
        ArrayList<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            System.out.println(i);

            nodes = Helpers.initNodes(imgArray);

            rootNodes = Helpers.initRootNodes(nodes, numSegments);

            MST.prim(rootNodes, nodes, edges, numSegments);


            ArrayList<Segment> segments = BFS.BFS(rootNodes);


        individuals.add(new Individual(segments, nodes));

        }

        ArrayList<Individual> best = Helpers.crowdingDistance(individuals, 1);

        for(Segment segment : best.get(0).getSegments()){
            for(Node n : segment.getNodes()){
               img = changeImage(img, n, segment.getRootNode());
          }

        }


        writeImage(path, img);


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

        Color c;
        if(node.isEdge()){
            c = Color.BLACK;
        }
        else{
            c = Color.WHITE;
        }

        img.setRGB(node.getY(), node.getX(), c.getRGB());

        /*if(node.isEdge()){
            try{
                img.setRGB(node.getY()-1, node.getX(), c.getRGB());
            }
            catch (Exception e){

            }
            try{
                img.setRGB(node.getY()+1, node.getX(), c.getRGB());
            }
            catch (Exception e){

            }
            try{
                img.setRGB(node.getY(), node.getX()+1, c.getRGB());
            }
            catch (Exception e){

            }
            try{
                img.setRGB(node.getY(), node.getX()-1, c.getRGB());
            }
            catch (Exception e){

            }
        }*/






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
