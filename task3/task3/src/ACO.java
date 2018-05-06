import java.util.*;

public class ACO {

    public static ArrayList<Machine> run(int optimalValue) {

        HashMap<ArrayList<Integer>, Edge> edges = new HashMap<>();

        PriorityQueue<Ant> population = new PriorityQueue();

        Ant bestAnt = new Ant(edges, new Gene());

        while(true){

            for (int j = 0; j < Constants.antPopulationSize; j++) {
                population.add(new Ant(edges, bestAnt.getGene()));
            }

            if(population.peek().getScore() < bestAnt.getScore()){
                bestAnt = population.peek();
            }

            if(bestAnt.getScore() <= optimalValue){
                System.out.println("Ant algorithm best makespan: " + population.peek().getScore());
                return population.peek().getSolution();
            }

            updateEdges(edges, population.peek().getPath());

        }
    }

    private static void updateEdges(HashMap<ArrayList<Integer>, Edge> edges, ArrayList<Edge> path) {
        for(Edge edge : edges.values()){
            edge.evaporate();
        }

        for(Edge edge : path){
            edge.updateSingle();
        }
    }
}
