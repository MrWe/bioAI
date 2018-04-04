import java.util.ArrayList;

public class PheromoneMatrix {

    ArrayList<ArrayList<Double>> matrix;
    double p = 0.1;
    double min = .001;
    double max = 2.0;


    PheromoneMatrix(int dimensions, double initVal){
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
        this.matrix.get(i).set(j, y < this.max ? y + value : y);
    }

}
