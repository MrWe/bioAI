import java.util.ArrayList;
import java.util.Arrays;

public class Job {

    private ArrayList<SubJob> subJobs;

    public Job(ArrayList<String> stringJob){
        subJobs = new ArrayList<>();
        generateSubJobs(stringJob);

    }

    private void generateSubJobs(ArrayList<String> stringJob){

        String[] stringArr = stringJob.get(0).trim().split("\\s+");

        ArrayList<String> params = new ArrayList<>(Arrays.asList(stringArr));

        for (int i = 0; i < params.size(); i+=2) {
            subJobs.add(new SubJob(Integer.parseInt(params.get(i)), Integer.parseInt(params.get(i+1)), this));
        }

    }
}
