import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;

public abstract class Helpers {

    static Comparator<Individual> edgeValueComparator = new Comparator<Individual>() {
        public int compare(Individual i, Individual o) {
            return (int) (i.getEdgeValue() - o.getEdgeValue());
        }
    };
    static Comparator<Individual> overallDeviationComparator = new Comparator<Individual>() {
        public int compare(Individual i, Individual o) {
            return (int) (i.getOverallDeviation() - o.getOverallDeviation());
        }
    };
    static Comparator<Individual> crowdingDistanceComparator = new Comparator<Individual>() {
        public int compare(Individual i, Individual o) {
            return (int) (o.getCrowdingDistance() - i.getCrowdingDistance());
        }
    };

    static double PlanarEuclideanDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt((Math.pow(x0 - x1, 2)) + Math.pow(y0 - y1, 2));
    }

    public static ArrayList<Individual> crowdingDistance(ArrayList<Individual> individuals, int num_individuals_to_keep) {
        //Sort individuals by edge value and get best and worst
        ArrayList<Individual> edgeValueRanks = new ArrayList<Individual>(individuals);
        Collections.sort(edgeValueRanks, edgeValueComparator);
        edgeValueRanks.get(0).setCrowdingDistance(Integer.MAX_VALUE);
        edgeValueRanks.get(edgeValueRanks.size() - 1).setCrowdingDistance(Integer.MAX_VALUE);

        //Sort individuals by overall deviation and get best and worst
        ArrayList<Individual> overallDeviationRanks = new ArrayList<Individual>(individuals);
        Collections.sort(overallDeviationRanks, overallDeviationComparator);
        overallDeviationRanks.get(0).setCrowdingDistance(Integer.MAX_VALUE);
        overallDeviationRanks.get(overallDeviationRanks.size() - 1).setCrowdingDistance(Integer.MAX_VALUE);

        //Establish FMin and FMax values
        double edgeValueFMax = edgeValueRanks.get(edgeValueRanks.size() - 1).getEdgeValue();
        double edgeValueFMin = edgeValueRanks.get(0).getEdgeValue();

        double overallDeviationFMax = overallDeviationRanks.get(overallDeviationRanks.size() - 1).getOverallDeviation();
        double overallDeviationFMin = overallDeviationRanks.get(0).getOverallDeviation();

        //Add edge value crowding distance
        for (int i = 1; i < edgeValueRanks.size() - 1; i++) {
            double distance = (edgeValueRanks.get(i - 1).getEdgeValue() - edgeValueRanks.get(i + 1).getEdgeValue()) / (edgeValueFMax - edgeValueFMin);

            edgeValueRanks.get(i).setCrowdingDistance(edgeValueRanks.get(i).getCrowdingDistance() + distance);
        }

        //Add overall deviation crowding distance
        for (int i = 1; i < overallDeviationRanks.size() - 1; i++) {
            double distance = (overallDeviationRanks.get(i - 1).getOverallDeviation() - overallDeviationRanks.get(i + 1).getOverallDeviation()) / (overallDeviationFMax - overallDeviationFMin);

            edgeValueRanks.get(i).setCrowdingDistance(edgeValueRanks.get(i).getCrowdingDistance() + distance);
        }

        //Sort by crowding distance
        Collections.sort(individuals, crowdingDistanceComparator);

        return new ArrayList<Individual>(individuals.subList(0, num_individuals_to_keep));
    }


    static double ColorEuclideanDistance(Color c0, Color c1) {
        return Math.sqrt((Math.pow(c0.getRed() - c1.getRed(), 2)) + (Math.pow(c0.getGreen() - c1.getGreen(), 2)) + (Math.pow(c0.getBlue() - c1.getBlue(), 2)));
    }

    /*static void setAvgColor(ArrayList<Centroid> centroids){

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
    }*/

    public static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public static int[] getRGBFromInt(int color) {
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;

        int[] t = new int[3];
        t[0] = red;
        t[1] = green;
        t[2] = blue;

        return t;
    }

    public static double rgbDistance(int argb1, int argb2) {
        int r1 = (argb1 >> 16) & 255;
        int g1 = (argb1 >> 8) & 255;
        int b1 = (argb1) & 255;

        int r2 = (argb2 >> 16) & 255;
        int g2 = (argb2 >> 8) & 255;
        int b2 = (argb2) & 255;

        return ColorEuclideanDistance(new Color(r1, g1, b1), new Color(r2, g2, b2));

        //return Math.sqrt(r1 * r2 + g1 * g2 + b1 * b2);
    }

    public static ArrayList<ArrayList<Node>> initNodes(int[][] img) {
        ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            nodes.add(new ArrayList<>());
            for (int j = 0; j < img[0].length; j++) {

                int c = img[i][j];
                nodes.get(i).add(new Node(i, j, c));
            }
        }
        return nodes;
    }

    static public ArrayList<Centroid> initCentroids(int[][] img, int num_centroids) {

        Random r = new Random();

        HashSet<String> selected = new HashSet<>();

        ArrayList<Centroid> centroids = new ArrayList<>();
        for (int n = 0; n < num_centroids; n++) {
            int x = r.nextInt(img.length);
            int y = r.nextInt(img[0].length);
            String s = x + "" + y;
            int counter = 0;
            while (selected.contains(s) && counter < 1000) {
                x = r.nextInt(img.length);
                y = r.nextInt(img[0].length);
                s = x + "" + y;
                counter += 1;
            }
            if (counter >= 999) {
                break;
            }

            selected.add(s);

            int c = img[x][y];
            centroids.add(new Centroid(x, y, c));
        }
        return centroids;
    }

    public static ArrayList<Node> initRootNodes(ArrayList<ArrayList<Node>> nodes, int numSegments) {

        ArrayList<Node> rootNodes = new ArrayList<>();

        Random r = new Random();

        HashSet<String> selected = new HashSet<>();

        for (int n = 0; n < numSegments; n++) { //init with 0 0 as root, so we take that into account for number of segments.
            int x = r.nextInt(nodes.size());
            int y = r.nextInt(nodes.get(0).size());
            String s = x + "" + y;
            int counter = 0;
            while (selected.contains(s) && (x == 0 && y == 0) && counter < 1000) {
                x = r.nextInt(nodes.size());
                y = r.nextInt(nodes.get(0).size());
                s = x + "" + y;
                counter += 1;
            }
            if (counter >= 999) {
                break;
            }

            selected.add(s);

            nodes.get(x).get(y).setRoot(true);

            rootNodes.add(nodes.get(x).get(y));
        }

        return rootNodes;
    }


}
