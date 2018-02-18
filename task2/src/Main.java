import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        /*
        Change these
         */
        String path = "7";
        int numCentroids = 16;
        int kmeansIterations = 30;

        /*
        Init image and centroids
         */
        BufferedImage img = readImage(path);
        ArrayList<Centroid> centroids = initDentroids(numCentroids, img);


        ArrayList<Centroid> trainedCentroids = kMeans(centroids, img, kmeansIterations);
        BufferedImage newImg = changeImage(img, trainedCentroids);
        writeImage(path, newImg);

    }

    static BufferedImage readImage(String path){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("TestImages/"+ path +"/Test image.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    static void writeImage(String path, BufferedImage img){
        File outputfile = new File("OutFiles/"+path+".jpg");
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static BufferedImage changeImage(BufferedImage img, ArrayList<Centroid> centroids) {

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {

                Color c = new Color(img.getRGB(j, i));
                double smallestDistance = Double.POSITIVE_INFINITY;
                int nearestCentroidIndex = 0;
                for (int k = 0; k < centroids.size(); k++) {
                    double currDistance = euclideanDistance(c, centroids.get(k).getPosition());
                    if (currDistance < smallestDistance) {
                        smallestDistance = currDistance;
                        nearestCentroidIndex = k;
                    }
                }

                img.setRGB(j, i, centroids.get(nearestCentroidIndex).getPosition().getRGB());
            }
        }
        return img;
    }

    static ArrayList<Centroid> initDentroids(int numOfCentroids, BufferedImage img){
        Random r = new Random();

        ArrayList<Centroid> centroids = new ArrayList<>();
        for (int i = 0; i < numOfCentroids; i++) {
            assert img != null;
            Color c = new Color(img.getRGB(r.nextInt(img.getWidth()),r.nextInt(img.getHeight())));
            centroids.add(new Centroid(c));
        }
        return centroids;
    }

    static double euclideanDistance(Color c0, Color c1){
        return Math.sqrt((Math.pow(c0.getRed(),2) - Math.pow(c1.getRed(),2)) + (Math.pow(c0.getGreen(),2) - Math.pow(c1.getGreen(),2)) + (Math.pow(c0.getBlue(),2) - Math.pow(c1.getBlue(),2)));
    }

    static ArrayList<Centroid> kMeans(ArrayList<Centroid> centroids, BufferedImage img, int numIterations){

        Random r = new Random();

        for (int n = 0; n < numIterations; n++) {
            int boundStart = r.nextInt(10);
            int bound = r.nextInt(10);


            for (int i = boundStart; i < img.getHeight()-1; i+=2) {
                for (int j = boundStart; j < img.getWidth()-1; j+=2) {
                    Color c = new Color(img.getRGB(j,i));
                    double smallestDistance = Double.POSITIVE_INFINITY;
                    int nearestCentroidIndex = 0;
                    for (int k = 0; k < centroids.size(); k++) {
                        double currDistance = euclideanDistance(c, centroids.get(k).getPosition());
                        if (currDistance < smallestDistance) {
                            smallestDistance = currDistance;
                            nearestCentroidIndex = k;
                        }
                    }
                    centroids.get(nearestCentroidIndex).assignColor(c);
                }
            }
            for(Centroid cent : centroids){
                cent.updateCentroid();
            }
        }
        return centroids;
    }
}



