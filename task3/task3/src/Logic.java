import java.util.*;

public class Logic {

    int numJobs;

    public ArrayList<Machine> run(){
        ImportJobs imports = new ImportJobs("Data/2.txt");
        numJobs = imports.numJobs;

        ArrayList<Machine> machines = new ArrayList<>();
        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < imports.numMachines; i++) {
            machines.add(new Machine());
        }

        for (int i = 0; i < imports.numJobs; i++) {
            jobs.add(new Job(imports.stringJobs.get(i), i));
        }
        for (int i = 0; i < imports.numMachines; i++) {
            for(Job job : jobs){
                machines.get(job.getSubJobs().get(i).getMachineIndex()).add(job.getSubJobs().get(i));
            }
        }

        for(Machine m : machines){

           List<Integer> keys = new ArrayList(m.getSubJobs().keySet());

            Collections.sort(keys);

            for(int time : keys){
                SubJob sj = m.getSubJobs().get(time);
                System.out.print(sj.getParent().getIndex() + "-" + sj.getStartTime() + "-" + sj.getDuration() + " ");
            }
            System.out.println();
        }

       return machines;



    }




}
