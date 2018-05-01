public class SubJob {

    private final int index;
    public int startTime;
    private int machineIndex;
    private int duration;
    private Job parent;
    private int pheromoneMatrixIndex;

    public SubJob(int machine, int duration, Job parent, int index, int totalNumberOfSubjobs){
        this.machineIndex = machine;
        this.duration = duration;
        this.parent = parent;
        this.startTime = 0;
        this.pheromoneMatrixIndex = totalNumberOfSubjobs * parent.getIndex() + index;
        this.index = index;
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

    public int getPheromoneMatrixIndex() {
        return pheromoneMatrixIndex;
    }

    public int getIndex() {
        return index;
    }
}
