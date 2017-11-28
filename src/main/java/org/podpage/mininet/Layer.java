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
   
    public Layer round() {
        return round(10);
    }

    public Layer round(double decimal) {
        double[] data = new double[this.size()];

        for (int i = 0; i < this.size(); i++) {
            data[i] = Math.round(this.get(i) * decimal) / decimal;
        }
        return new Layer(data);
    }
    
    public boolean equals(Layer layer) {
        for (int i = 0; i < this.size(); i++) {
            double a = this.get(i);
            double b = layer.get(i);

            if (!(a + 0.0001 > b && a - 0.0001 < b)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "[" + stream().map(Object::toString).collect(Collectors.joining(", "))
                + "]";
    }
}
