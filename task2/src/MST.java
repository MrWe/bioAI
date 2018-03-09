import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MST {

    public static ArrayList<ArrayList<Node>> prim(ArrayList<ArrayList<Node>> nodes, ArrayList<Node> start) {

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        executorService.execute(() -> {

            final ArrayList<Edge> edges = new ArrayList<>();
            final PriorityQueue<Edge> pqueue = new PriorityQueue<>();
            final HashSet<String> pqueueHash = new HashSet<>();

            //Set initial condition
            for(Node root : start){
                root.setCost(0);
                pqueue.addAll(addEdges(root, nodes));
            }

            while (!pqueue.isEmpty()) {
                //Set current to best possible node in priority queue, comparison can be found i Node class
                final Edge current = pqueue.poll();

                if (!(current.getN1().isClosed() && current.getN2().isClosed())) { //The edge between N1 and N2 lead to the discovery of a new node (N2)
                    edges.add(current);
                }

                current.getN1().setClosed(true);
                current.getN2().setClosed(true);

                pqueue.addAll(addEdges(current.getN1(), nodes));
                pqueue.addAll(addEdges(current.getN2(), nodes));
            }
        });
        executorService.shutdown();
        while(!executorService.isTerminated()){}
        return nodes;
    }

    private  static ArrayList<ArrayList<Node>> addChildren(ArrayList<ArrayList<Node>> nodes) {
        for (ArrayList<Node> row : nodes) {
            for (Node node : row) {
                if (node.getParent() != null) {
                    node.getParent().addChild(node);
                }
            }
        }
        return nodes;
    }

    private static double getG(Node current, Node neighbour) {
        return Helpers.rgbDistance(current.getColor(), neighbour.getColor());



    }

    private static ArrayList<Edge> addEdges(Node node, ArrayList<ArrayList<Node>> nodes) {
        final ArrayList<Edge> edges = new ArrayList<>();
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