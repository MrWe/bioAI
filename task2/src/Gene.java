import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Gene {

    String direction;


    public Gene(Point2D pos){
        this.direction = direction;
    }

    public void setDirection(String dir){
        this.direction = dir;
    }

    public String getGene(){
        return this.direction;
    }

    public double euclideanDistance(Color c0, Color c1) {
        return Math.sqrt((Math.pow(c0.getRed(), 2) - Math.pow(c1.getRed(), 2)) + (Math.pow(c0.getGreen(), 2) - Math.pow(c1.getGreen(), 2)) + (Math.pow(c0.getBlue(), 2) - Math.pow(c1.getBlue(), 2)));
    }

    public void addDirection(String dir){

    }
}
