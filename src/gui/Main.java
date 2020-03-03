// Amjad Alissa Alkhalaf
// Rami Bader

package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    public void start(Stage myStage) {

        myStage.setTitle("Monitored Assignment2");

        EnlargedCanvas enlargedCanvas = new EnlargedCanvas();
        Buttons buttons = new Buttons();
        Separator separator = new Separator(Orientation.VERTICAL);

        HBox rootNode = new HBox(10);
        rootNode.setPadding(new Insets(10));
        rootNode.getStylesheets().add(getClass().getResource("StyleSheet.css").toString());
        rootNode.getChildren().addAll(buttons,separator, enlargedCanvas);

        Scene myScene = new Scene(rootNode,rootNode.getMinWidth(),rootNode.getMinHeight());

        myStage.setScene(myScene);
        myStage.setResizable(false);
        myStage.sizeToScene();
        myStage.show();
    }
}

