
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;


public class SearchPath {

    private ClosedSet closed; //TODO: Send as arg
    private PriorityQueue<Node> pqueue;
    private HashSet<String> pqueueHash;

    
    public SearchPath(Node start){
        pqueue = new PriorityQueue<>();
        pqueueHash = new HashSet<>();

        //Set initial condition
        start.setCost(0);
        pqueue.offer(start);

    }


    public ClosedSet getClosed() {
        return closed;
    }

    public void setClosed(ClosedSet closed) {
        this.closed = closed;
    }

    public PriorityQueue<Node> getPqueue() {
        return pqueue;
    }

    public void setPqueue(PriorityQueue<Node> pqueue) {
        this.pqueue = pqueue;
    }

    public HashSet<String> getPqueueHash() {
        return pqueueHash;
    }

    public void setPqueueHash(HashSet<String> pqueueHash) {
        this.pqueueHash = pqueueHash;
    }

    public ClosedSet dijkstra(ClosedSet closed, BufferedImage img, Centroid centroid){


        //While will run until all possible nodes are checked, even if solution is not found
        if(!pqueue.isEmpty()){
            //Set current to best possible node in priority queue, comparison can be found i Node class
            Node current = pqueue.poll();
            pqueueHash.remove(current.getHash());
            current.setBelongsToCentroid(centroid);
            centroid.addNode(current);
            closed.add(current);

            //else add neighbours to current node
            current.setNeighbours(addNeighbours(current, img));
            for (int i = 0; i < current.getNeighbours().size(); i++) {
            Node neighbour = current.getNeighbours().get(i);
            //Ignore node if it is a wall

                double tentative_g_score = current.getCost() + euclideanDistance(centroid.getColor(), neighbour.getColor());

                //If neighbours is in closed list and its score are worse; ignore it
                if(closed.contains(neighbour) && tentative_g_score > neighbour.getCost()){
                    continue;
                }

                //If open list contains neighbour OR it is better, set parent to current node.
                if(!pqueueHash.contains(neighbour.getHash()) || tentative_g_score < neighbour.getCost()){

                    neighbour.setParent(current);
                    neighbour.setCost(tentative_g_score);

                }
                // Add to open list if not already there
                if(!pqueueHash.contains(neighbour.getHash())){
                    pqueue.offer(neighbour);
                    pqueueHash.add(neighbour.getHash());
                }
            }
        }
        return closed;
    }


    public ClosedSet runOneStep(ClosedSet closed, BufferedImage img, Centroid centroid){
        return dijkstra(closed, img, centroid);
    }


    public ArrayList<Node> addNeighbours(Node node, BufferedImage img){
        ArrayList<Node> neighbours = new ArrayList<Node>();
        try{
            neighbours.add(new Node(node.getX(), node.getY()-1, new Color(img.getRGB((int)node.getX(), (int)node.getY()-1))));
        }
        catch(Exception ignored){
        }
        try{
            neighbours.add(new Node(node.getX()-1, node.getY(), new Color(img.getRGB((int)node.getX()-1, (int)node.getY()))));
        }
        catch(Exception ignored){
        }
        try{
            neighbours.add(new Node(node.getX(), node.getY()+1, new Color(img.getRGB((int)node.getX(), (int)node.getY()+1))));
        }
        catch(Exception ignored){
        }
        try{
            neighbours.add(new Node(node.getX()+1, node.getY(), new Color(img.getRGB((int)node.getX()+1, (int)node.getY()))));
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

    private double euclideanDistance(Color c0, Color c1) {
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


}
