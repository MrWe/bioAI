import java.util.ArrayList;

class Solution implements Comparable<Solution> {

    private int score;
    private ArrayList<Machine> machines;
    private Gene gene;

    Solution(Gene gene) {
        this.gene = gene;
        this.machines = createMachines(gene);
        this.score = Helper.getMakeSpan(this.machines);
    }

    public Gene getGene() {
        return gene;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public int compareTo(Solution o) {
        return this.getScore()-o.getScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution that = (Solution) o;
        return this.gene.getQueue().equals(that.gene.getQueue());
    }


    private ArrayList<Machine> createMachines(Gene gene) {
        ArrayList<Machine> machines = new ArrayList<>();
        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < ImportJobs.numMachines; i++) {
            machines.add(new Machine());
        }

        for (int i = 0; i < ImportJobs.numJobs; i++) {
            jobs.add(new Job(ImportJobs.stringJobs.get(i), i));
        }

        for (Integer index : gene.queue) {
            SubJob subJob = jobs.get(index).pop();
            machines.get(subJob.getMachineIndex()).add(subJob);
        }

        return machines;
    }
}