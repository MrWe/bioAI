import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private String filename = "3";

    public void start(Stage stage) {

        int optimalValue = readOptimalValue(filename);

        ImportJobs imports = new ImportJobs("Data/"+filename+".txt");

        //ArrayList<Machine> machines = BeesAlgorithm.run(imports, optimalValue);

        /*ACO aco = new ACO();

        ArrayList<Machine> machines = aco.run(optimalValue);
        */

        Astar a = null;
        ArrayList<Machine> machines = new ArrayList<>();

        try{
            a = new Astar(optimalValue);
        }
        catch(Exception e){
            System.out.println("Heisann");
            System.out.println(e);
            machines = a.bestMachines;
        }

        if(machines.size() == 0){
            machines = a.bestMachines;
        }

        stage.setTitle("Gantt");
        GanttChart<Number, String> chart = createChart(machines, ImportJobs.numJobs);

        Scene scene  = new Scene(chart,2000,500);
        stage.setScene(scene);
        stage.show();

    }

    private int readOptimalValue(String filename){
        File f = new File("optimals.txt");

        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(lines.get(Integer.parseInt(filename)-1));
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