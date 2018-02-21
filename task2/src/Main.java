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
        String path = "9";
        int numCentroids = 5;



        /*
        Init image and centroids
         */
        BufferedImage img = readImage(path);


        ArrayList<Centroid> centroids = initCentroids(img, numCentroids);
        ArrayList<ArrayList<Node>> nodes = initNodes(img);
        ArrayList<Node> startNodes = getStartNodes(centroids, nodes);
        ArrayList<SearchPath> searches = new ArrayList<>();






        for (Node n : startNodes){
            searches.add(new SearchPath(n));
        }



        System.out.println(img.getWidth() * img.getHeight());

        HashSet<Node> closedList = new HashSet<>();


        boolean runOneMore = true;
        boolean didRun;
        while(closedList.size() < img.getWidth() * img.getHeight())

        {
            runOneMore = false;

            for (int i = 0; i < searches.size(); i++) {
                closedList = searches.get(i).runOneStep(closedList, img, centroids.get(i), nodes);



            }

        }

        System.out.println(closedList.size());



        for(Centroid c : centroids){
            for(Node n : c.getcurrentlyAssignedNodes()){
                img = changeImage(img, n, c);
            }
        }



        writeImage(path, img);

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println(totalTime / 1000000000);


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



   /* static ArrayList<Node> initNodes(ArrayList<Centroid> centroids){
        ArrayList<Node> nodes = new ArrayList<>();

        for(Centroid c : centroids){
            nodes.add(new Node(c.getX(), c.getY(), c.getColor()));
        }
        return nodes;
    }*/

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

        ArrayList<Centroid> centroids = new ArrayList<>();
        for (int n = 0; n < num_centroids; n++){
            int x = r.nextInt(img.getWidth());
            int y = r.nextInt(img.getHeight());
            Color c = new Color(img.getRGB(x,y));
            centroids.add(new Centroid(x,y,c));
        }
        return centroids;
    }

    /*static ArrayList<Centroid> getWeights(BufferedImage img, ArrayList<Centroid> centroids){
        ArrayList<Point2D> seen = new ArrayList<>();

        for(Centroid c : centroids){
            c.addPixel(new Gene(c, c.getPosition(), getColorFromCoords(img, c.getPosition()), getPixelNeighbours((int)c.getPosition().getX(), (int)c.getPosition().getY(), img)));
            seen.add(c.getPosition());
        }


        while(seen.size() < img.getHeight() * img.getWidth()){
            for(Centroid c : centroids){


            }
        }
        return centroids;
    }*/



    static BufferedImage changeImage(BufferedImage img, Node node, Centroid centroid) {

        Color c = new Color(centroid.getColor().getRGB());

        img.setRGB((int)node.getX(), (int)node.getY(), c.getRGB());

        return img;
    }

    static ArrayList<Point2D> getPixelNeighbours(int  x, int y, BufferedImage img) {
        ArrayList<Point2D> neighbours = new ArrayList<>();


        int neighbourX = x;
        int neighbourY = y-1;

        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Point2D.Double(neighbourX, neighbourY));
        }

        neighbourX = x-1;
        neighbourY = y;

        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Point2D.Double(neighbourX, neighbourY));
        }

        neighbourX = x;
        neighbourY = y+1;

        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Point2D.Double(neighbourX, neighbourY));
        }

        neighbourX = x+1;
        neighbourY = y;

        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Point2D.Double(neighbourX, neighbourY));
        }

        return neighbours;
    }

    static boolean withinGrid(int colNum, int rowNum, BufferedImage img) {

        if((colNum < 0) || (rowNum <0) ) {
            return false;    //false if row or col are negative
        }
        if((colNum >= img.getWidth()) || (rowNum >= img.getHeight())) {
            return false;    //false if row or col are > 75
        }
        return true;
    }

    static Color getColorFromCoords(BufferedImage img, Point2D pix){
        return new Color(img.getRGB((int)pix.getX(), (int)pix.getY()));
    }



}
