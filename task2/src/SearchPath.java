import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class SearchPath {
    private PriorityQueue<Node> pqueue;
    private HashSet<Node> pqueueHash;


    SearchPath(Node start){
        pqueue = new PriorityQueue<>();
        pqueueHash = new HashSet<>();

        //Set initial condition
        start.setCost(0);
        pqueue.offer(start);

    }

    private ArrayList<Node> dijkstra(ArrayList<Node> closed, BufferedImage img, Centroid centroid, ArrayList<ArrayList<Node>> nodes){
        if(!pqueue.isEmpty()){
            //Set current to best possible node in priority queue, comparison can be found i Node class
            Node current = pqueue.poll();
            pqueueHash.remove(current);
            if(current.isClosed()){
                return closed;
            }
            closed.add(current);
            current.setClosed(true);

            centroid.addNode(current);


            current.setNeighbours(addNeighbours(current, nodes));
            current.setBelongsToCentroid(centroid); //OBS! This may be wrong
            for (int i = 0; i < current.getNeighbours().size(); i++) {
                Node neighbour = current.getNeighbours().get(i);

                double tentative_g_score = (Helpers.PlanarEuclideanDistance(centroid.getX(), centroid.getY(), neighbour.getX(), neighbour.getY())) + Helpers.ColorEuclideanDistance(centroid.getColor(), neighbour.getColor());

                //If neighbours is in closed list; ignore it
                if(neighbour.isClosed() && tentative_g_score > neighbour.getCost()){
                    continue;
                }

                //If open list contains neighbour OR it is better, set parent to current node.
                if(!pqueueHash.contains(neighbour) || tentative_g_score < neighbour.getCost()){
                    current.setBelongsToCentroid(centroid);
                    centroid.addNode(current);
                    neighbour.setParent(current);
                    neighbour.setCost(tentative_g_score);
                    neighbour.setBelongsToCentroid(centroid); //OBS! This may be wrong
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


    public ArrayList<Node> runOneStep(ArrayList<Node> closed, BufferedImage img, Centroid centroid, ArrayList<ArrayList<Node>> nodes){
        return dijkstra(closed, img, centroid, nodes);
    }


    private ArrayList<Node> addNeighbours(Node node, ArrayList<ArrayList<Node>> nodes){
        ArrayList<Node> neighbours = new ArrayList<>();
        try{
            Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY()-1));
            if(!neighbour.isClosed()){
                neighbours.add(neighbour);
            }

        }
        catch(Exception ignored){
        }
        try{
            Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY()+1));
            if(!neighbour.isClosed()){
                neighbours.add(neighbour);
            }
        }
        catch(Exception ignored){
        }
        try{
            Node neighbour = nodes.get((int) node.getX()-1).get((int) (node.getY()));
            if(!neighbour.isClosed()){
                neighbours.add(neighbour);
            }
        }
        catch(Exception ignored){
        }
        try{
            Node neighbour = nodes.get((int) node.getX()+1).get((int) (node.getY()));
            if(!neighbour.isClosed()){
                neighbours.add(neighbour);
            }
        }
        catch(Exception ignored){
        }


        return neighbours;
    }




}