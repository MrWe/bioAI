import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class ImportJobs {

    public int numMachines;
    public int numJobs;
    public ArrayList<ArrayList<String>> stringJobs;

    ImportJobs(String file){
        File f = new File(file);

        stringJobs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            String params = br.readLine().trim();

            numJobs = Integer.parseInt(params.split("\\s+")[0]);
            numMachines = Integer.parseInt(params.split("\\s+")[1]);
            while ((line = br.readLine()) != null) {
                stringJobs.add(new ArrayList<String>(Collections.singleton(line)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
