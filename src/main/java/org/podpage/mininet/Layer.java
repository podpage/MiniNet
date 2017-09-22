package org.podpage.mininet;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by podpage on 08.08.2017.
 */
public class Layer extends ArrayList<Double>  {

    public Layer(double... layerData) {
        for (double data : layerData) {
            add(data);
        }
    }

    public Layer(ArrayList<Double> layerData) {
        addAll(layerData);
    }

    @Override
    public String toString() {
        return "[" + stream().map(Object::toString).collect(Collectors.joining(", "))
                + "]";
    }
}
