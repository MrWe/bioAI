import java.util.*;

public class Ant {

    private Gene gene;
    private ArrayList<Job> jobs;
    private ArrayList<Machine> solution;
    private int score;


    Ant(PheromoneMatrix pheromoneMatrix) {
        constructGeneFromMatrix(pheromoneMatrix);

        this.solution = createSolution(this.getGene().getQueue());
        this.score = Helper.getMakeSpan(this.solution);


        Random r = new Random();
        if(r.nextDouble() < 1){
            localSearch(this.getGene().getQueue());
        }
    }

    /*private void localSearch(ArrayList<Integer> queue) {

        int size = 3;

        Random r = new Random();

        int start = r.nextInt(this.gene.queue.size()-size);
        int stop = start + size;

        ArrayList<Integer> partQueue = new ArrayList<>(this.gene.getQueue().subList(start, stop));

        Integer[] intArray = partQueue.toArray(new Integer[0]);

        List<List<Integer>> permutations = permute(intArray);


        for(List<Integer> permutation : permutations) {
            ArrayList<Integer> newGene = new ArrayList<>(this.getGene().getQueue());
            newGene.subList(start, stop).clear();
            newGene.addAll(start, permutation);

            ArrayList<Machine> currSolution = createSolution(newGene);
            int currScore = Helper.getMakeSpan(currSolution);


            if(currScore < this.score){
                this.solution = currSolution;
            }
        }
    }*/

    private void localSearch(ArrayList<Integer> queue) {

        Gene start = new Gene(queue);

        Astar a = new Astar(start);

        if(a.bestSolution.getScore() < this.getScore()){
            this.solution = a.bestMachines;
            this.gene = a.bestSolution.getGene();
            this.score = a.bestSolution.getScore();
        }

    }

    public List<List<Integer>> permute(Integer[] numbers) {
        // we use a list of lists rather than a list of arrays
        // because lists support adding in the middle
        // and track current length
        List<List<Integer>> permutations = new ArrayList<List<Integer>>();
        // Add an empty list so that the middle for loop runs
        permutations.add(new ArrayList<Integer>());

        for ( int i = 0; i < numbers.length; i++ ) {
            // create a temporary container to hold the new permutations
            // while we iterate over the old ones
            List<List<Integer>> current = new ArrayList<List<Integer>>();
            for ( List<Integer> permutation : permutations ) {
                for ( int j = 0, n = permutation.size() + 1; j < n; j++ ) {
                    List<Integer> temp = new ArrayList<Integer>(permutation);
                    temp.add(j, numbers[i]);
                    current.add(temp);
                }
            }
            permutations = new ArrayList<List<Integer>>(current);
        }

        return permutations;
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

    private void constructGeneFromMatrix(PheromoneMatrix pheromoneMatrix) {
        ArrayList<Integer> queue = new ArrayList<>();

        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < ImportJobs.numJobs; i++) {
            jobs.add(new Job(ImportJobs.stringJobs.get(i), i));
        }

        Random r = new Random();

        int start = r.nextInt(ImportJobs.numJobs);
        jobs.get(start).pop();

        queue.add(start);

        for (int i = 0; i < (ImportJobs.numJobs * ImportJobs.numMachines)-1; i++) {

                int next = nextQueueElement(queue.get(queue.size() - 1), pheromoneMatrix, jobs);
            if(jobs.get(next).isNotOnLastSubJob()) {
                jobs.get(next).pop();
                queue.add(next);
            }
        }

        for (Job j : jobs){
            j.resetSubJobIndex();
        }

        this.jobs = jobs;
        this.gene = new Gene(queue);

    }

    private ArrayList<Job> createJobs(){
        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < ImportJobs.numJobs; i++) {
            jobs.add(new Job(ImportJobs.stringJobs.get(i), i));
        }

        return jobs;
    }

    private Integer nextQueueElement(Integer i, PheromoneMatrix pheromoneMatrix, ArrayList<Job> jobs) {

        ArrayList<Double> probabilities = new ArrayList<>();

        double sum = 0;

        for (int j = 0; j < ImportJobs.numJobs; j++) {
            if(jobs.get(j).isNotOnLastSubJob()) {
                double p = probability(i, j, pheromoneMatrix, jobs);
                probabilities.add(p);
                sum += p;
            }
            else{
                probabilities.add(0.0);
            }
        }

        for (int j = 0; j < probabilities.size(); j++) {
            double divider = 0;
            for (int k = 0; k < probabilities.size(); k++) {
                if(k != j){
                    divider += probabilities.size();
                }
            }
            probabilities.set(j, probabilities.get(j)/divider);
        }

        /*for (int j = 0; j < probabilities.size(); j++) {
            probabilities.set(j, probabilities.get(j)/sum);
        }*/

       // System.out.println(probabilities);

        Random r = new Random();

        if(r.nextDouble() < 0.3){
            int index = r.nextInt(probabilities.size());
            if(probabilities.get(index) != 0.0){
                return index;
            }

        }
        return probabilities.indexOf(Collections.max(probabilities));
    }

    private double probability(int i, Integer j, PheromoneMatrix pheromoneMatrix, ArrayList<Job> jobs) {
        double pheromoneValue = Math.pow(pheromoneMatrix.get(i, j), Constants.alpha);
        double heuristic = 1 / Math.pow(jobs.get(j).getCurrSubJob().getDuration(), Constants.beta);
        return pheromoneValue * heuristic;

    }

    private ArrayList<Machine> createSolution(ArrayList<Integer> queue){

        ArrayList<Job> jobs = createJobs();

        ArrayList<Machine> machines = new ArrayList<>();

        for (int i = 0; i < ImportJobs.numMachines; i++) {
            machines.add(new Machine());
        }

        for (Integer index : queue) {
            SubJob subJob = jobs.get(index).pop();
            machines.get(subJob.getMachineIndex()).add(subJob);
        }

        for (Job j : jobs){
            j.resetSubJobIndex();
        }

        return machines;

    }


    public Gene getGene() {
        return gene;
    }


}


