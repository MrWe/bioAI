import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MST {

    public static ArrayList<ArrayList<Node>> prim(ArrayList<ArrayList<Node>> nodes, ArrayList<Node> start, ArrayList<ArrayList<ArrayList<Edge>>> initedges) {

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        executorService.execute(() -> {

            final ArrayList<Edge> edges = new ArrayList<>();
            final PriorityQueue<Edge> pqueue = new PriorityQueue<>();
            final HashSet<String> pqueueHash = new HashSet<>();

            //Set initial condition
            for(final Node root : start){
                root.setCost(0);
                pqueue.addAll(addEdges(root, nodes, initedges));
            }

            while (!pqueue.isEmpty()) {
                //Set current to best possible node in priority queue, comparison can be found i Node class
                final Edge current = pqueue.poll();



                nodes.get(current.getN1X()).get(current.getN1Y()).setClosed(true);
                nodes.get(current.getN2X()).get(current.getN2Y()).setClosed(true);

                pqueue.addAll(addEdges(nodes.get(current.getN1X()).get(current.getN1Y()), nodes, initedges));
                pqueue.addAll(addEdges(nodes.get(current.getN2X()).get(current.getN2Y()), nodes, initedges));


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

    private static ArrayList<Edge> addEdges(Node node, ArrayList<ArrayList<Node>> nodes, ArrayList<ArrayList<ArrayList<Edge>>> initedges) {
        final ArrayList<Edge> edges = new ArrayList<>();

        ArrayList<Edge> neighbourEdges = initedges.get(node.getX()).get(node.getY());

        ArrayList<Edge> edgesToReturn = new ArrayList<>();

        for(Edge e : neighbourEdges){

            if(! (nodes.get(e.getN1X()).get(e.getN1Y()).isClosed() && (nodes.get(e.getN2X()).get(e.getN2Y()).isClosed()))){
                edgesToReturn.add(e);

                nodes.get(e.getN2X()).get(e.getN2Y()).setParent(node);
                node.addChild(nodes.get(e.getN2X()).get(e.getN2Y()));
                nodes.get(e.getN2X()).get(e.getN2Y()).setCost(e.getEdgeCost());
            }
        }



        return edgesToReturn;


        /*


        try {
            final Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY() - 1));
            final double g = getG(node, neighbour);
            final Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }
        try {
            final Node neighbour = nodes.get((int) node.getX()).get((int) (node.getY() + 1));
            final double g = getG(node, neighbour);
            final Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }
        try {
            final  Node neighbour = nodes.get((int) node.getX()-1).get((int) (node.getY()));
            final double g = getG(node, neighbour);
            final Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }
        try {
            final Node neighbour = nodes.get((int) node.getX()+1).get((int) (node.getY()));
            final double g = getG(node, neighbour);
            final Edge edge = new Edge(node, neighbour, g);
            if(!(node.isClosed() && neighbour.isClosed())){

                neighbour.setParent(node);
                node.addChild(neighbour);
                neighbour.setCost(g);

                edges.add(edge);
            }

        } catch (Exception ignored) {
        }


        return edges;
        */
    }


}