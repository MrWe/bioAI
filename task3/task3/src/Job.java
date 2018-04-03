import java.util.ArrayList;
import java.util.Arrays;

public class Job {

    private ArrayList<SubJob> subJobs;
    private int totalTime;
    private int index;

    public Job(ArrayList<String> stringJob, int index){
        this.subJobs = new ArrayList<>();
        this.totalTime = 0;
        this.index = index;
        generateSubJobs(stringJob);
    }

    public ArrayList<SubJob> getSubJobs() {
        return subJobs;
    }

    private void generateSubJobs(ArrayList<String> stringJob){
        String[] stringArr = stringJob.get(0).trim().split("\\s+");
        ArrayList<String> params = new ArrayList<>(Arrays.asList(stringArr));
        for (int i = 0; i < params.size(); i+=2) {
            subJobs.add(new SubJob(Integer.parseInt(params.get(i)), Integer.parseInt(params.get(i+1)), this));
        }
    }

    public int getTotalTime(){
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getIndex() {
        return index;
    }

    public SubJob pop(){
        return subJobs.remove(0);
    }
}
