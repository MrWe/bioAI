import java.awt.*;

public abstract class Helpers {

    static double PlanarEuclideanDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt((Math.pow(x0 - x1, 2)) + Math.pow(y0 - y1, 2));
    }

    static double ColorEuclideanDistance(Color c0, Color c1) {
        return Math.sqrt((Math.pow(c0.getRed()- c1.getRed(), 2)) + (Math.pow(c0.getGreen() - c1.getGreen(), 2)) + (Math.pow(c0.getBlue()- c1.getBlue(), 2)));
    }
}
