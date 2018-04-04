import java.util.ArrayList;
import java.util.Random;

public class Ant {

    private Gene gene;

    Ant(int numJobs, int numSubJobs){
        this.gene = new Gene(numJobs, numSubJobs);
    }

    Ant(int numJobs, int numSubJobs, PheromoneMatrix pheromoneMatrix){
        constructGeneFromMatrix(numJobs, numSubJobs, pheromoneMatrix);
    }

    private void constructGeneFromMatrix(int numJobs, int numSubJobs, PheromoneMatrix pheromoneMatrix, ){
        ArrayList<Integer> queue = new ArrayList<>();

        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < numJobs; i++) {
            jobs.add(new Job(ImportJobs.stringJobs.get(i), i)); //TODO: Test if static means its the same for all instances, what is even static? ask trætteberg
        }


        Random r = new Random();

        int start = r.nextInt(numJobs);

        queue.add(start);



    }
    

    public Gene getGene() {
        return gene;
    }
}
