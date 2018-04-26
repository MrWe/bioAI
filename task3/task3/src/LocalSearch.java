
import java.util.*;
import java.util.SplittableRandom;


public class LocalSearch {

    ArrayList<Machine> bestMachines;
    LocalSearchSolution bestSolution;

    public LocalSearch(int optimalValue) {
        Randrun(optimalValue);
    }

    public LocalSearch(Gene start){
        run(start);
    }

    private void run(Gene start){
        PriorityQueue<LocalSearchSolution> open = new PriorityQueue<>();
        LinkedList<LocalSearchSolution> closed = new LinkedList<>();

        int bestScore = Integer.MAX_VALUE;

        for (int i = 0; i < 40; i++) {
            open.add(new LocalSearchSolution(start));
        }

        for (int i = 0; i < 30; i++) {

            if(open.isEmpty()){
                for (int k = 0; k < 5; k++) {
                    open.add(new LocalSearchSolution(new Gene(ImportJobs.numJobs, ImportJobs.numMachines)));
                }
            }

            LocalSearchSolution current = open.poll();
            closed.add(current);

            ArrayList<LocalSearchSolution> neighbours = createNeighbours(current);

            for (LocalSearchSolution as : neighbours){

                if(as.getScore() < bestScore){
                    bestScore = as.getScore();
                    bestMachines = as.getMachines();
                    bestSolution = as;

                    //System.out.println("Fant ny best! " + bestScore + " Optimal: " + optimalValue);

                }

                if(!closed.contains(as)){
                    open.add(as);
                }
            }
        }

    }


    private void Randrun(int optimalValue){
        PriorityQueue<LocalSearchSolution> open = new PriorityQueue<>();


        int bestScore = Integer.MAX_VALUE;

        for (int i = 0; i < 100; i++) {
            open.add(new LocalSearchSolution(new Gene(ImportJobs.numJobs, ImportJobs.numMachines)));
        }

        for (int i = 0; i < 100000; i++) {

            if(open.isEmpty()){
                for (int k = 0; k < 5; k++) {
                    open.add(new LocalSearchSolution(new Gene(ImportJobs.numJobs, ImportJobs.numMachines)));
                }
            }

            LocalSearchSolution current = open.poll();

            SplittableRandom r = new SplittableRandom();

            if(r.nextDouble() < 0.005){

                //if(open.size() > 500) {
                    open = new PriorityQueue<>();
                //}

            }

            if(r.nextDouble() < 0.03){
                current = createNeighbours(current).get(0);
            }


            ArrayList<LocalSearchSolution> neighbours = createNeighbours(current);

            for (LocalSearchSolution as : neighbours){

                if(as.getScore() < bestScore){
                    bestScore = as.getScore();
                    bestMachines = as.getMachines();

                    System.out.println("Fant ny best! " + bestScore + " Optimal: " + optimalValue);

                }

                open.add(as);
            }

        }


        System.out.println(bestScore);
    }



    private ArrayList<LocalSearchSolution> createNeighbours(LocalSearchSolution current) {
        final ArrayList<LocalSearchSolution> neighbours = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SplittableRandom r = new SplittableRandom();
            ArrayList<Integer> sequence = new ArrayList<>(current.getGene().getQueue());

            int iter = r.nextInt(3)+1;

            for (int j = 0; j < iter; j++) {

                int r1 = r.nextInt(sequence.size());
                int r2 = r.nextInt(sequence.size());

                Collections.swap(sequence, r1, r2);
            }

            neighbours.add(new LocalSearchSolution(new Gene(sequence)));
        }
        return neighbours;
    }
}

class LocalSearchSolution implements Comparable<LocalSearchSolution> {

    private int score;
    private ArrayList<Machine> machines;
    private Gene gene;
    private String hash;

    public LocalSearchSolution(Gene gene) {
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

    public void setMachines(ArrayList<Machine> machines) {
        this.machines = machines;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int val) {
        this.score = val;
    }

    @Override
    public int compareTo(LocalSearchSolution o) {
        return this.getScore()-o.getScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalSearchSolution that = (LocalSearchSolution) o;
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

    public String getHash() {
        return hash;
    }

}
