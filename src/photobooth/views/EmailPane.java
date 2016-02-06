/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import photobooth.Global;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import photobooth.managers.EmailManager;

/**
 *
 * @author default
 */
public class EmailPane extends MyPane {

    private static EmailPane instance;

    public static EmailPane getInstance() {
        if (instance == null) {
            instance = new EmailPane();
        }
        return instance;
    }
    private Timer t;
    private int checkDurationSeconds;

    private EmailPane() {
        addXButton();
        addLabel();
    }

    public void init() {
        final int checkInterval = 5;
        int totalCheckDurationSeconds = 120;
        checkDurationSeconds = 0;

        if (t != null) {
            t.cancel();
            t.purge();
        }
        t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                checkDurationSeconds += checkInterval;
                if (checkDurationSeconds > totalCheckDurationSeconds) {
                    t.cancel();
                    t.purge();
                    Global.getInstance().setSceneRoot(HomePane.getInstance());
                    return;
                }

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, -3);
                Date now = cal.getTime();
                List<BufferedImage> emailImages = EmailManager.getInstance().getEmailImages(now);
                int i = 1;
                try {
                    FileUtils.cleanDirectory(new File("C:\\Users\\default.User\\Desktop\\email"));
                    for (BufferedImage emailImage : emailImages) {
                        ImageIO.write(emailImage, "jpg", new File("C:\\Users\\default.User\\Desktop\\email\\" + i++ + ".jpg"));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(EmailPane.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!emailImages.isEmpty()) {
                    t.cancel();
                    t.purge();
                    ExplorerPane.getInstance().setDir("C:\\Users\\default.User\\Desktop\\email");
                    Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                }
            }
        }, 1000, checkInterval*1000);
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
                t.cancel();
                t.purge();
                Global.getInstance().setSceneRoot(HomePane.getInstance());
            }
        });
    }

    private void addLabel() {
        String text = Global.getPhrase("email_pane_text");
        Label label = new Label(text);
        label.setWrapText(true);
        label.setLayoutX(50);
        label.setLayoutY(150);
        label.setMaxWidth(700);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(40));
        label.setTextFill(Color.web("#000"));
        this.getChildren().add(label);
    }

}
