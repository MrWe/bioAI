import java.util.ArrayList;

public class Segment {

    private ArrayList<Node> nodes;

    public Segment(){
        nodes = new ArrayList<>();
    }

    public void add(Node node){
        nodes.add(node);
    }

    public ArrayList<Node> get(){
        return this.nodes;
    }
}
