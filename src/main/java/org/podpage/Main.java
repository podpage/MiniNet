package org.podpage;

import org.podpage.mininet.DataSet;
import org.podpage.mininet.Layer;
import org.podpage.mininet.LayerSet;
import org.podpage.mininet.NeuralNetwork;

import java.io.File;

/**
 * Created by podpage on 08.08.2017.
 */
public class Main {

    public static void main(String... args) {

        DataSet dataSet = new DataSet(
                new LayerSet(new Layer(1, 1), new Layer(1)),
                new LayerSet(new Layer(0, 0), new Layer(1)),
                new LayerSet(new Layer(0, 1), new Layer(0)),
                new LayerSet(new Layer(1, 0), new Layer(0))
        );

        dataSet.save(new File("C:\\Users\\NAME\\Desktop\\test.mset"));
        DataSet.load(new File("C:\\Users\\NAME\\Desktop\\test.mset"));

        NeuralNetwork nN = new NeuralNetwork(dataSet, 2, 4, 4, 1);
        nN.showDebug(true);
        nN.setSpeed(3);
        nN.setErrorMargin(0.01);
        nN.train();

        nN.save(new File("C:\\Users\\NAME\\Desktop\\test.mnet"));

        nN = NeuralNetwork.load(new File("C:\\Users\\NAME\\Desktop\\test.mnet"));

        System.out.println(nN.process(new Layer(1, 1)));
        System.out.println(nN.process(new Layer(0, 0)));
        System.out.println(nN.process(new Layer(0, 1)));
        System.out.println(nN.process(new Layer(1, 0)));
    }
}