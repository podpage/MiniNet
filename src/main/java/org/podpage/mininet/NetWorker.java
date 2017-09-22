package org.podpage.mininet;

import java.util.ArrayList;

/**
 * Created by podpage on 08.08.2017.
 */
public class NetWorker {

    private ArrayList<Integer> elements;
    private ArrayList<ArrayList<Double>> activations = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Double>>> weights = new ArrayList<>();

    public NetWorker(ArrayList<Integer> layer) {
        this.elements = layer;
    }

    public void initState() {
        activations.clear();
        weights.clear();

        for (Integer element : elements) {
            activations.add(createActivationLayer(element));
        }

        for (int l = 0; l < elements.size() - 1; ++l) {
            weights.add(new ArrayList<>());
            for (int i = 0; i < elements.get(l); ++i) {
                weights.get(l).add(new ArrayList<>());
                for (int j = 0; j < elements.get(l + 1); ++j) {
                    weights.get(l).get(i).add(rand(-1, 1));
                }
            }
        }
    }

    public ArrayList<Double> process(Layer inputs) {
        return update(inputs);
    }

    public ArrayList<Double> update(Layer inputs) {
        if (inputs.size() != this.elements.get(0))
            System.out.println("Input sizes does not match!");
        int l, i, j;

        for (i = 0; i < inputs.size(); ++i)
            activations.get(0).set(i, inputs.get(i));
        for (l = 0; l < this.elements.size() - 1; ++l) {
            for (j = 0; j < this.elements.get(l + 1); ++j) {
                double sum = 0;
                for (i = 0; i < this.elements.get(l); ++i) {
                    sum += activations.get(l).get(i) * this.weights.get(l).get(i).get(j);
                }
                activations.get(l + 1).set(j, sigmoid(sum));
            }
        }
        return activations.get(activations.size() - 1);
    }

    public double backPropagate(Layer target, double learningRate) {
        if (target.size() != this.elements.get(this.elements.size() - 1))
            System.out.println("Target size does not match!");

        ArrayList<ArrayList<Double>> a = activations;
        ArrayList<ArrayList<ArrayList<Double>>> w = weights;
        int l, i, j;
        ArrayList<ArrayList<Double>> delta = new ArrayList<>();
        for (l = 0; l < this.elements.size(); ++l) {
            delta.add(new ArrayList<>());
            for (i = 0; i < this.elements.get(l); ++i)
                delta.get(l).add(0d);
        }

        // hidden -> output
        int o = this.elements.size() - 1;
        for (i = 0; i < this.elements.get(o); ++i) {
            delta.get(o).set(i, (target.get(i) - a.get(o).get(i)) * dsigm(a.get(o).get(i)));
        }

        // hidden or input -> hidden
        for (l = this.elements.size() - 2; l > 0; --l) {
            for (i = 0; i < this.elements.get(l); ++i) {
                for (j = 0; j < this.elements.get(l + 1); ++j) {
                    delta.get(l).set(i, delta.get(l).get(i) + delta.get(l + 1).get(j) * w.get(l).get(i).get(j));
                }
                delta.get(l).set(i, delta.get(l).get(i) * dsigm(a.get(l).get(i)));
            }
        }

        // update weights
        for (l = 0; l < this.elements.size() - 1; ++l) {
            for (i = 0; i < this.elements.get(l); ++i) {
                for (j = 0; j < this.elements.get(l + 1); ++j) {
                    w.get(l).get(i).set(j, w.get(l).get(i).get(j) + learningRate * delta.get(l + 1).get(j) * a.get(l).get(i));
                }
            }
        }

        double error = 0;
        for (i = 0; i < target.size(); ++i)
            error = Math.max(Math.abs(target.get(i) - a.get(o).get(i)), error);
        return error;
    }

    private ArrayList<Double> createActivationLayer(int size) {
        ArrayList<Double> layer = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            layer.add(0D);
        }
        return layer;
    }

    private double rand(double a, double b) {
        return (b - a) * Math.random() + a;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double dsigm(double y) {
        return y * (1 - y);
    }
}
