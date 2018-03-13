import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        /*
        Change these
         */
        String path = "2";
        int numSegments = 50;
        int numPopulations = 20;
        int numIndividuals = 2;


        BufferedImage img = readImage(path);

        //img = scale(img, img.getType(), (int)(img.getWidth()*0.1), (int)(img.getHeight()*0.1), 0.1, 0.1);
        int[][] imgArray = Helpers.convertTo2DWithoutUsingGetRGB(img);

        ArrayList<ArrayList<Node>> nodes = Helpers.initNodes(imgArray);

        ArrayList<ArrayList<ArrayList<Edge>>> edges = new ArrayList<>();


        for (int i = 0; i < nodes.size(); i++) {
            edges.add(new ArrayList<>());
            for (int j = 0; j < nodes.get(i).size(); j++) {
                edges.get(i).add(new ArrayList<>());
                edges.get(i).get(j).addAll(Helpers.setNodeEdges(nodes.get(i).get(j),nodes));
            }
        }


        Population p = new Population(imgArray, edges, numSegments, numIndividuals);

        for (int i = 0; i < numPopulations; i++) {

            System.out.println("Population: " + i);
            System.out.println("Overall Deviation: " + p.getIndividuals().get(0).getOverallDeviation());
            System.out.println("Edge Value: " + p.getIndividuals().get(0).getEdgeValue());
            System.out.println();

            ArrayList<ArrayList<ArrayList<Integer>>> newRoots = GA.doGA(imgArray, p, numIndividuals);

            p = new Population(imgArray, edges, newRoots, numIndividuals);

            BufferedImage currImg = deepCopy(img);

            Individual outIndividual = p.getIndividuals().get(0);
            for(Segment segment : outIndividual.getSegments()){
                for(Node n : segment.getNodes()){
                    currImg = changeImageGroundTruth(currImg, n);
                }
            }

            writeImage(path, currImg, i);

        }


        BufferedImage img2 = deepCopy(img);

        Individual i = p.getIndividuals().get(0);
            for(Segment segment : i.getSegments()){
                for(Node n : segment.getNodes()){
                    img2 = changeImageWithColors(img2, n);
                }
            }

        writeImage(path+"COLORS", img2, 0);


    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }


    static BufferedImage readImage(String path) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File("TestImages/" + path + "/Test image.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    static void writeImage(String path, BufferedImage img, int imgCount) {
        File dir = new File("OutFiles/" + path);
        dir.mkdir();

        File outputfile = new File("OutFiles/" + path +"/" + path + imgCount + ".jpg");
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static BufferedImage changeImageGroundTruth(BufferedImage img, Node node) {

        Color c;
        if(node.isEdge()){
            c = Color.BLACK;
        }
        else{
            c = Color.WHITE;
        }

        img.setRGB(node.getY(), node.getX(), c.getRGB());

        try{
            img.setRGB(node.getY()+1, node.getX(), c.getRGB());
        }
        catch (Exception e){

        }

        try{
            img.setRGB(node.getY()-1, node.getX(), c.getRGB());
        }
        catch (Exception e){

        }

        try{
            img.setRGB(node.getY(), node.getX()+1, c.getRGB());
        }
        catch (Exception e){

        }

        try{
            img.setRGB(node.getY(), node.getX()-1, c.getRGB());
        }
        catch (Exception e){

        }

        return img;
    }

    static BufferedImage changeImageWithColors(BufferedImage img, Node node) {

        Color c;
        if(node.isEdge()){
            c = Color.GREEN;
        }
        else{
            c = new Color(img.getRGB(node.getY(), node.getX()));
        }

        img.setRGB(node.getY(), node.getX(), c.getRGB());



        return img;
    }



    static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }


}
