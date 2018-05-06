import java.util.*;

public class BeesAlgorithm {

    private static Solution bestSolution;
    private static int bestMakespan = Integer.MAX_VALUE;
    private static int optimalValue;

    public static ArrayList<Machine> run(int optValue) {
        optimalValue = optValue;

        PriorityQueue<Solution> sites = new PriorityQueue<>();

        //Create initial sites
        for (int i = 0; i < Constants.initialPopulation; i++) {
            sites.add(new Solution(new Gene()));
        }

        while(true){

            ArrayList<Solution> eliteSites = new ArrayList<>();
            //Get elites
            for (int i = 0; i < Constants.elites; i++) {
                eliteSites.add(sites.poll());
            }

            ArrayList<Solution> nonEliteSites = new ArrayList<>();
            //Get nonelites
            for (int i = 0; i < Constants.nonElites; i++) {
                nonEliteSites.add(sites.poll());
            }

            //Site abandonment
            if(new SplittableRandom().nextDouble() < 0.01) {
                sites = new PriorityQueue<>();
            }

            //Scouts
            for (int i = 0; i < Constants.initialPopulation/4; i++) {
                sites.add(new Solution(new Gene()));
            }


            for(Solution s : eliteSites){
                sites.addAll(createNeighbours(s, Constants.nep, Constants.softMutationRate));
            }

            for(Solution s : nonEliteSites){
                sites.addAll(createNeighbours(s, Constants.nsp, Constants.strongMutationRate));
            }

            if(sites.peek().getScore() < bestMakespan){
                bestMakespan = sites.peek().getScore();
                bestSolution = sites.peek();
                if(bestMakespan <= optimalValue){
                    break;
                }
            }
        }


        System.out.println("Bees algorithm best makespan: " + bestMakespan);


        return bestSolution.getMachines();

    }

    private static ArrayList<Solution> createNeighbours(Solution current, int iterations, int mutationRate) {
        final ArrayList<Solution> neighbours = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            SplittableRandom r = new SplittableRandom();
            ArrayList<Integer> sequence = new ArrayList<>(current.getGene().getQueue());

            for (int j = 0; j < mutationRate; j++) {

                int r1 = r.nextInt(sequence.size());
                int r2 = r.nextInt(sequence.size());

                Collections.swap(sequence, r1, r2);
            }

            neighbours.add(new Solution(new Gene(sequence)));
        }
        return neighbours;
    }



}


