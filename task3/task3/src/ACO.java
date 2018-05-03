import java.util.*;

public class ACO {

    public static ArrayList<Machine> run(int optimalValue) {

        int n = ImportJobs.numMachines * ImportJobs.numJobs;

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

    private static void updatePheromoneMatrix(Ant ant, PheromoneMatrix pheromoneMatrix) {

        resetJobIndices(ant.getJobs());

        pheromoneMatrix.evaporate();

        ArrayList<Integer> queue = ant.getGene().getQueue();
        for (int i = 0; i < queue.size() - 1; i++) {

            int row = ant.getJobs().get(queue.get(i)).pop().getPheromoneMatrixIndex();
            int col = ant.getJobs().get(queue.get(i + 1)).pop().getPheromoneMatrixIndex();
            ant.getJobs().get(queue.get(i + 1)).lowercurrIndexByOne();

            pheromoneMatrix.updateSingle(row, col, 1.2);
        }

    }

    private static void resetJobIndices(ArrayList<Job> jobs) {
        for (Job j : jobs) {
            j.resetSubJobIndex();
        }
    }
}
