import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BFS {

    public static ArrayList<Segment> BFS(ArrayList<Node> rootnodes) {


        final ArrayList<Segment> segments = new ArrayList<>();

        final HashSet<Node> seen = new HashSet<>();


        for (int n = 0; n < rootnodes.size(); n++) {
            final int index = n;


            final LinkedList<Node> children = new LinkedList<>();
            segments.add(new Segment());

            children.add(rootnodes.get(index));

            while (!children.isEmpty()) {

                final Node current = children.remove(0);
                segments.get(index).add(current);
                current.setSegment(segments.get(index));

                for (final Node child : current.getChildren()) {
                    if (child.isRoot() || seen.contains(child)) {
                        continue;
                    }

                    children.add(child);
                    seen.add(child);


                }
            }
        }
        return segments;
    }

}
