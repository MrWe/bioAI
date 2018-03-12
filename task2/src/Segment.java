import java.util.ArrayList;

public class Segment {

    private ArrayList<Node> nodes;
    private Node rootNode;

    public Segment(){
        this.nodes = new ArrayList<>();
    }

    public void setNodes(ArrayList<Node> nodes){
        this.nodes = nodes;
    }

    public void add(Node node){
        nodes.add(node);
    }

    public ArrayList<Node> getNodes(){
        return this.nodes;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
}
