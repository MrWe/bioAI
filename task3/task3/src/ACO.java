import java.lang.reflect.Array;
import java.util.*;

public class ACO {

    static int numJobs;

    public static ArrayList<Machine> run(int optimalValue) {

        int n = ImportJobs.numMachines * ImportJobs.numJobs;

        //PheromoneMatrix pheromoneMatrix = new PheromoneMatrix( (n*(n-1)) / 2, 0.5);

        HashMap<ArrayList<Integer>, Edge> edges = new HashMap<>();

        int best = Integer.MAX_VALUE;
        ArrayList<Machine> bestM = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            int currBest = Integer.MAX_VALUE;
            Ant currBestAnt = new Ant(edges);

            for (int j = 0; j < 3; j++) {
                Ant a = new Ant(edges);

                if(a.getScore() < currBest){

                    currBest = a.getScore();
                    currBestAnt = a;

                    if(a.getScore() < best){
                        best = a.getScore();
                        bestM = a.getSolution();

                        System.out.println(best);

                        if(best <= optimalValue){
                            //System.out.println("VI VANT!");
                            //System.out.println(best);
                            return bestM;
                        }

                    }
                }
            }
            //updatePheromoneMatrix(currBestAnt, pheromoneMatrix);

            /*Random r = new Random();
            if(r.nextDouble() > 0.999){
                pheromoneMatrix = new PheromoneMatrix((int) Math.pow(ImportJobs.numMachines,2), 0.5);
            }*/


        }

        System.out.println("NEI " + best);
        return bestM;

    }

    private static void updatePheromoneMatrix(Ant ant, PheromoneMatrix pheromoneMatrix){

        resetJobIndices(ant.getJobs());

        pheromoneMatrix.evaporate();

        ArrayList<Integer> queue = ant.getGene().getQueue();
        for (int i = 0; i < queue.size()-1; i++) {

            int row = ant.getJobs().get(queue.get(i)).pop().getPheromoneMatrixIndex();
            int col = ant.getJobs().get(queue.get(i+1)).pop().getPheromoneMatrixIndex();
            ant.getJobs().get(queue.get(i+1)).lowercurrIndexByOne();

            pheromoneMatrix.updateSingle(row, col, 1.2);
        }

    }

    private static void resetJobIndices(ArrayList<Job> jobs){
        for(Job j : jobs){
            j.resetSubJobIndex();
        }
    }
}
