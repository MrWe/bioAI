import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



// TODO: use date for x-axis
public class GanttChartSample extends Application {

    public void start(Stage stage) {

        Logic program = new Logic();

        ArrayList<Machine> machines = program.run();

        stage.setTitle("Gantt Chart Sample");

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
                XYChart.Data point = new XYChart.Data(startTime, machineName, new GanttChart.ExtraData( sj.getDuration(), "status-red"));

                s.getData().add(point);
            }

            series.add(s);

        }

        for (XYChart.Series s : series){
            chart.getData().add(s);
        }



        //chart.getData().addAll((Collection<? extends XYChart.Series<Number, String>>) series);

        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());
        //chart.getStylesheets().add("-fx-background-color:rgba(0,0,128,0.7);");

        Scene scene  = new Scene(chart,2000,500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}