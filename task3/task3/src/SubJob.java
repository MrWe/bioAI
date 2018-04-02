public class SubJob {

    private int machine;
    private int duration;
    private Job parent;

    public SubJob(int machine, int duration, Job parent){
        this.machine = machine;
        this.duration = duration;
        this.parent = parent;
    }

    public int getMachine() {
        return machine;
    }

    public int getDuration() {
        return duration;
    }

    public Job getParent() {
        return parent;
    }
}
