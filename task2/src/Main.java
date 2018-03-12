import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        /*
        Change these
         */
        String path = "3";
        int numSegments = 30;
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

        for (int i = 0; i < 10; i++) {

            nodes = Helpers.initNodes(imgArray);

            rootNodes = Helpers.initRootNodes(nodes, numSegments);

            nodes = MST.prim(nodes, rootNodes, edges);


            ArrayList<Segment> segments = new ArrayList<>();

        for(Node rootNode : rootNodes){
            Segment segment = new Segment();
            segment.setNodes(BFS.BFS(rootNode, segment));

            segment.setRootNode(rootNode);
            segments.add(segment);



        }

        individuals.add(new Individual(segments, nodes));


        }

        double bestEdge = Integer.MAX_VALUE;
        double bestOverall = Integer.MAX_VALUE;
        Individual bestI = individuals.get(0);

        for(Individual individual : individuals){
            if(individual.getEdgeValue() < bestEdge && individual.getOverallDeviation() < bestOverall){
                bestEdge = individual.getEdgeValue();
                bestOverall = individual.getOverallDeviation();
                bestI = individual;
            }
        }


        for(Segment segment : bestI.getSegments()){
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
