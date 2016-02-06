/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import photobooth.Global;

/**
 *
 * @author default
 */
public class InsertCoinPane extends MyPane {

    private static InsertCoinPane instance = null;
    private Label insertLabel;
    private ImagePane backPanel;
    private ImageView imageView;
    private Label insertedLabel;

    private InsertCoinPane() {
        addXButton();
        addLabel();
        addImageView();
    }

    public static InsertCoinPane getInstance() {
        if (instance == null) {
            instance = new InsertCoinPane();
        }
        return instance;
    }

    private void addXButton() {
        Button exitButton = new Button("X");
        exitButton.setLayoutX(720);
        exitButton.setLayoutY(10);
        exitButton.getStyleClass().add("blueButton");
        this.getChildren().add(exitButton);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                destroyCoinAcceptor();
                Global.getInstance().setSceneRoot(backPanel);
            }
        });
    }

    public void init(int picturesCount, BufferedImage bi, ImagePane ip) {
        initCoinAcceptor();
        backPanel = ip;
        WritableImage toFXImage = SwingFXUtils.toFXImage(bi, null);
        imageView.setImage(toFXImage);
        insertLabel.setText("Insert " + picturesCount * 100 + " AMD to print");
    }

    private void addLabel() {
        Label qtyLabel = new Label("Quantity: ");
        qtyLabel.setLayoutX(50);
        qtyLabel.setLayoutY(330);
        qtyLabel.setFont(new Font(30));
        qtyLabel.setTextFill(Color.web("#000"));
        this.getChildren().add(qtyLabel);
        
        insertLabel = new Label("Insert ");
        insertLabel.setLayoutX(50);
        insertLabel.setLayoutY(370);
        insertLabel.setFont(new Font(30));
        insertLabel.setTextFill(Color.web("#000"));
        this.getChildren().add(insertLabel);
        
        insertedLabel = new Label("Inserted: 0 AMD");
        insertedLabel.setLayoutX(50);
        insertedLabel.setLayoutY(410);
        insertedLabel.setFont(new Font(30));
        insertedLabel.setTextFill(Color.web("#000"));
        this.getChildren().add(insertedLabel);
    }

    private void initCoinAcceptor() {
    }

    private void destroyCoinAcceptor() {
    }

    private void addImageView() {
        imageView = new ImageView();
        imageView.setLayoutX(150);
        imageView.setLayoutY(20);
        imageView.setFitWidth(450);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        this.getChildren().add(imageView);

    }

}
