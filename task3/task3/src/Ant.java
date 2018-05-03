import java.util.*;

import static java.lang.Integer.max;
import static java.lang.Math.min;

public class Ant {

    private Gene gene;
    private ArrayList<Job> jobs;
    private ArrayList<Machine> solution;
    private int score;
    private HashMap<Double[], Edge> edges;


    Ant(HashMap<ArrayList<Double>, Edge> edges) {
        createEmptyMachines();


        this.edges = edges;

//      this.score = Helper.getMakeSpan(this.solution);

        run();
    }

    private void createEmptyMachines() {
        ArrayList<Machine> machines = new ArrayList<>();
        jobs = new ArrayList<>();

        for (int i = 0; i < ImportJobs.numMachines; i++) {
            machines.add(new Machine());
        }

        setSolution(machines);

        for (int i = 0; i < ImportJobs.numJobs; i++) {
            jobs.add(new Job(ImportJobs.stringJobs.get(i), i));
        }
    }

    private void addSubJobToMachine(Job job){
        SubJob subJob = job.pop();
        solution.get(subJob.getMachineIndex()).add(subJob);
    }

    private void run(){
        Random r = new Random();
        int next = r.nextInt(ImportJobs.numJobs);

        for (int i = 0; i < (ImportJobs.numJobs * ImportJobs.numMachines)-1; i++) {
            addSubJobToMachine(this.jobs.get(next));
            next = sumProbability(this.jobs.get(next));
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
        firstAvailableSlot =  min(subJob.getParent().getTotalTime(), firstAvailableSlot);

        return (1 / Math.pow(firstAvailableSlot + subJob.getDuration(), Constants.beta)); // apparently we don't want this between 0 and 1
        //return (Math.pow(firstAvailableSlot + subJob.getDuration(), Constants.beta));
    }


    private double probability(Job from, Job to) {
        double pheromoneValue = Math.pow(this.pheromoneMatrix.get(from.getCurrSubJob().getPheromoneMatrixIndex(), to.getCurrSubJob().getPheromoneMatrixIndex()), Constants.alpha);
        double heuristic = getHeuristic(to.getCurrSubJob());
        return pheromoneValue * heuristic;
    }

    private int sumProbability(Job from){
        ArrayList<Double> jProbabilities = new ArrayList<>();
        for(Job to : jobs){
            double ijProbability;
            if(!to.isFinished()){
                ijProbability = probability(from, to);
            } else {
                jProbabilities.add(0.0);
                continue;
            }
            double pSum = 0.0;
            for(Job l : jobs){
                if(!l.isFinished() && !l.equals(from)){
                    pSum += probability(from, l);
                }
            }
            jProbabilities.add(ijProbability / pSum);
        }
        //System.out.println(jProbabilities);
        //System.out.println("Winner:" + assignWinner(jProbabilities));
        return assignWinner(jProbabilities);
    }

    private int assignWinner(ArrayList<Double> distribution){ // Assign a winning node from a probability distribution
        Random r = new Random();
        double n = r.nextDouble();

        int index = 0;
        while(n > distribution.get(index)){
            n -= distribution.get(index);
            index++;
        }

        return distribution.indexOf(Collections.max(distribution));
        //return index;
    }


    public Gene getGene() {
        return gene;
    }

}





