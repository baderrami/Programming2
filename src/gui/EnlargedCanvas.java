package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import java.util.Random;

public class EnlargedCanvas extends Canvas {

    //Canvas dimensions
    private static final double CANVASWIDTH = 670;
    private static final double CANVASHEIGHT = 670;

    //Rectangle dimensions
    private static final double RECWIDTH = 40;
    private static final double RECHEIGHT = 40;

    //Gap between rectangles
    private static final double gap = 2;

    //For mouse coordinates
    private double xCoordinates;
    private double yCoordinates;

    private static GraphicsContext graphicsContext;

    public EnlargedCanvas() {
        super(CANVASWIDTH,CANVASHEIGHT);

        graphicsContext = this.getGraphicsContext2D();

        //Action MouseClicked event
        this.setOnMouseClicked(me -> {
            xCoordinates = me.getX();
            yCoordinates = me.getY();

            if(me.getButton() == MouseButton.PRIMARY) {
                fillRectangleOnMouseEvent(xCoordinates, yCoordinates);
                NNLetterRecognition.readCanvas();
            }
            if(me.getButton() == MouseButton.SECONDARY){
                clearCanvas();
                NNLetterRecognition.readCanvas();
            }
        });

        //Action MouseDragged event
        this.setOnMouseDragged(me -> {
            xCoordinates = me.getX();
            yCoordinates = me.getY();

            if(me.getButton() == MouseButton.PRIMARY) {
                fillRectangleOnMouseEvent(xCoordinates, yCoordinates);
                NNLetterRecognition.readCanvas();
            }
        });
    }

    //fillRectangleOnMouseEvent
    private static void fillRectangleOnMouseEvent(double xCoordinates, double yCoordinates) {

        double widthEnd;
        double heightEnd;

        for (double x = 0; x < CANVASWIDTH; x += RECWIDTH + gap) {
            for (double y = 0; y < CANVASHEIGHT; y += RECHEIGHT + gap) {

                widthEnd = x + RECWIDTH;
                heightEnd = y + RECHEIGHT;

                if (((xCoordinates >= x) && (xCoordinates <= widthEnd)) &&
                        ((yCoordinates >= y) && (yCoordinates <= heightEnd))) {

                    graphicsContext.fillRect(x, y, RECWIDTH, RECHEIGHT);
                }
            }
        }
    }

    //Read the canvas into an array double[256]
    public static double [] readPixels() {

        WritableImage writableImage = graphicsContext.getCanvas().snapshot(null,null);

        double[] pixelDate = new double [256];
        int index = 0;
        int pixelColor;

        for (int x = 0; x < CANVASWIDTH; x += RECWIDTH + gap) {
            for (int y = 0; y < CANVASHEIGHT; y += RECHEIGHT + gap) {

                pixelColor = writableImage.getPixelReader().getArgb(x, y);

                //-16777216 this vaule corresponds to the pixel's color is black
                if (pixelColor == -16777216) {
                    pixelDate[index] = 1.0;
                } else {
                    pixelDate[index] = 0.0;
                }
                ++index;
            }
        }
        return pixelDate;
    }

    //Filling a pattern which is stored in an array double[256].
    public static void fillSingleRectangle(double [] pixelData) {

        clearCanvas();

        int index = 0;

        for (int x = 0; x < CANVASWIDTH; x += RECWIDTH + gap) {
            for (int y = 0; y < CANVASHEIGHT; y += RECHEIGHT + gap) {
                if(pixelData[index] == 1.0) {
                    graphicsContext.fillRect(x, y,RECWIDTH, RECHEIGHT);
                }
                ++index;
            }
        }
    }

    //Appyling noise for the pattern passed
    public static double [] applyNoise(double[] pixelData) {

        Random r = new Random();

        int random;
        double [] pixelDataModified = new double[256];
        int index = 0;

        for(double pixelValue : pixelData) {
            random = r.nextInt(10);
            switch (random) {
                case 1:
                    if (pixelValue == 1) {
                        pixelDataModified[index] = 0;
                    }
                    else {
                        pixelDataModified[index] = 1;
                    }
                    break;
                default:
                    pixelDataModified[index] = pixelValue;
                    break;
            }
            ++index;
        }
        return pixelDataModified;
    }

    //Clearing EnlargedCanvas
    public static void clearCanvas() {

        graphicsContext.clearRect(0.0, 0.0, CANVASWIDTH, CANVASHEIGHT);
    }
}
