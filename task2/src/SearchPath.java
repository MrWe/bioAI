import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class SearchPath {
    private PriorityQueue<Node> pqueue;
    private HashSet<Node> pqueueHash;


    public SearchPath(Node start){
        pqueue = new PriorityQueue<>();
        pqueueHash = new HashSet<>();

        //Set initial condition
        start.setCost(0);
        pqueue.offer(start);

    }


    public PriorityQueue<Node> getPqueue() {
        return pqueue;
    }

    public void setPqueue(PriorityQueue<Node> pqueue) {
        this.pqueue = pqueue;
    }

    public HashSet<Node> getPqueueHash() {
        return pqueueHash;
    }

    public void setPqueueHash(HashSet<Node> pqueueHash) {
        this.pqueueHash = pqueueHash;
    }

    public HashSet<Node> dijkstra(HashSet<Node> closed, BufferedImage img, Centroid centroid, ArrayList<ArrayList<Node>> nodes){

        //While will run until all possible nodes are checked, even if solution is not found
        if(!pqueue.isEmpty()){
            //Set current to best possible node in priority queue, comparison can be found i Node class
            Node current = pqueue.poll();
            pqueueHash.remove(current);

            closed.add(current);
            current.setClosed(true);
            current.setBelongsToCentroid(centroid);
            centroid.addNode(current);


            //else add neighbours to current node
            current.setNeighbours(addNeighbours(current, img, nodes));
            for (int i = 0; i < current.getNeighbours().size(); i++) {
                Node neighbour = current.getNeighbours().get(i);
                //Ignore node if it is a wall

                double tentative_g_score = (PlanarEuclideanDistance(centroid.getX(), centroid.getY(), neighbour.getX(), neighbour.getY())) + ColorEuclideanDistance(centroid.getColor(), neighbour.getColor());

                //If neighbours is in closed list and its score are worse; ignore it
                if(neighbour.isClosed()){
                    continue;
                }

                //If open list contains neighbour OR it is better, set parent to current node.
                if(!pqueueHash.contains(neighbour) || tentative_g_score < neighbour.getCost()){
                    current.setBelongsToCentroid(centroid);
                    centroid.addNode(current);
                    neighbour.setParent(current);
                    neighbour.setCost(tentative_g_score);

                }
                // Add to open list if not already there
                if(!pqueueHash.contains(neighbour)){
                    pqueue.offer(neighbour);
                    pqueueHash.add(neighbour);
                }
            }

        }
       return closed;
    }


    public HashSet<Node> runOneStep(HashSet<Node> closed, BufferedImage img, Centroid centroid, ArrayList<ArrayList<Node>> nodes){
        return dijkstra(closed, img, centroid, nodes);
    }


    public ArrayList<Node> addNeighbours(Node node, BufferedImage img, ArrayList<ArrayList<Node>> nodes){
        ArrayList<Node> neighbours = new ArrayList<Node>();
        try{
            neighbours.add(nodes.get((int) node.getX()).get((int) (node.getY()-1)));
        }
        catch(Exception ignored){
        }
        try{
            neighbours.add(nodes.get((int) node.getX()-1).get((int) (node.getY())));
        }
        catch(Exception ignored){
        }
        try{
            neighbours.add(nodes.get((int) node.getX()).get((int) (node.getY()+1)));
        }
        catch(Exception ignored){
        }
        try{
            neighbours.add(nodes.get((int) node.getX()+1).get((int) (node.getY())));
        }
        catch(Exception ignored){
        }


        return neighbours;
    }


    /*private ArrayList<Node> addNeighbours(Node current, BufferedImage img) {
        ArrayList<Node> neighbours = new ArrayList<>();
        int x = (int)current.getX();
        int y = (int)current.getY();
        int neighbourX = x;
        int neighbourY = y-1;
        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Node(neighbourX, neighbourY, new Color(img.getRGB(neighbourX, neighbourY))));
        }
        neighbourX = x-1;
        neighbourY = y;
        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Node(neighbourX, neighbourY, new Color(img.getRGB(neighbourX, neighbourY))));
        }
        neighbourX = x;
        neighbourY = y+1;
        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Node(neighbourX, neighbourY, new Color(img.getRGB(neighbourX, neighbourY))));
        }
        neighbourX = x+1;
        neighbourY = y;
        //make sure it is within  grid
        if(withinGrid (neighbourX, neighbourY, img)) {
            neighbours.add(new Node(neighbourX, neighbourY, new Color(img.getRGB(neighbourX, neighbourY))));
        }
        return neighbours;
    }*/

    private boolean withinGrid(int colNum, int rowNum, BufferedImage img) {

        if((colNum < 0) || (rowNum <0) ) {
            return false;    //false if row or col are negative
        }
        if((colNum >= img.getWidth()) || (rowNum >= img.getHeight())) {
            return false;    //false if row or col are > 75
        }
        return true;
    }

    private double ColorEuclideanDistance(Color c0, Color c1) {
        //return Math.sqrt((Math.pow(c0.getRed(), 2) - Math.pow(c1.getRed(), 2)) + (Math.pow(c0.getGreen(), 2) - Math.pow(c1.getGreen(), 2)) + (Math.pow(c0.getBlue(), 2) - Math.pow(c1.getBlue(), 2)));
        double a = c0.getRed() - c1.getRed();
        double b = c0.getGreen() - c1.getGreen();
        double c = c0.getBlue() - c1.getBlue();

        a = (a < 0) ? a *-1 : a;
        b =  (b < 0) ? b *-1 : b;
        c = (c < 0) ? c*-1 : c;
        return a + b + c;
        //return Math.pow(c0.getRed() - c1.getRed(), 2) +  Math.pow(c0.getGreen() - c1.getGreen(), 2) + Math.pow(c0.getBlue() - c1.getBlue(), 2);
    }

    private double PlanarEuclideanDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt((Math.pow(x0, 2) - Math.pow(x0, 2)) + (Math.pow(x1, 2) - Math.pow(x1, 2)));

        //return Math.pow(c0.getRed() - c1.getRed(), 2) +  Math.pow(c0.getGreen() - c1.getGreen(), 2) + Math.pow(c0.getBlue() - c1.getBlue(), 2);
    }






}