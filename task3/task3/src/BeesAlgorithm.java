import java.util.*;

public class BeesAlgorithm {


    static double bestErr = Integer.MAX_VALUE;
    static Gene bestGene;
    static Bee bestBee;
    static int nb = 50;
    static int neighbourhoodSize = 10; // We begin by generating 100 new bees in a single scout local search
    static int bestMakespan = Integer.MAX_VALUE;
    static int optimalValue;

    public static ArrayList<Machine> run(ImportJobs imports, int optValue) {
        optimalValue = optValue;

        ArrayList<Bee> hive = new ArrayList<>();

        int numActive = (int) Math.floor(nb * 0.5);
        int numScout = (int) Math.floor(nb * 0.25);

        ArrayList<Bee> scouts = createScouts(numScout, imports);
        hive.addAll(scouts);

        int iter = 0;

        while(bestMakespan > optimalValue){

            Collections.sort(hive, errComparator);

            hive = new ArrayList<Bee>(hive.subList(0, (int) Math.floor(hive.size()))); // Retain 10% of the best bees
            ArrayList<Bee> newHive = new ArrayList<>();

            for(Bee bee : hive){
                ArrayList<Bee> localSearchResults = waggleDance(bee, neighbourhoodSize);
                newHive.addAll(localSearchResults);
            }

            Collections.sort(newHive, errComparator);

            hive = new ArrayList<Bee>(newHive.subList(0, nb));

            setBestErr(hive);

            iter++;

            if(iter % 100 == 0){
                System.out.println(iter + "   " +bestMakespan);
            }


        }

        System.out.println(bestMakespan);

        return bestBee.getMachines();



    }

    private static void setBestErr(ArrayList<Bee> hive){
        for (Bee bee : hive){
            if(bee.getMakespan() < bestMakespan){
                bestMakespan = bee.getMakespan();
                bestGene = bee.gene;
                bestBee = bee;
            }
        }
    }


    private static ArrayList<Bee> createScouts(int ns, ImportJobs imports){
        ArrayList<Bee> hive = new ArrayList<>();
        for (int i = 0; i < ns; i++) {
            Bee bee = new Bee(imports.numJobs, imports.numMachines, imports.stringJobs, optimalValue);
            bee.setStatus(2);

            hive.add(bee);
        }
        return hive;
    }

    private void initializeFlowerPatch(){

    }

    private void recruitment(ArrayList<Bee> hive){

    }

    private static ArrayList<Bee> waggleDance(Bee bee, int neighbourhoodSize){
        ArrayList<Bee> newBees = new ArrayList<>();
        for (int i = 0; i < neighbourhoodSize; i++) {
            Bee newBee = new Bee(bee.getNumJobs(), bee.getNumMachines(), bee.getStringJobs(), bee.getOptimalValue(), bee.getGene());
            Gene newBeeGene = newBee.getGene();

            Random r = new Random();
            for (int j = 0; j < 15; j++) {

                int swapFrom = r.nextInt(newBeeGene.getQueue().size());
                int swapTo = r.nextInt(newBeeGene.getQueue().size());

                Collections.swap(newBeeGene.getQueue(), swapFrom, swapTo);
            }
            newBees.add(newBee);
        }
        return newBees;
    }

    private void localSearch(FlowerPatch patch){

    }

    private void abandonSite(FlowerPatch patch){

    }

    private void shrinkNeighbourhood(){

    }

    private void globalSearch(){

    }

    static Comparator<Bee> errComparator = new Comparator<Bee>() {
        public int compare(Bee i, Bee o) {
            return (int) (i.err - o.err);
        }
    };
}


