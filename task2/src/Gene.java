import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Gene {

    private Point2D direction;
    private Point2D pos;
    private Color color;
    private ArrayList<Point2D> neighbours;
    private ArrayList<Double> neighbours_weights;
    private Centroid parent;



    public Gene(Centroid parent, Point2D pos, Color c, ArrayList<Point2D> neighbours) {
        this.pos = pos;
        this.color = c;
        this.neighbours = neighbours;

        this.parent = parent;
    }

    public void setDirection(Point2D dir) {
        this.direction = dir;
    }

    public Point2D getGene() {
        return this.direction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void addDirection(Point2D dir) {
        this.direction = dir;
    }

    public ArrayList<Double> setNeighboursWeights(ArrayList<Point2D> neighbours, BufferedImage img){
        ArrayList<Double> weights = new ArrayList<>();
        for(Point2D p : neighbours){
            Color currC = getColorFromCoords(img, p);
            double dist = euclideanDistance(this.color, currC);

        }
        return weights;
    }

    public double euclideanDistance(Color c0, Color c1) {
        return Math.sqrt((Math.pow(c0.getRed(), 2) - Math.pow(c1.getRed(), 2)) + (Math.pow(c0.getGreen(), 2) - Math.pow(c1.getGreen(), 2)) + (Math.pow(c0.getBlue(), 2) - Math.pow(c1.getBlue(), 2)));
    }

    public Color getColorFromCoords(BufferedImage img, Point2D pix){
        return new Color(img.getRGB((int)pix.getX(), (int)pix.getY()));
    }

}
