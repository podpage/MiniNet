package org.podpage.mininet;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by podpage on 08.08.2017.
 */
public class DataSet extends ArrayList<LayerSet> {

    public DataSet(LayerSet... sets) {
        for (LayerSet set : sets) {
            add(set);
        }
    }

    public DataSet(ArrayList<LayerSet> layerSets) {
        addAll(layerSets);
    }

    public DataSet addChain(LayerSet set) {
        add(set);
        return this;
    }
    
     public boolean isCompliant() {
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (i == j) {
                    continue;
                }
                LayerSet a = this.get(i);
                LayerSet b = this.get(j);
                if(a.getInput().equals(b.getInput())) {
                    if(!a.getOutput().equals(b.getOutput())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void save(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(os);
            String data = new Gson().toJson(this);
            pw.print(data);
            pw.flush();
            pw.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataSet load(File file) {
        if (file.exists()) {
            try {
                FileInputStream is = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String data = sb.toString();

                DataSet dataSet = new Gson().fromJson(data, DataSet.class);
                br.close();
                is.close();
                return dataSet;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
