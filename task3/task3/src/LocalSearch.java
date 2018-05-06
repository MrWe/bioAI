
import java.util.*;
import java.util.SplittableRandom;


public class LocalSearch {

    ArrayList<Machine> bestMachines;
    Solution bestSolution;

    public LocalSearch(int optimalValue) {
        Randrun(optimalValue);
    }

    public LocalSearch(Gene start, int iterations){
        run(start, iterations);
    }

    private void run(Gene start, int iterations){
        PriorityQueue<Solution> open = new PriorityQueue<>();
        LinkedList<Solution> closed = new LinkedList<>();

        int bestScore = Integer.MAX_VALUE;


        open.add(new Solution(start));


        for (int i = 0; i < iterations; i++) {

            Solution current = open.poll();
            closed.add(current);

            ArrayList<Solution> neighbours = createNeighbours(current);

            for (Solution as : neighbours){

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
        PriorityQueue<Solution> open = new PriorityQueue<>();


        int bestScore = Integer.MAX_VALUE;

        for (int i = 0; i < 100; i++) {
            open.add(new Solution(new Gene()));
        }

        for (int i = 0; i < 100000; i++) {

            if(open.isEmpty()){
                for (int k = 0; k < 5; k++) {
                    open.add(new Solution(new Gene()));
                }
            }

            Solution current = open.poll();

            SplittableRandom r = new SplittableRandom();

            if(r.nextDouble() < 0.005){

                //if(open.size() > 500) {
                    open = new PriorityQueue<>();
                //}

            }

            if(r.nextDouble() < 0.03){
                current = createNeighbours(current).get(0);
            }


            ArrayList<Solution> neighbours = createNeighbours(current);

            for (Solution as : neighbours){

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



    private ArrayList<Solution> createNeighbours(Solution current) {
        final ArrayList<Solution> neighbours = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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


