import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logic {

    int numJobs;

    public ArrayList<Machine> run() {
        ImportJobs imports = new ImportJobs("Data/1.txt");
        numJobs = imports.numJobs;
        ArrayList<Machine> machines = new ArrayList<>();

        int best = Integer.MAX_VALUE;
        ArrayList<Machine> bestM = new ArrayList<>();

        for (int N = 0; N < 100; N++) {


            machines = new ArrayList<>();
            ArrayList<Job> jobs = new ArrayList<>();

            for (int i = 0; i < imports.numMachines; i++) {
                machines.add(new Machine());
            }

            for (int i = 0; i < imports.numJobs; i++) {
                jobs.add(new Job(imports.stringJobs.get(i), i));
            }

            Individual individual = new Individual(numJobs, imports.numMachines);


            for (Integer index : individual.queue) {
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

            int curr = getMakeSpan(jobs);

            if (curr < best) {
                best = curr;
                bestM = machines;
            }
        }
        System.out.println(best);
        return bestM;

    }


    private int getMakeSpan(ArrayList<Job> jobs) {
        int highest = 0;

        for (Job j : jobs) {
            if (j.getTotalTime() > highest) {
                highest = j.getTotalTime();
            }
        }
        return highest;
    }


}
