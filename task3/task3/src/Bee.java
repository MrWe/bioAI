import java.util.ArrayList;

public class Bee{

    Gene gene;
    int numJobs;
    int numMachines;
    ArrayList<ArrayList<String>> stringJobs;
    ArrayList<Machine> machines;
    int err;
    int status; // 0 = inactive, 1 = active, 2 = scout
    int optimalValue;
    int makespan;

    public Bee(int numJobs, int numMachines, ArrayList<ArrayList<String>> stringJobs, int optimalValue){
        this.gene = new Gene(numJobs, numMachines);
        this.numJobs = numJobs;
        this.numMachines = numMachines;
        this.stringJobs = stringJobs;
        this.machines = createMachines();
        this.optimalValue = optimalValue;
        this.makespan = Helper.getMakeSpan(this.machines);
        this.err = optimalValue - makespan;
        this.status = 0;
    }

    public Bee(int numJobs, int numMachines, ArrayList<ArrayList<String>> stringJobs, int optimalValue, Gene gene){
        this.gene = gene;
        this.numJobs = numJobs;
        this.numMachines = numMachines;
        this.stringJobs = stringJobs;
        this.machines = createMachines();
        this.makespan = Helper.getMakeSpan(this.machines);
        this.err = optimalValue - makespan;
        this.status = 0;
    }

    public int getMakespan() {
        return makespan;
    }

    public void setMakespan(int makespan) {
        this.makespan = makespan;
    }

    public int getOptimalValue() {
        return optimalValue;
    }

    public Gene getGene() {
        return gene;
    }

    public int getNumJobs() {
        return numJobs;
    }

    public int getNumMachines() {
        return numMachines;
    }

    public ArrayList<ArrayList<String>> getStringJobs() {
        return stringJobs;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }

    public int getErr() {
        return err;
    }

    private ArrayList<Machine> createMachines(){
        ArrayList<Machine> machines = new ArrayList<>();
        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < numMachines; i++) {
            machines.add(new Machine());
        }

        for (int i = 0; i < numJobs; i++) {
            jobs.add(new Job(stringJobs.get(i), i));
        }

        for (Integer index : gene.queue) {
            SubJob subJob = jobs.get(index).pop();
            machines.get(subJob.getMachineIndex()).add(subJob);
        }

        return machines;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
