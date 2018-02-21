import java.util.ArrayList;
import java.util.HashSet;

public class ClosedSet{

    private HashSet<Node> coordSet = new HashSet<>();



    public HashSet<Node> getCoordSet() {
        return this.coordSet;
    }

    public void setCoordSet(HashSet<Node> coordSet) {
        this.coordSet = coordSet;
    }

    public void add(Node node){

       if(!this.coordSet.contains(node)){
           this.coordSet.add(node);
       }

    }

    public boolean contains(Node node){
        return coordSet.contains(node);

    }


}
