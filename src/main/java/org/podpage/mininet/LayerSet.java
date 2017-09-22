package org.podpage.mininet;

/**
 * Created by podpage on 08.08.2017.
 */
public class LayerSet {

    public Layer input;
    public Layer output;

    public LayerSet(Layer input, Layer output) {
        this.input = input;
        this.output = output;
    }

    public Layer getInput() {
        return input;
    }

    public void setInput(Layer input) {
        this.input = input;
    }

    public Layer getOutput() {
        return output;
    }

    public void setOutput(Layer output) {
        this.output = output;
    }
}
