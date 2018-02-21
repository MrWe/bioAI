import java.util.HashSet;

public class ClosedSet{

    private HashSet<Node> nodeset = new HashSet<>();
    private HashSet<String> coordSet = new HashSet<>();


    public void add(Node node){

       if(!coordSet.contains(node.getHash())){
           this.nodeset.add(node);
           this.coordSet.add(node.getHash());
       }

    }

    public boolean contains(Node node){
        return coordSet.contains(node.getHash());

    }


}
