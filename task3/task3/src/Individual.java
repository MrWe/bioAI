import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;

public class Individual {

    ArrayList<Integer> queue;

    public Individual(int numJobs, int numMachines){
        queue = randomQueue(numJobs, numMachines);
    }

    public Individual(ArrayList<Integer> queue){
        this.queue = queue;
    }

    private ArrayList<Integer> randomQueue(int numJobs, int numMachines){
        ArrayList<Integer> queue = new ArrayList<>();
        for (int i = 0; i < numJobs; i++) {
            for (int j = 0; j < numMachines; j++) {
                queue.add(i);
            }
        }
        Collections.shuffle(queue, new Random(System.nanoTime()));

        return queue;
    }
}
