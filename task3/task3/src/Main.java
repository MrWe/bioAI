import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ImportJobs imports = new ImportJobs("/Users/Wiker/Documents/bioAI/task3/task3/Data/1.txt");

        ArrayList<Machine> machines = new ArrayList<>();
        ArrayList<Job> jobs = new ArrayList<>();

        for (int i = 0; i < imports.numMachines; i++) {
            machines.add(new Machine());
        }

        for (int i = 0; i < imports.numJobs; i++) {
            jobs.add(new Job(imports.stringJobs.get(i)));
        }

        System.out.println(jobs);


    }
}
