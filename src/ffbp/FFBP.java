package ffbp;

public class FFBP {

    private int[] layout;
    private Layer[] layer;
    private double eta = 1.0;
    private double alpha = 0.0;
    private double aggregateDelta = 0.0;

    private class Cell {
        public double[] weight;
        public double[] deltaWeight;
        public double bias;
        public double deltaBias;
        public double activation;
        public double delta;

        public Cell(int fanIn) {
            weight = new double[fanIn];
            for (int i = 0; i < fanIn; ++i) weight[i] = 0.0;
            deltaWeight = new double[fanIn];
            for (int i = 0; i < fanIn; ++i) deltaWeight[i] = 0.0;
            bias = 0.0;
            deltaBias = 0.0;
            activation = 0.0;
            delta = 0.0;
        }
    }

    private class Layer {
        public Cell[] cell;

        public Layer(int size, int fanIn) {
            cell = new Cell[size];
            for (int i = 0; i < size; ++i)
                cell[i] = new Cell(fanIn);
        }
    }

    public FFBP(int[] layout) {
        try {
            // copy the layout vector
            this.layout = new int[layout.length];
            for (int i = 0; i < layout.length; ++i)
                this.layout[i] = layout[i];
            // create layer structure
            layer = new Layer[layout.length];
            for (int i = 0; i < layout.length; ++i)
                layer[i] = new Layer(layout[i], i == 0 ? 0 : layout[i-1]);
            // preset parameters
            eta = 1.0;
            alpha = 0.0;
            aggregateDelta = 0.0;
        } catch (Exception e) {
            throw new FFBPError();
        }
    }

    public void randomize(double lowerBound, double upperBound) {
        try {
            java.util.Random r = new java.util.Random();
            for (int i = 1; i < layout.length; ++i) {
                for (int j = 0; j < layout[i]; ++j) {
                    for (int k = 0; k < layout[i-1]; ++k) {
                        layer[i].cell[j].weight[k] = lowerBound + r.nextDouble() * (upperBound - lowerBound);
                        layer[i].cell[j].deltaWeight[k] = 0.0;
                    }
                    layer[i].cell[j].bias = lowerBound + r.nextDouble() * (upperBound - lowerBound);
                    layer[i].cell[j].activation = 0.0;
                    layer[i].cell[j].delta = 0.0;
                    layer[i].cell[j].deltaBias = 0.0;
                }
            }
            aggregateDelta = 0.0;
        } catch (Exception e) {
            throw new FFBPError();
        }
    }

    public void activateInputAndFeedForward(double[] input) {
        try {
            if (input.length != layer[0].cell.length)
                throw new FFBPError();
            // copy the input vector
            for (int i = 0; i < input.length; ++i)
                layer[0].cell[i].activation = input[i];
            // feed forward
            for (int i = 1; i < layout.length; ++i)
                for (int j = 0; j < layout[i]; ++j) {
                    double net = layer[i].cell[j].bias;
                    for (int k = 0; k < layout[i-1]; ++k)
                        net += layer[i-1].cell[k].activation * layer[i].cell[j].weight[k];
                    layer[i].cell[j].activation = 1.0 / (1.0 + java.lang.Math.exp(-net));
                }
            aggregateDelta = 0.0;
        } catch (Exception e) {
            throw new FFBPError();
        }
    }

    public double[] getOutput() {
        try {
            double[] output = new double[layout[layout.length-1]];
            for (int i = 0; i < output.length; ++i)
                output[i] = layer[layout.length-1].cell[i].activation;
            return output;
        } catch (Exception e) {
            throw new FFBPError();
        }
    }

    public void applyDesiredOutputAndPropagateBack(double[] desired) {
        try {
            if (desired.length != layer[layout.length-1].cell.length)
                throw new FFBPError();
            aggregateDelta = 0.0;
            // calculate delta at output vector
            for (int i = 0; i < desired.length; ++i) {
                layer[layout.length-1].cell[i].delta =
                        (desired[i] - layer[layout.length-1].cell[i].activation) *
                                layer[layout.length-1].cell[i].activation *
                                (1.0 - layer[layout.length-1].cell[i].activation);
                aggregateDelta += java.lang.Math.abs(layer[layout.length-1].cell[i].delta);
            }
            // calculate delta at hidden units
            for (int i = layout.length-2; i > 0; --i) {
                for (int j = 0; j < layout[i]; ++j) {
                    layer[i].cell[j].delta = 0.0;
                    for (int k = 0; k < layout[i+1]; ++k)
                        layer[i].cell[j].delta +=
                                layer[i+1].cell[k].delta * layer[i+1].cell[k].weight[j];
                    layer[i].cell[j].delta *=
                            layer[i].cell[j].activation * (1.0 - layer[i].cell[j].activation);
                }
            }
            // calculate weight changes and apply
            for (int i = 1; i < layout.length; ++i)
                for (int j = 0; j < layout[i]; ++j) {
                    layer[i].cell[j].deltaBias =
                            eta * layer[i].cell[j].delta + alpha * layer[i].cell[j].deltaBias;
                    layer[i].cell[j].bias += layer[i].cell[j].deltaBias;
                    for (int k = 0; k < layout[i-1]; ++k) {
                        layer[i].cell[j].deltaWeight[k] =
                                eta * layer[i-1].cell[k].activation * layer[i].cell[j].delta +
                                        alpha * layer[i].cell[j].deltaWeight[k];
                        layer[i].cell[j].weight[k] += layer[i].cell[j].deltaWeight[k];
                    }
                }
        } catch (Exception e) {
            throw new FFBPError();
        }
    }

    public double getAlpha() {
        return alpha;
    }
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
    public double getEta() {
        return eta;
    }
    public void setEta(double eta) {
        this.eta = eta;
    }
    public double getAggregateDelta() {
        return aggregateDelta;
    }
}

