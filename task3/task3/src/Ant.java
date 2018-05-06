import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.min;

public class Ant implements Comparable<Ant> {

    private Gene gene;
    private ArrayList<Job> jobs;
    private ArrayList<Machine> solution;
    private int score;
    private HashMap<ArrayList<Integer>, Edge> edges;
    private ArrayList<Edge> path;
    private ArrayList<Integer> geneQueue;

    Ant(HashMap<ArrayList<Integer>, Edge> edges, Gene parent) {
        path = new ArrayList<>();
        geneQueue = new ArrayList<>();
        this.edges = edges;

        if(new Random().nextDouble() < 0.1){
            LocalSearch localSearch = new LocalSearch(parent, Constants.antLocalSearchIterations);
            this.solution = localSearch.bestMachines;
            this.score = localSearch.bestSolution.getScore();
            this.gene = localSearch.bestSolution.getGene();
        }
        else{
            createEmptyMachines();
            run();
            this.gene = new Gene(this.geneQueue);
            this.score = Helper.getMakeSpan(this.solution);
        }
    }

    public ArrayList<Integer> getGeneQueue() {
        return geneQueue;
    }

    public ArrayList<Edge> getPath() {
        return path;
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

    private void addSubJobToMachine(Job to){
        SubJob current = to.pop();
        geneQueue.add(to.getIndex());
        solution.get(current.getMachineIndex()).add(current);
    }

    private void run(){
        Random r = new Random();
        int next = r.nextInt(ImportJobs.numJobs);
        int previous;

        for (int i = 0; i < (ImportJobs.numJobs * ImportJobs.numMachines)-1; i++) {

            addSubJobToMachine(this.jobs.get(next));
            previous = next;
            next = sumProbability(this.jobs.get(next));
            path.add(edges.get(createKey(this.jobs.get(next).getCurrSubJob(), this.jobs.get(previous).getCurrSubJob())));
        }
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public ArrayList<Machine> getSolution() {
        return solution;
    }

    public void setSolution(ArrayList<Machine> solution){
        this.solution = solution;
    }

    public int getScore() {
        return score;
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
        double pheromoneValue = Math.pow(this.edges.get(createKey(to.getCurrSubJob(), from.getCurrSubJob())).getPheromoneValue(), Constants.alpha);
        double heuristic = getHeuristic(to.getCurrSubJob());
        return pheromoneValue * heuristic;
    }

    private int sumProbability(Job from){
        ArrayList<Double> jProbabilities = new ArrayList<>();
        for(Job to : jobs){
            double ijProbability;
            if(!to.isFinished()){
                ArrayList<Integer> edgeKey = createKey(to.getCurrSubJob(), from.getCurrSubJob());
                if(!keyInEdges(edgeKey)){
                    insertEdge(edgeKey, new Edge(to.getCurrSubJob().getPheromoneMatrixIndex(), from.getCurrSubJob().getPheromoneMatrixIndex()));
                }
                ijProbability = probability(from, to);
            } else {
                jProbabilities.add(0.0);
                continue;
            }
            double pSum = 0.0;
            for(Job l : jobs){
                if(!l.isFinished() && !l.equals(from)){
                    ArrayList<Integer> edgeKey = createKey(l.getCurrSubJob(), from.getCurrSubJob());
                    if(!keyInEdges(edgeKey)){
                        insertEdge(edgeKey, new Edge(l.getCurrSubJob().getPheromoneMatrixIndex(), from.getCurrSubJob().getPheromoneMatrixIndex()));
                    }
                    pSum += probability(from, l);
                }
            }
            jProbabilities.add(ijProbability / pSum);
        }
        //System.out.println(jProbabilities);
        //System.out.println("Winner:" + assignWinner(jProbabilities));
        return assignWinner(jProbabilities);
    }

    private void insertEdge(ArrayList<Integer> key, Edge edge){
        edges.put(key, edge);
    }

    private boolean keyInEdges(ArrayList<Integer> key){
        return edges.containsKey(key);
    }

    private ArrayList<Integer> createKey(SubJob to, SubJob from){
        ArrayList<Integer> key = new ArrayList<>();
        key.add(to.getPheromoneMatrixIndex());
        key.add(from.getPheromoneMatrixIndex());
        return key;
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

    @Override
    public int compareTo(Ant o) {
        return this.getScore()-o.getScore();
    }

}





