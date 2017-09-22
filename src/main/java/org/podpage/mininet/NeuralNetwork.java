package org.podpage.mininet;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by podpage on 08.08.2017.
 */
public class NeuralNetwork {

    private NetWorker worker;
    private ArrayList<Integer> layer;

    private int speed = 1;
    private double learningRate = 0.5;

    private int step = 0;
    private int counter = 0;

    private ArrayList<Double> error = new ArrayList<>();
    private DataSet dataSet;

    private boolean debug = true;
    private boolean run = true;

    private double lastError = 1;

    private int ecStep = Integer.MAX_VALUE;
    private double ecError = 0.05;

    public NeuralNetwork(DataSet dataSet, int... layers) {
        this.layer = makeLayers(layers);
        this.dataSet = dataSet;
        this.speed = 3;
        this.init();
        this.reset();
    }


    NeuralNetwork() {
        this.speed = 3;
    }

    private void init() {
        this.worker = new NetWorker(this.layer);
    }

    private void reset() {
        this.worker.initState();
    }

    private void startTraining() {
        this.run = true;
        this.step = 0;
        this.counter = 0;
        this.error.clear();
    }

    private void done() {
        lastError = getMax(error);
        run = false;
    }

    public Layer process(Layer layer) {
        return new Layer(worker.process(layer));
    }

    public void train() {
        if (dataSet == null || dataSet.size() == 0) {
            return;
        }
        startTraining();
        while (run) {
            tick();
        }
    }

    private void tick() {
        this.error.clear();
        int i;
        DataSet pattern = dataSet;
        if (this.speed <= 1) {
            if (this.speed == 0 && this.counter++ % 50 != 0) return;
            i = this.step % pattern.size();
            this.worker.update(pattern.get(i).getInput());
            this.error.add(this.worker.backPropagate(pattern.get(i).getOutput(), this.learningRate));
            this.step++;
            if (this.ecStep <= this.step || (this.ecError >= 0 && i == pattern.size() - 1 && this.ecError > getMax(this.error))) {
                this.done();
            }
        } else {
            double epoch = Math.pow(10, this.speed - 1);
            loop:
            for (int n = 0; n < epoch; ++n) {
                for (i = 0; i < pattern.size(); ++i) {
                    this.worker.update(pattern.get(i).getInput());
                    this.error.add(this.worker.backPropagate(pattern.get(i).getOutput(), this.learningRate));
                    this.step++;
                    if (this.ecStep <= this.step || (this.ecError >= 0 && i == pattern.size() - 1 && this.ecError > getMax(this.error))) {
                        this.done();
                        break loop;
                    }
                }
            }
            this.worker.update(pattern.get(this.counter++ % pattern.size()).getInput());
        }

        if (debug) {
            System.out.println("Steps:" + this.step);
            System.out.println("Max Error:" + getMax(this.error));
        }
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setErrorMargin(double errorMargin) {
        this.ecError = errorMargin;
    }

    public void setMaxStep(int maxStep) {
        this.ecStep = maxStep;
    }

    public void showDebug(boolean debug) {
        this.debug = debug;
    }

    public double getLastError() {
        return lastError;
    }

    public int getStep() {
        return step;
    }

    public NetWorker getWorker() {
        return worker;
    }

    public void setWorker(NetWorker worker) {
        this.worker = worker;
    }

    public void save(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(os);
            String data = new Gson().toJson(getWorker());
            pw.print(data);
            pw.flush();
            pw.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NeuralNetwork load(File file) {
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

                NetWorker worker = new Gson().fromJson(data, NetWorker.class);

                NeuralNetwork neuralNetwork = new NeuralNetwork();
                neuralNetwork.setWorker(worker);
                br.close();
                is.close();
                return neuralNetwork;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private double getMax(ArrayList<Double> ds) {
        double max = 0;
        for (double d : ds) {
            if (d > max) {
                max = d;
            }
        }
        return max;
    }

    private ArrayList<Integer> makeLayers(int... layers) {
        ArrayList<Integer> hlayer = new ArrayList<>();
        for (int layer : layers) {
            hlayer.add(layer);
        }
        return hlayer;
    }
}
