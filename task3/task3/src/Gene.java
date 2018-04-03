import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Gene {

    ArrayList<Integer> queue;


    public Gene(int numJobs, int numMachines) {
        queue = randomQueue(numJobs, numMachines);
    }

    public Gene(ArrayList<Integer> queue) {
        this.queue = queue;
    }

    private ArrayList<Integer> randomQueue(int numJobs, int numMachines) {
        ArrayList<Integer> queue = new ArrayList<>();
        for (int i = 0; i < numJobs; i++) {
            for (int j = 0; j < numMachines; j++) {
                queue.add(i);
            }
        }
        Collections.shuffle(queue, new Random(System.nanoTime()));

        return queue;
    }

    private ArrayList<Integer> copyGene() {
        return new ArrayList(this.queue);
    }

    public ArrayList<Integer> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Integer> queue) {
        this.queue = queue;
    }
}
