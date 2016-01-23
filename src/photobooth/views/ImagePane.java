/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author default
 */
public class ImagePane extends Pane {

    public ImagePane(final File imageFile) {
        try {
            Pane pane = new Pane();
            pane.setLayoutX(10);
            pane.setLayoutY(50);
            pane.setStyle("-fx-background-color: BLACK");
            pane.setMaxSize(600, 400);
            pane.setMinSize(600, 400);
            Rectangle rect = new Rectangle(600, 400);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(2);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.TRANSPARENT);
            ImageView imageView = new ImageView();
            Image image = new Image(new FileInputStream(imageFile));
            if (image.getHeight() > image.getWidth()) {

            }
            imageView.setImage(image);
            //imageView.setStyle("-fx-background-color: BLACK");
            // imageView.setFitHeight(stage.getHeight() - 10);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            

           // pane.getChildren().add(imageView);
            pane.getChildren().add(rect);
            this.getChildren().add(pane);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImagePane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
