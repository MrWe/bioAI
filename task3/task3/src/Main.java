import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



// TODO: use date for x-axis
public class Main extends Application {

    String filename = "1";

    public void start(Stage stage) {

        ImportJobs imports = new ImportJobs("Data/"+filename+".txt");
        ArrayList<Machine> machines = ACO.run(imports);
        


        stage.setTitle("Gantt Chart Sample");
        GanttChart<Number, String> chart = createChart(machines, imports.numJobs);

        Scene scene  = new Scene(chart,2000,500);
        stage.setScene(scene);
        stage.show();
    }


    private ArrayList<String> generateColors(int numJobs){
        ArrayList<String> colors = new ArrayList<>();

        for (int i = 0; i < numJobs; i++) {
            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            colors.add("-fx-background-color:rgba("+r+","+g+","+b+",0.7);");
        }

        return colors;
    }

    private GanttChart<Number, String> createChart(ArrayList<Machine> machines, int numJobs){

        ArrayList<String> colors = generateColors(numJobs);

        String[] UImachines = new String[machines.size()];
        for (int i = 0; i < machines.size(); i++) {
            UImachines[i] = "Machine " + i;
        }

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChart<Number,String> chart = new GanttChart<Number,String>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(UImachines)));

        chart.setTitle("Machine Monitoring");
        chart.setLegendVisible(false);
        chart.setBlockHeight( 20);

        ArrayList<XYChart.Series> series = new ArrayList<>();

        for (int i = 0; i < UImachines.length; i++) {

            String machineName = UImachines[i];

            XYChart.Series s = new XYChart.Series();

            for(int startTime : machines.get(i).getSubJobs().keySet()){
                SubJob sj = machines.get(i).getSubJobs().get(startTime);
                XYChart.Data point = new XYChart.Data(startTime, machineName, new GanttChart.ExtraData( sj.getDuration(), "status-red", colors.get(sj.getParent().getIndex())));

                s.getData().add(point);
            }

            series.add(s);

        }

        for (XYChart.Series s : series){
            chart.getData().add(s);
        }

        return chart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}