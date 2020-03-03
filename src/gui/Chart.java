package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;


public class Chart extends Canvas {

    //Canvas dimensions
    private static final double CANVASWIDTH = 350;
    private static final double CANVASHEIGHT = 340;

    //Colors
    private static final Paint BARCOLOR = Color.DARKCYAN;
    private static final Paint TEXTCOLOR = Color.DARKBLUE;
    private static final Paint LINECOLOR = Color.GREY;
    private static final Font textFont = new Font("Arial",14);

    //Factor for scaling the result which comes from the nn
    private static final double FACTOR = 300;

    // bar width
    private static final double BARWIDTH = 12;

    private static GraphicsContext graphicsContext;

    public Chart() {
        super(CANVASWIDTH,CANVASHEIGHT);

        graphicsContext = this.getGraphicsContext2D();

        // Tick numbers
        graphicsContext.setStroke(TEXTCOLOR);
        double xTickMark = 0;
        graphicsContext.strokeText("0.0", xTickMark,310);
        graphicsContext.strokeText("0.1", xTickMark,280);
        graphicsContext.strokeText("0.2", xTickMark,250);
        graphicsContext.strokeText("0.3", xTickMark,220);
        graphicsContext.strokeText("0.4", xTickMark,190);
        graphicsContext.strokeText("0.5", xTickMark,160);
        graphicsContext.strokeText("0.6", xTickMark,130);
        graphicsContext.strokeText("0.7", xTickMark,100);
        graphicsContext.strokeText("0.8", xTickMark,70);
        graphicsContext.strokeText("0.9", xTickMark,40);
        graphicsContext.strokeText("1.0", xTickMark,10);

        // Letters
        graphicsContext.setStroke(TEXTCOLOR);
        graphicsContext.setFont(textFont);
        double yLetterMark = 330;
        graphicsContext.strokeText("A",40, yLetterMark);
        graphicsContext.strokeText("B",80, yLetterMark);
        graphicsContext.strokeText("C",120, yLetterMark);
        graphicsContext.strokeText("D",160, yLetterMark);
        graphicsContext.strokeText("E",200, yLetterMark);
        graphicsContext.strokeText("F",240, yLetterMark);
        graphicsContext.strokeText("G",280, yLetterMark);
        graphicsContext.strokeText("H",320, yLetterMark);

        setBarValues();
    }

    //filling the bars according to their respective letters and values
    public static void setBarValues() {

        graphicsContext.clearRect(40,10,310,300);
        double[] values = NNLetterRecognition.getOutPut();

        // Backgroung lines
        graphicsContext.setStroke(LINECOLOR);
        for(int i = 10; i <= 320; i= i+30) {
            graphicsContext.strokeLine(30,i,350,i);
        }

        // Filling the bars
        // 310-barHeight because it fills top bottom
        // (the modification is to draw the bar correctly)
        graphicsContext.setFill(BARCOLOR);
        double xBarCordinates = 40;
        double yBarCordinates;
        double barHeight;

        for (int index = 0; index < 8; ++index) {
            barHeight = values[index] * FACTOR;
            yBarCordinates = 310 - barHeight;
            graphicsContext.fillRect(xBarCordinates, yBarCordinates, BARWIDTH, barHeight);
            xBarCordinates +=40;
        }
    }
}
