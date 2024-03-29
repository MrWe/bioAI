import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    private String filename = "6";

    public void start(Stage s1) {

        int optimalValue = readOptimalValue(filename);
        ImportJobs imports = new ImportJobs("Data/"+filename+".txt");
        boolean enableBees = true;
        boolean enableAnts = true;


        Stage s2 = new Stage();

        s1.setTitle("Bees");
        s2.setTitle("Ants");

        if(enableBees) {
            final long startTime = System.currentTimeMillis();

            ArrayList<Machine> machinesBee = BeesAlgorithm.run(optimalValue);

            final long endTime = System.currentTimeMillis();
            System.out.println("Total BA time: " + (endTime - startTime) );

            GanttChart<Number, String> chartBee = createChart(machinesBee, ImportJobs.numJobs);
            s1.setScene(new Scene(chartBee,2000,400));
            s1.show();

        }
        if(enableAnts) {
            final long startTime = System.currentTimeMillis();

            ArrayList<Machine> machinesAnt = ACO.run(optimalValue);

            final long endTime = System.currentTimeMillis();
            System.out.println("Total ACO time: " + (endTime - startTime) );

            GanttChart<Number, String> chartAnt = createChart(machinesAnt, ImportJobs.numJobs);
            s2.setScene(new Scene(chartAnt, 2000, 400));
            s2.show();
        }

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

        int intensity = 255;

        while(colors.size() < numJobs) {

            int r = intensity;
            int g = 0;
            int b = 0;
            colors.add("-fx-background-color:rgba(" + r + "," + g + "," + b + ",0.7);");

            r = 0;
            g = intensity;
            b = 0;
            colors.add("-fx-background-color:rgba(" + r + "," + g + "," + b + ",0.7);");

            r = 0;
            g = 0;
            b = intensity;
            colors.add("-fx-background-color:rgba(" + r + "," + g + "," + b + ",0.7);");

            r = 0;
            g = intensity;
            b = intensity;
            colors.add("-fx-background-color:rgba(" + r + "," + g + "," + b + ",0.7);");

            r = intensity;
            g = 0;
            b = intensity;
            colors.add("-fx-background-color:rgba(" + r + "," + g + "," + b + ",0.7);");

            r = intensity;
            g = intensity;
            b = 0;
            colors.add("-fx-background-color:rgba(" + r + "," + g + "," + b + ",0.7);");

            intensity /= 2;
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
                XYChart.Data point = new XYChart.Data(startTime, machineName, new GanttChart.ExtraData( sj.getDuration(), "status-red", colors.get(sj.getParent().getIndex()), sj.getIndex(), sj.getParent().getIndex()));

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