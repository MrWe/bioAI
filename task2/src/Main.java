import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        /*
        Change these
         */
        String path = "1";
        int numCentroids = 50;


        BufferedImage img = readImage(path);


        ArrayList<Centroid> centroids = initCentroids(img, numCentroids);
        ArrayList<ArrayList<Node>> nodes = initNodes(img);
        ArrayList<Node> startNodes = getStartNodes(centroids, nodes);
        ArrayList<SearchPath> searches = new ArrayList<>();


        for (Node n : startNodes){
            searches.add(new SearchPath(n));
        }

        ArrayList<Node> closedList = new ArrayList<>();

        while(closedList.size() < img.getWidth() * img.getHeight())
        {
            for (int i = 0; i < searches.size(); i++) {
                closedList = searches.get(i).runOneStep(closedList, img, centroids.get(i), nodes);
            }
        }

        for(Centroid c : centroids){
            for(Node n : c.getcurrentlyAssignedNodes()){
                img = changeImage(img, n, c);
            }
        }

        overallDeviation(centroids);
        edgeValue(centroids);

        System.out.println(centroids.get(0).getOverallDeviation());
        System.out.println(centroids.get(0).getEdgeValue());

        writeImage(path, img);

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println("Seconds used to execute: "+totalTime / 1000000000);


    }


    /*
    We want to minimize this
     */
    static void overallDeviation(ArrayList<Centroid> centroids){
        for (Centroid centroid : centroids){
            double fitness = 0;
            for (Node node : centroid.getcurrentlyAssignedNodes()) {
                fitness += Helpers.ColorEuclideanDistance(node.getColor(), centroid.getColor());
            }
            centroid.setOverallDeviation(fitness);
        }
    }


    /*
    We want to maximize this
     */
    static void edgeValue(ArrayList<Centroid> centroids){
        for(Centroid centroid : centroids){
            double fitness = 0;
            for(Node node : centroid.getcurrentlyAssignedNodes()){
                for(Node neighbour : node.getNeighbours()){
                    if(node.getBelongsToCentroid().getHash().equals(neighbour.getBelongsToCentroid().getHash())){
                        continue;
                    }
                    fitness += Helpers.ColorEuclideanDistance(node.getColor(), neighbour.getColor());
                }
            }
            centroid.setEdgeValue(fitness);
        }
    }

    static BufferedImage readImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(String.format("TestImages/%s/Test image.jpg", path)));
            //TestImages/7/Test image.jpg

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

    static ArrayList<ArrayList<Node>> initNodes(BufferedImage img){
        ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
        for (int i = 0; i < img.getWidth(); i++) {
            nodes.add(new ArrayList<Node>());
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = new Color(img.getRGB(i,j));
                nodes.get(i).add(new Node(i,j,c));
            }
        }
        return nodes;
    }

    static ArrayList<Node> getStartNodes(ArrayList<Centroid> centroids, ArrayList<ArrayList<Node>> nodes){
        ArrayList<Node> startNodes = new ArrayList<>();

        for(Centroid c : centroids){
            startNodes.add(nodes.get((int) c.getX()).get((int) c.getY()));
        }
        return startNodes;
    }

    static ArrayList<Centroid> initCentroids(BufferedImage img, int num_centroids){

        Random r = new Random();

        HashSet<String> selected = new HashSet<>();

        ArrayList<Centroid> centroids = new ArrayList<>();
        for (int n = 0; n < num_centroids; n++){
            int x = r.nextInt(img.getWidth());
            int y = r.nextInt(img.getHeight());
            String s = x+""+y;
            int counter = 0;
            while(selected.contains(s) && counter < 1000){
                x = r.nextInt(img.getWidth());
                y = r.nextInt(img.getHeight());
                s = x+""+y;
                counter += 1;
            }
            if(counter >= 999){
                break;
            }

            selected.add(s);

            Color c = new Color(img.getRGB(x,y));
            centroids.add(new Centroid(x,y,c));
        }
        return centroids;
    }

    static BufferedImage changeImage(BufferedImage img, Node node, Centroid centroid) {

        Color c = new Color(centroid.getColor().getRGB());

        img.setRGB((int)node.getX(), (int)node.getY(), c.getRGB());

        return img;
    }
}
