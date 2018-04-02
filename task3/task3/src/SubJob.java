public class SubJob {

    public int startTime;
    private int machineIndex;
    private int duration;
    private Job parent;

    public SubJob(int machine, int duration, Job parent){
        this.machineIndex = machine;
        this.duration = duration;
        this.parent = parent;
        this.startTime = 0;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getMachineIndex() {
        return machineIndex;
    }

    public int getDuration() {
        return duration;
    }

    public Job getParent() {
        return parent;
    }
}
