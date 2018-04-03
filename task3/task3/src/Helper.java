import java.util.ArrayList;

public class Helper {

    public static int getMakeSpan(ArrayList<Machine> machines) {
        int highest = 0;

        for (Machine m : machines) {
            if (m.getTotalTime() > highest) {
                highest = m.getTotalTime();
            }
        }
        return highest;
    }
}
