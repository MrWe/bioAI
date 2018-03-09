import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BFS {

    public static ArrayList<Node> BFS(Node startNode) {

        ArrayList<Node> closed = new ArrayList<>();

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        executorService.execute(() -> {

            final LinkedList<Node> BFSQueue = new LinkedList<>();

            //Set initial condition
            BFSQueue.add(startNode);

            //While will run until all possible nodes are checked, even if solution is not found
            while (!BFSQueue.isEmpty()) {
                //Set current to first in open list
                final Node current = BFSQueue.pop();

                closed.add(current);

                //else add neighbours to current node
                final ArrayList<Node> children = current.getChildren();
                for (int i = 0; i < children.size(); i++) {
                   final Node child = children.get(i);

                    //If neighbours is in closed list; ignore it
                    if (closed.contains(child) || child.isRoot()) {
                        continue;
                    }

                    //If open list does not contains neighbour
                    if (!BFSQueue.contains(child)) {
                        BFSQueue.offer(child);
                    }
                }
            }
        });

        executorService.shutdown();
        while(!executorService.isTerminated()){}
        return closed;
    }
}
