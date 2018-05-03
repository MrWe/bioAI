import java.util.*;

import static java.lang.Integer.max;

public class Ant {

    private Gene gene;
    private ArrayList<Job> jobs;
    private ArrayList<Machine> solution;
    private int score;
    private PheromoneMatrix pheromoneMatrix;


    Ant(PheromoneMatrix pheromoneMatrix) {
        createEmptyMachines();


        this.pheromoneMatrix = pheromoneMatrix;

//      this.score = Helper.getMakeSpan(this.solution);

        sumProbability(this.jobs.get(0));
    }


    private void createEmptyMachines() {
        ArrayList<Machine> machines = new ArrayList<>();
        jobs = new ArrayList<>();

        for (int i = 0; i < ImportJobs.numMachines; i++) {
            machines.add(new Machine());
        }

        setSolution(machines);

        System.out.println(ImportJobs.numJobs);

        System.out.println(ImportJobs.stringJobs);

        for (int i = 0; i < ImportJobs.numJobs; i++) {
            jobs.add(new Job(ImportJobs.stringJobs.get(i), i));
        }

    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public ArrayList<Machine> getSolution() {
        return solution;
    }

    public int getScore() {
        return score;
    }

    public void setSolution(ArrayList<Machine> solution){
        this.solution = solution;
    }

    private void constructGeneFromMatrix() {
    }

    private double getHeuristic(SubJob subJob){
        int firstAvailableSlot = solution.get(subJob.getMachineIndex()).getFirstAvailableSlot();
        firstAvailableSlot =  max(subJob.getParent().getTotalTime(), firstAvailableSlot);

        //return (1 / Math.pow(firstAvailableSlot + subJob.getDuration(), Constants.beta)); // apparently we don't want this between 0 and 1
        return (Math.pow(firstAvailableSlot + subJob.getDuration(), Constants.beta));
    }


    private double probability(Job from, Job to) {
        double pheromoneValue = Math.pow(this.pheromoneMatrix.get(from.getCurrSubJob().getPheromoneMatrixIndex(), to.getCurrSubJob().getPheromoneMatrixIndex()), Constants.alpha);
        double heuristic = getHeuristic(to.getCurrSubJob());

        return pheromoneValue * heuristic;
    }

    private double sumProbability(Job from){
        ArrayList<Double> jProbabilities = new ArrayList<>();
        for(Job to : jobs){
            double ijProbability = 0.0;
            if(!to.isFinished()){
                ijProbability = probability(from, to);
            } else {
                jProbabilities.add(0.0);
                continue;
            }
            double pSum = 0.0;
            for(Job l : jobs){
                if(!l.isFinished()){
                    pSum += probability(from, l);
                }
            }
            jProbabilities.add(ijProbability / pSum);
        }
        System.out.println(jProbabilities);
        return 0.0;
    }


    public Gene getGene() {
        return gene;
    }

    public static void main(String[] args){
        ImportJobs imports = new ImportJobs("Data/1.txt");
        int n = ImportJobs.numMachines * ImportJobs.numJobs;


        Ant ant = new Ant(new PheromoneMatrix((n*(n-1)) / 2, 0.5));
        //ant.sumProbability(ant.getJobs().get(0));
    }

}





