import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Gene {

    ArrayList<Integer> queue;


    public Gene() {
        queue = randomQueue();
    }

    public Gene(ArrayList<Integer> queue) {
        this.queue = queue;
    }

    private ArrayList<Integer> randomQueue() {
        ArrayList<Integer> queue = new ArrayList<>();
        for (int i = 0; i < ImportJobs.numJobs; i++) {
            for (int j = 0; j < ImportJobs.numMachines; j++) {
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
