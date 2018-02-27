import java.awt.*;
import java.util.ArrayList;

public abstract class Helpers {

    static double PlanarEuclideanDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt((Math.pow(x0 - x1, 2)) + Math.pow(y0 - y1, 2));
    }

    static double ColorEuclideanDistance(Color c0, Color c1) {
        return Math.sqrt((Math.pow(c0.getRed()- c1.getRed(), 2)) + (Math.pow(c0.getGreen() - c1.getGreen(), 2)) + (Math.pow(c0.getBlue()- c1.getBlue(), 2)));
    }

    static void setAvgColor(ArrayList<Centroid> centroids){

        for(Centroid c : centroids){
            double r = 0;
            double g = 0;
            double b = 0;

            for(Node n : c.getcurrentlyAssignedNodes()){
                r += n.getColor().getRed();
                g += n.getColor().getGreen();
                b += n.getColor().getBlue();
            }

            r /= c.getcurrentlyAssignedNodes().size();
            g /= c.getcurrentlyAssignedNodes().size();
            b /= c.getcurrentlyAssignedNodes().size();

            Color color = new Color((int)r, (int) g, (int) b);
            c.setAvgColor(color);
        }
    }
}
