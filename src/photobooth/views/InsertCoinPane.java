/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import photobooth.Global;

/**
 *
 * @author default
 */
public class InsertCoinPane extends Pane {

    private static InsertCoinPane instance = null;
    private Label l;
    private ImagePane backPanel;

    private InsertCoinPane() {
        addXButton();
        addLabel();
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

    public void init(int picturesCount, ImagePane ip) {
        initCoinAcceptor();
        backPanel = ip;
        l.setText("Insert " + picturesCount * 100 + " AMD to print");
    }

    private void addLabel() {
        l = new Label("Insert ");
        l.setWrapText(true);
        l.setLayoutX(50);
        l.setLayoutY(200);
        l.setMaxWidth(700);
        l.setTextAlignment(TextAlignment.CENTER);
        l.setFont(new Font(40));
        l.setTextFill(Color.web("#000"));
        this.getChildren().add(l);
    }

    private void initCoinAcceptor() {
    }

    private void destroyCoinAcceptor() {
    }

}
