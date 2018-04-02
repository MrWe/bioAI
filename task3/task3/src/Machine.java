import java.util.Collections;
import java.util.HashMap;

import static java.lang.Integer.max;

public class Machine {

    private HashMap<Integer, SubJob> subJobs;


    public Machine(){
        subJobs = new HashMap<>();
    }

    public HashMap<Integer, SubJob> getSubJobs() {
        return subJobs;
    }

    /*
    OBS OBS: Here we assume that we always want the first available slot given that startTime is less than first available slot
    Validation that the earlier start time is correct for a job must be done somewhere else
     */
    public void add(SubJob subJob) {
        int firstAvailableSlot = getFirstAvailableSlot();
        int startTime = max(subJob.getParent().getTotalTime(), firstAvailableSlot);
        this.subJobs.put(startTime, subJob);
        subJob.setStartTime(startTime);

        subJob.getParent().setTotalTime(subJob.getDuration() + startTime); //TODO: Test if this works
    }

    public int getFirstAvailableSlot(){
        if(subJobs.keySet().size() == 0){
            return 0;
        }
        int latestTimeStart = Collections.max(subJobs.keySet());
        return latestTimeStart + subJobs.get(latestTimeStart).getDuration();
    }
}
