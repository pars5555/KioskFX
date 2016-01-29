/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import photobooth.Global;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author default
 */
public class HomePane extends Pane {

    private static HomePane instance = null;

    public HomePane() {
        String text = Global.getPhrase("home_text");
        Label label = new Label(text);
        label.setWrapText(true);
        label.setLayoutX(50);
        label.setLayoutY(200);
        label.setMaxWidth(700);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(40));
        label.setTextFill(Color.web("#000"));
        this.getChildren().add(label);

        Button but1 = new Button("USB, Memory Card");
        but1.setMinWidth(250);
        but1.getStyleClass().add("blueButton");
        but1.setLayoutX(100);
        but1.setLayoutY(50);
        but1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Global.getInstance().setSceneRoot(LoadingPane.getInstance());

                Platform.runLater(() -> {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ExplorerPane.getInstance().setDir("C:\\Users\\default.User\\Desktop\\moyka");
                            Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                        }
                    }).start();
                });

            }
        });
        this.getChildren().add(but1);

        Button but2 = new Button("Email");
        but2.setMinWidth(250);
        but2.getStyleClass().add("blueButton");
        but2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EmailPane.getInstance().init();
                Global.getInstance().setSceneRoot(EmailPane.getInstance());
            }
        });
        but2.setLayoutX(450);
        but2.setLayoutY(50);
        this.getChildren().add(but2);

    }

    public static HomePane getInstance() {
        if (instance == null) {
            instance = new HomePane();
        }
        return instance;
    }

}
