/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.io.File;
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
public class HomePane extends MyPane {

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
                            
                            
                            
                            File[] roots = File.listRoots();                         
                            for (int i = 0; i < roots.length; i++) {
                                String path = roots[i].getPath();
                                if (path.startsWith("E")) {
                                    ExplorerPane.getInstance().setDir("E:\\");
                                    Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                                    return;
                                }
                                if (path.startsWith("F")) {
                                    ExplorerPane.getInstance().setDir("F:\\");
                                    Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                                    return;
                                }
                                if (path.startsWith("G")) {
                                    ExplorerPane.getInstance().setDir("G:\\");
                                    Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                                    return;
                                }
                                if (path.startsWith("H")) {
                                    ExplorerPane.getInstance().setDir("H:\\");
                                    Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                                    return;
                                }
                                if (path.startsWith("I")) {
                                    ExplorerPane.getInstance().setDir("I:\\");
                                    Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                                    return;
                                }
                            }
                            Global.getInstance().setSceneRoot(HomePane.getInstance());
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
