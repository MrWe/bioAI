import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

class MST {

    private PriorityQueue<Edge> pqueue;
    private HashSet<String> pqueueHash;
    private HashMap<String, Double> memoizer;


    MST(ArrayList<Node> start, ArrayList<ArrayList<Node>> nodes) {
        pqueue = new PriorityQueue<>();
        pqueueHash = new HashSet<>();
        memoizer = new HashMap<>();

        //Set initial condition
        for(Node root : start){
            root.setCost(0);
            pqueue.addAll(addEdges(root, nodes));
        }

    }

    public ArrayList<ArrayList<Node>> prim(ArrayList<ArrayList<Node>> nodes) {
        ArrayList<Edge> edges = new ArrayList<>();
        while (!pqueue.isEmpty()) {
            //Set current to best possible node in priority queue, comparison can be found i Node class
            Edge current = pqueue.poll();

            if(!(current.getN1().isClosed() && current.getN2().isClosed())) { //The edge between N1 and N2 lead to the discovery of a new node (N2)
                edges.add(current);
            }

            current.getN1().setClosed(true);
            current.getN2().setClosed(true);

            pqueue.addAll(addEdges(current.getN1(), nodes));
            pqueue.addAll(addEdges(current.getN2(), nodes));
        }
        return nodes;
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
        //return Helpers.rgbDistance(current.getColor(), neighbour.getColor());

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

    private ArrayList<Edge> addEdges(Node node, ArrayList<ArrayList<Node>> nodes) {
        ArrayList<Edge> edges = new ArrayList<>();
        try {
            Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY() - 1));
            double g = getG(node, neighbour);
            Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }
        try {
            Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY() + 1));
            double g = getG(node, neighbour);
            Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }
        try {
            Node neighbour = nodes.get((int) node.getX()-1).get((int) (node.getY()));
            double g = getG(node, neighbour);
            Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }
        try {
            Node neighbour = nodes.get((int) node.getX()+1).get((int) (node.getY()));
            double g = getG(node, neighbour);
            Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }


        return edges;
    }


}