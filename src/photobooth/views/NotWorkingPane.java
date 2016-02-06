/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import photobooth.Global;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author default
 */
public class NotWorkingPane extends MyPane {

    private static NotWorkingPane instance = null;

    public NotWorkingPane() {
        initComponents();
    }

    public static NotWorkingPane getInstance() {
        if (instance == null) {
            instance = new NotWorkingPane();
        }
        return instance;
    }

    private void initComponents() {
        String insert = Global.getPhrase("not_working");
        Label label = new Label(insert);
        label.setLayoutX(0);
        label.setLayoutY(50);
        label.setMinWidth(800);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font(80));
        label.setTextFill(Color.web("#000"));
        this.getChildren().add(label);
        
        Image stopImage = new Image(this.getClass().getResourceAsStream("/photobooth/images/stop.png"));
        ImageView stopImageView = new ImageView(stopImage);
        stopImageView.setLayoutX(280);
        stopImageView.setLayoutY(200);
        this.getChildren().add(stopImageView);
    }

}
