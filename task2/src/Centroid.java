import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Centroid {

    private ArrayList<Gene>  currentlyAssignedPixels;
    private Point2D position;

    public Centroid(Point2D pos){
        this.position = pos;
    }

    public void updateCentroid() {

    }

    public Point2D getPosition() {
        return position;
    }

    public void addPixel(Gene pix){
        this.currentlyAssignedPixels.add(pix);
    }



}


