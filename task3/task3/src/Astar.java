import com.sun.tools.internal.ws.wsdl.document.Import;

import java.util.*;
import java.util.stream.Collectors;
import java.util.SplittableRandom;


public class Astar {

    ArrayList<Machine> bestMachines;
    AstarSolution bestSolution;

    public Astar(int optimalValue) {
        Randrun(optimalValue);

    }

    public Astar(Gene start){
        run(start);
    }

    private void run(Gene start){
        PriorityQueue<AstarSolution> open = new PriorityQueue<>();
        LinkedList<AstarSolution> closed = new LinkedList<>();

        int bestScore = Integer.MAX_VALUE;

        for (int i = 0; i < 5; i++) {
            open.add(new AstarSolution(start));
        }

        for (int i = 0; i < 2; i++) {

            if(open.isEmpty()){
                for (int k = 0; k < 5; k++) {
                    open.add(new AstarSolution(new Gene(ImportJobs.numJobs, ImportJobs.numMachines)));
                }
            }



            AstarSolution current = open.poll();
            closed.add(current);

            ArrayList<AstarSolution> neighbours = createNeighbours(current);

            for (AstarSolution as : neighbours){

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


        //System.out.println(bestScore);
    }


    private void Randrun(int optimalValue){
        PriorityQueue<AstarSolution> open = new PriorityQueue<>();


        int bestScore = Integer.MAX_VALUE;

        for (int i = 0; i < 100; i++) {
            open.add(new AstarSolution(new Gene(ImportJobs.numJobs, ImportJobs.numMachines)));
        }

        for (int i = 0; i < 100000; i++) {

            if(open.isEmpty()){
                for (int k = 0; k < 5; k++) {
                    open.add(new AstarSolution(new Gene(ImportJobs.numJobs, ImportJobs.numMachines)));
                }
            }

            AstarSolution current = open.poll();

            SplittableRandom r = new SplittableRandom();

            if(r.nextDouble() < 0.005){

                //if(open.size() > 500) {
                    open = new PriorityQueue<>();
                //}

            }

            if(r.nextDouble() < 0.03){
                current = createNeighbours(current).get(0);
            }


            ArrayList<AstarSolution> neighbours = createNeighbours(current);

            for (AstarSolution as : neighbours){

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



    private ArrayList<AstarSolution> createNeighbours(AstarSolution current) {
        final ArrayList<AstarSolution> neighbours = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SplittableRandom r = new SplittableRandom();
            ArrayList<Integer> sequence = new ArrayList<>(current.getGene().getQueue());

            int iter = r.nextInt(3)+1;

            for (int j = 0; j < iter; j++) {

                int r1 = r.nextInt(sequence.size());
                int r2 = r.nextInt(sequence.size());

                Collections.swap(sequence, r1, r2);
            }

            neighbours.add(new AstarSolution(new Gene(sequence)));
        }
        return neighbours;
    }
}

class AstarSolution implements Comparable<AstarSolution> {

    private int score;
    private ArrayList<Machine> machines;
    private Gene gene;
    private String hash;

    public AstarSolution(Gene gene) {
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
    public int compareTo(AstarSolution o) {
        return this.getScore()-o.getScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AstarSolution that = (AstarSolution) o;
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
