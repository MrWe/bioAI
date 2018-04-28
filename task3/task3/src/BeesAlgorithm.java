import java.util.*;

public class BeesAlgorithm {

    private static int initialPopulation = 20;
    private static int m = (int)Math.floor(initialPopulation/2);
    private static int elites =  (int)Math.floor(m*0.3);
    private static int nonElites = m - elites;
    private static int nep = 20;
    private static int nsp = 5;


    private static Solution bestSolution;
    private static int bestMakespan = Integer.MAX_VALUE;
    private static int optimalValue;

    public static ArrayList<Machine> run(int optValue) {
        optimalValue = optValue;

        PriorityQueue<Solution> sites = new PriorityQueue<>();

        //Create initial sites
        for (int i = 0; i < initialPopulation; i++) {
            sites.add(new Solution(new Gene()));
        }

        for (int n = 0; n < 3000; n++) {

            ArrayList<Solution> eliteSites = new ArrayList<>();
            //Get elites
            for (int i = 0; i < elites; i++) {
                eliteSites.add(sites.poll());
            }

            ArrayList<Solution> nonEliteSites = new ArrayList<>();
            //Get elites
            for (int i = 0; i < nonElites; i++) {
                nonEliteSites.add(sites.poll());
            }

            sites = new PriorityQueue<>();

            for (int i = 0; i < initialPopulation/4; i++) {
                sites.add(new Solution(new Gene()));
            }

            for(Solution s : eliteSites){
                sites.addAll(createNeighbours(s, nep));
            }

            for(Solution s : nonEliteSites){
                sites.addAll(createNeighbours(s, nsp));
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

    private static ArrayList<Solution> createNeighbours(Solution current, int iterations) {
        final ArrayList<Solution> neighbours = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            SplittableRandom r = new SplittableRandom();
            ArrayList<Integer> sequence = new ArrayList<>(current.getGene().getQueue());

            int iter = r.nextInt(3)+1;

            for (int j = 0; j < iter; j++) {

                int r1 = r.nextInt(sequence.size());
                int r2 = r.nextInt(sequence.size());

                Collections.swap(sequence, r1, r2);
            }

            neighbours.add(new Solution(new Gene(sequence)));
        }
        return neighbours;
    }



}


