import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ACO {

    static int numJobs;

    public static ArrayList<Machine> run(ImportJobs imports, int optimalValue) {

        //ImportJobs imports = new ImportJobs("Data/1.txt");
        numJobs = imports.numJobs;
        ArrayList<Machine> machines;

        int best = Integer.MAX_VALUE;
        ArrayList<Machine> bestM = new ArrayList<>();

        machines = new ArrayList<>();
        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < imports.numMachines; i++) {
            machines.add(new Machine());
        }

        for (int i = 0; i < imports.numJobs; i++) {
            jobs.add(new Job(imports.stringJobs.get(i), i));
        }

        Gene gene = new Gene(numJobs, imports.numMachines);


        for (Integer index : gene.queue) {
            SubJob subJob = jobs.get(index).pop();
            machines.get(subJob.getMachineIndex()).add(subJob);
        }

        for (Machine m : machines) {

            List<Integer> keys = new ArrayList(m.getSubJobs().keySet());

            Collections.sort(keys);

            for (int time : keys) {
                SubJob sj = m.getSubJobs().get(time);
                //System.out.print(sj.getParent().getIndex() + "-" + sj.getStartTime() + "-" + sj.getDuration() + " ");
            }
            //System.out.println();
        }

        int curr = Helper.getMakeSpan(machines);

        if (curr < best) {
            best = curr;
            bestM = machines;
        }

        System.out.println(best);
        return bestM;

    }

    private void updatePheromoneMatrix(Ant ant, PheromoneMatrix pheromoneMatrix, ArrayList<Job> jobs){

        resetJobIndices(jobs);

        pheromoneMatrix.evaporate();

        ArrayList<Integer> queue = ant.getGene().getQueue();
        for (int i = 0; i < queue.size()-1; i++) {
            int row = jobs.get(queue.get(i)).pop().getPheromoneMatrixIndex();
            int col = jobs.get(queue.get(i+1)).pop().getPheromoneMatrixIndex();

            pheromoneMatrix.updateSingle(row, col, 0.5);

        }
    }

    private void resetJobIndices(ArrayList<Job> jobs){
        for(Job j : jobs){
            j.resetSubJobIndex();
        }
    }
}
