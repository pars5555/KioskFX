/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import photobooth.Global;
import photobooth.managers.PrintManager;

/**
 *
 * @author default
 */
public class PrintPane extends MyPane {

    private static PrintPane instance = null;
    private ImagePane backPanel;
    private ImageView imageView;
    private Label qtyLabel;
    private ProgressBar progressBar;
    private Timer timer;

    private PrintPane() {
        addXButton();
        addLabel();
        addImageView();
    }

    public static PrintPane getInstance() {
        if (instance == null) {
            instance = new PrintPane();
        }
        return instance;
    }

    private void addXButton() {
       Button button = new Button();
        try {
            button.setGraphic(new ImageView(new Image(getClass().getResource("/photobooth/images/exit.png").openStream())));
        } catch (IOException ex) {
            Logger.getLogger(EmailPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        button.setStyle("-fx-background-color: transparent;");
        button.setLayoutX(730);
        button.setLayoutY(10);
        button.setMaxSize(50, 50);
        button.setMinSize(50, 50);
        button.getStyleClass().add("blueButton");
        this.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timer.purge();
                timer.cancel();
                Global.getInstance().setSceneRoot(backPanel);
            }
        });
    }

    public void init(int picturesCount, BufferedImage bi, ImagePane ip) {
        PrintManager.print(bi, picturesCount);
        initCoinAcceptor();
        backPanel = ip;
        WritableImage toFXImage = SwingFXUtils.toFXImage(bi, null);
        imageView.setImage(toFXImage);
        qtyLabel.setText("Copies: " + picturesCount);
        progressBar.setProgress(0);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                progressBar.setProgress(progressBar.getProgress() + 0.01);
                if (progressBar.getProgress() >= 1) {
                    Global.getInstance().setSceneRoot(backPanel);
                    timer.purge();
                    timer.cancel();
                }
            }
        }, 1000, 100);
    }

    private void addLabel() {
        qtyLabel = new Label("ddd");
        qtyLabel.setLayoutX(50);
        qtyLabel.setLayoutY(330);
        qtyLabel.setMaxWidth(700);
        qtyLabel.setTextAlignment(TextAlignment.CENTER);

        qtyLabel.setFont(new Font(30));
        qtyLabel.setTextFill(Color.web("#000"));
        this.getChildren().add(qtyLabel);

        progressBar = new ProgressBar(0);
        progressBar.setLayoutX(50);
        progressBar.setLayoutY(380);
        progressBar.setMaxSize(700, 40);
        progressBar.setMinSize(700, 40);

        this.getChildren().add(progressBar);
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
