public class Constants {

    public static double alpha = 0.5;
    public static double beta = 1-alpha;
    public static double initialPheromoneValue = 2;
    public static double pheromoneUpdateValue = 1.5;
    public static double pheromoneP = 0.1;
    public static double pheromoneMin = 0.01;
    public static double pheromoneMax = 1;
    public static int antLocalSearchIterations = 50;
    public static int antPopulationSize = 10;


    public static int initialPopulation = 100;
    public static int m = (int)Math.floor(initialPopulation/2);
    public static int elites =  (int)Math.floor(m*0.3);
    public static int nonElites = m - elites;
    public static int nep = 20;
    public static int nsp = 5;
}
