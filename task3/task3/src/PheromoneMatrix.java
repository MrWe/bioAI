import java.util.ArrayList;

public class PheromoneMatrix {

    private ArrayList<ArrayList<Double>> matrix;
    private double p = 0.0001;
    private double min = 0.0001;
    private double max = 1;


    PheromoneMatrix(int dimensions, double initVal){

        matrix = new ArrayList<>();

        for (int i = 0; i < dimensions; i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < dimensions; j++) {
                row.add(initVal);
            }
            matrix.add(row);
        }
    }

    public double get(int i, int j){
        return matrix.get(i).get(j);
    }

    public void evaporate(){
        for (ArrayList<Double> m : this.matrix) {
            for (int j = 0; j < m.size(); j++) {
                double y = m.get(j);
                m.set(j, y > this.min ? y * (1 - this.p) : y);
            }
        }
    }

    public void updateSingle(int i, int j, double value){
        double y = this.matrix.get(i).get(j);
        this.matrix.get(i).set(j, y < this.max ? y * value : y);
    }

    public void show(){
        for (ArrayList<Double> row : this.matrix){
            for(double d : row){
                System.out.print(d + " ");
            }
            System.out.println();
        }
    }

}