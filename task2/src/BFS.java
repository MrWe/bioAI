import java.util.ArrayList;
import java.util.LinkedList;

public class BFS {


    public static ArrayList<Node> BFS(Node startNode){

        LinkedList<Node> BFSQueue = new LinkedList<>();
        ArrayList<Node> closed = new ArrayList<>();


        //Set initial condition
        BFSQueue.add(startNode);
        Node current;
        System.out.println(startNode.getChildren());
        //While will run until all possible nodes are checked, even if solution is not found
        while(!BFSQueue.isEmpty()){
            //Set current to first in open list
            current = BFSQueue.pop();

            closed.add(current);

            //else add neighbours to current node
            ArrayList<Node> children = current.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);

                //If neighbours is in closed list; ignore it
                if(closed.contains(child) || child.isRoot()){
                    continue;
                }

                //If open list does not contains neighbour
                if(!BFSQueue.contains(child)){
                    BFSQueue.offer(child);
                }
            }
        }
    return closed;
    }
}
