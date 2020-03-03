package gui;

import ffbp.*;
import javafx.scene.layout.VBox;
import java.util.Random;

public class NNLetterRecognition extends VBox {

    private static FFBP nn;

    //The desired output values of each letter
    private static double [] outputValueA = {1,0,0,0,0,0,0,0};
    private static double [] outputValueB = {0,1,0,0,0,0,0,0};
    private static double [] outputValueC = {0,0,1,0,0,0,0,0};
    private static double [] outputValueD = {0,0,0,1,0,0,0,0};
    private static double [] outputValueE = {0,0,0,0,1,0,0,0};
    private static double [] outputValueF = {0,0,0,0,0,1,0,0};
    private static double [] outputValueG = {0,0,0,0,0,0,1,0};
    private static double [] outputValueH = {0,0,0,0,0,0,0,1};

    public NNLetterRecognition() {
        super (5);

        //First initialization of the nn
        int[] layout = {256, 16, 8};
        nn = new FFBP(layout);
        nn.randomize(-0.1, +0.1);
        nn.setEta(0.5);
        nn.setAlpha(0.5);

        Chart outputChart = new Chart();

        this.getChildren().add(outputChart);
    }

    //New net
    public static void newNetwork() {

        int[] layout = {256, 16, 8};
        nn = new FFBP(layout);
        nn.randomize(-0.1, +0.1);
        nn.setEta(0.5);
        nn.setAlpha(0.5);
    }

    //Training method
    public static void training(double[] iv0, double[] iv1, double[] iv2, double[] iv3,
                                double[] iv4, double[] iv5, double[] iv6, double[] iv7) {

        Random r = new Random();
        int random;

        for (int i = 0; i <= 500; ++i) {
            random = r.nextInt(8);
            switch (random) {
                case 0:
                    nn.activateInputAndFeedForward(iv0);
                    nn.applyDesiredOutputAndPropagateBack(outputValueA);
                    break;
                case 1:
                    nn.activateInputAndFeedForward(iv1);
                    nn.applyDesiredOutputAndPropagateBack(outputValueB);
                    break;
                case 2:
                    nn.activateInputAndFeedForward(iv2);
                    nn.applyDesiredOutputAndPropagateBack(outputValueC);
                    break;
                case 3:
                    nn.activateInputAndFeedForward(iv3);
                    nn.applyDesiredOutputAndPropagateBack(outputValueD);
                    break;
                case 4:
                    nn.activateInputAndFeedForward(iv4);
                    nn.applyDesiredOutputAndPropagateBack(outputValueE);
                    break;
                case 5:
                    nn.activateInputAndFeedForward(iv5);
                    nn.applyDesiredOutputAndPropagateBack(outputValueF);
                    break;
                case 6:
                    nn.activateInputAndFeedForward(iv6);
                    nn.applyDesiredOutputAndPropagateBack(outputValueG);
                    break;
                case 7:
                    nn.activateInputAndFeedForward(iv7);
                    nn.applyDesiredOutputAndPropagateBack(outputValueH);
                    break;
            }
        }
    }

    //Reading the pattern from the canvas and passing it to the nn
    public static void readCanvas() {
        double[] input = EnlargedCanvas.readPixels();
        nn.activateInputAndFeedForward(input);
        Chart.setBarValues();
    }

    //Gtting the result from the nn, which corresponds to the last layer of the net
    public static double [] getOutPut() {

        return nn.getOutput();
    }

    //Apply Noise if tbNoise is selected
    private static double[] getInputLetterWithNoise(double [] inputLetter) {
        return EnlargedCanvas.applyNoise(inputLetter);
    }
}

