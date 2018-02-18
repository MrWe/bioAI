import java.awt.*;
import java.util.ArrayList;

public class Centroid {

    private ArrayList<Color>  currentlyAssignedColors;
    private Color position;

    public Centroid(Color c){
        this.currentlyAssignedColors = new ArrayList<>();
        this.position = c;
    }

    public void updateCentroid(){
        double r = 0;
        double g = 0;
        double b = 0;

        for(Color c : this.currentlyAssignedColors){
            r += c.getRed();
            g += c.getGreen();
            b += c.getBlue();
        }

        r /= this.currentlyAssignedColors.size();
        g /= this.currentlyAssignedColors.size();
        b /= this.currentlyAssignedColors.size();

        double redDir =   ( this.position.getRed() - r);
        double greenDir = ( this.position.getGreen() - g);
        double blueDir =  ( this.position.getBlue() - b);

        double newR = this.position.getRed() - (redDir * 0.00001);
        double newG = this.position.getGreen() - (greenDir * 0.00001);
        double newB = this.position.getBlue() - (blueDir * 0.00001);



        this.position = new Color(ensureRange((int)newR), ensureRange((int)newG), ensureRange((int)newB));
        //this.position = new Color((int)newR, (int)newG, (int)newB);


        this.currentlyAssignedColors = new ArrayList<>();

    }

    private int ensureRange(int value) {
        return Math.min(Math.max(value, 0), 255);
    }
    
    public void assignColor(Color c){
        this.currentlyAssignedColors.add(c);
    }

    public Color getPosition() {
        return position;
    }
}
