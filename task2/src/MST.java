import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

class MST {

    private PriorityQueue<Node> pqueue;
    private HashSet<String> pqueueHash;
    private HashMap<String, Double> memoizer;


    MST(Node start) {
        pqueue = new PriorityQueue<>();
        pqueueHash = new HashSet<>();
        memoizer = new HashMap<>();

        //Set initial condition
        start.setCost(0);
        pqueue.offer(start);



    }

    public ArrayList<ArrayList<Node>> prim(ArrayList<ArrayList<Node>> nodes) {
        while (!pqueue.isEmpty()) {
            //Set current to best possible node in priority queue, comparison can be found i Node class
            Node current = pqueue.poll();

            current.setClosed(true);

            current.setNeighbours(addNeighbours(current, nodes));

            for (int i = 0; i < current.getNeighbours().size(); i++) {
                Node neighbour = current.getNeighbours().get(i);

                double g_score = getG(current, neighbour);

                //If neighbours is in closed list; ignore it
                if (neighbour.isClosed()) {
                    continue;
                }

                // Add to open list if not already there
                if (!pqueueHash.contains(neighbour.hashNode())) {
                    neighbour.setCost(g_score);
                    neighbour.setParent(current);
                    pqueue.offer(neighbour);
                    pqueueHash.add(neighbour.hashNode());
                }

                //If open list contains neighbour OR it is better, set parent to current node.
                else if (g_score < neighbour.getCost()) {
                    neighbour.setParent(current);
                    neighbour.setCost(g_score);
                    pqueue.remove(neighbour);
                    pqueue.offer(neighbour);
                }
            }
        }
        return addChildren(nodes);
    }

    private ArrayList<ArrayList<Node>> addChildren(ArrayList<ArrayList<Node>> nodes) {
        for (ArrayList<Node> row : nodes) {
            for (Node node : row) {
                if (node.getParent() != null) {
                    node.getParent().addChild(node);
                }
            }
        }
        return nodes;
    }

    private double getG(Node current, Node neighbour) {
        String key = current.getColor() + "" + neighbour.getColor();
        String revKey = neighbour.getColor() + "" + current.getColor();

        double g_score;

        if (memoizer.containsKey(key)) {
            g_score = memoizer.get(key);
        } else if (memoizer.containsKey(revKey)) {
            g_score = memoizer.get(revKey);
        } else {
            g_score = Helpers.rgbDistance(current.getColor(), neighbour.getColor());
            memoizer.put(key, g_score);
        }

        return g_score;
    }


    private ArrayList<Node> addNeighbours(Node node, ArrayList<ArrayList<Node>> nodes) {
        ArrayList<Node> neighbours = new ArrayList<>();
        try {
            Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY() - 1));
            neighbours.add(neighbour);
        } catch (Exception ignored) {
        }
        try {
            Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY() + 1));

            neighbours.add(neighbour);

        } catch (Exception ignored) {
        }
        try {
            Node neighbour = nodes.get((int) node.getX() - 1).get((int) (node.getY()));

            neighbours.add(neighbour);

        } catch (Exception ignored) {
        }
        try {
            Node neighbour = nodes.get((int) node.getX() + 1).get((int) (node.getY()));

            neighbours.add(neighbour);

        } catch (Exception ignored) {
        }


        return neighbours;
    }


}