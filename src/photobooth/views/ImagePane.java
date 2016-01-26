/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import photobooth.Global;

/**
 *
 * @author default
 */
public class ImagePane extends Pane {

    private int brightness;
    private int contrast;
    private int quantity;
    private Label contrastLabel;
    private Label brightnessLabel;
    private Label quantityLabel;
    private int imageViewWidth = 600;
    private int imageViewHeight = 460;
    private final ExplorerPane backPanel;

    public ImagePane(ExplorerPane backPanel, final File imageFile) {
        this.contrast = 0;
        this.brightness = 0;
        this.quantity = 1;
        addImagePane(imageFile);
        addContrastControls();
        addBrightnessControls();
        addMovementControls();
        addPrintControls();
        addBackButton();
        this.backPanel = backPanel;
    }

    private void addImagePane(final File imageFile) {
        try {
            BorderPane borderPane = new BorderPane();
            borderPane.setLayoutX(10);
            borderPane.setLayoutY(10);
            borderPane.setMaxSize(imageViewWidth, imageViewHeight);
            borderPane.setMinSize(imageViewWidth, imageViewHeight);
            borderPane.setStyle("-fx-background-color: white");

            Rectangle rect = new Rectangle(600, 400);
            rect.setLayoutX(10);
            rect.setLayoutY(40);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(2);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.TRANSPARENT);
            ImageView imageView = new ImageView();
            BufferedImage bufImage = ImageIO.read(imageFile);
            bufImage = Scalr.resize(bufImage, Scalr.Method.BALANCED, 1800, 1800);
            if (bufImage.getHeight() > bufImage.getWidth()) {
                bufImage = Scalr.rotate(bufImage, Scalr.Rotation.CW_90, (BufferedImageOp) null);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufImage, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            Image image = new Image(is, imageViewWidth, imageViewWidth, true, true);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            borderPane.setCenter(imageView);
            this.getChildren().add(borderPane);
            this.getChildren().add(rect);
        } catch (Exception ex) {
            Logger.getLogger(ImagePane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addBrightness() {

        this.brightness++;
        if (this.brightness > 10) {
            this.brightness = 10;
        }
        updateBrightnessLabel();
    }

    private void addContrast() {

        this.contrast++;
        if (this.contrast > 10) {
            this.contrast = 10;
        }
        updateContrastLabel();
    }

    private void addQuantity() {
        this.quantity++;
        if (this.quantity > 10) {
            this.quantity = 10;
        }
        updateQuantityLabel();
    }

    private void updateContrastLabel() {
        this.contrastLabel.setText(String.valueOf(this.contrast));
    }

    private void updateBrightnessLabel() {
        this.brightnessLabel.setText(String.valueOf(this.brightness));
    }

    private void updateQuantityLabel() {
        this.quantityLabel.setText(String.valueOf(this.quantity));
    }

    private void subtractContrast() {
        this.contrast--;
        if (this.contrast < -10) {
            this.contrast = -10;
        }
        updateContrastLabel();
    }

    private void subtractBrightness() {
        this.brightness--;
        if (this.brightness < -10) {
            this.brightness = -10;
        }
        updateBrightnessLabel();
    }

    private void subtractQuantity() {
        this.quantity--;
        if (this.quantity < 1) {
            this.quantity = 1;
        }
        updateQuantityLabel();
    }

    private void addContrastControls() {
        Button leftBtn = new Button("<");
        leftBtn.setMinSize(40, 40);
        leftBtn.setMaxSize(40, 40);
        leftBtn.setLayoutX(615);
        leftBtn.setLayoutY(360);
        leftBtn.getStyleClass().add("blueButton");
        leftBtn.getStyleClass().add("small-square");
        this.getChildren().add(leftBtn);
        leftBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                subtractContrast();
            }
        });

        Button rightBtn = new Button(">");
        rightBtn.setMinSize(40, 40);
        rightBtn.setMaxSize(40, 40);
        rightBtn.setLayoutX(750);
        rightBtn.setLayoutY(360);
        rightBtn.getStyleClass().add("blueButton");
        rightBtn.getStyleClass().add("small-square");
        this.getChildren().add(rightBtn);
        rightBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addContrast();
            }
        });

        Label contrastText = new Label("Contrast");
        contrastText.setFont(new Font(20));
        contrastText.setTextFill(Color.web("#000"));
        BorderPane bpp = new BorderPane(contrastText);
        bpp.setLayoutX(610);
        bpp.setLayoutY(330);
        bpp.setMaxSize(180, 40);
        bpp.setMinSize(180, 40);
        this.getChildren().add(bpp);

        this.contrastLabel = new Label("0");
        contrastLabel.setFont(new Font(30));
        contrastLabel.setTextFill(Color.web("#000"));
        BorderPane bp = new BorderPane(contrastLabel);
        bp.setLayoutX(660);
        bp.setLayoutY(360);
        bp.setMaxSize(90, 40);
        bp.setMinSize(90, 40);
        this.getChildren().add(bp);

    }

    private void addBrightnessControls() {
        Button leftBtn = new Button("<");
        leftBtn.setMinSize(40, 40);
        leftBtn.setMaxSize(40, 40);
        leftBtn.setLayoutX(615);
        leftBtn.setLayoutY(290);
        leftBtn.getStyleClass().add("blueButton");
        leftBtn.getStyleClass().add("small-square");
        this.getChildren().add(leftBtn);
        leftBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                subtractBrightness();
            }
        });

        Button rightBtn = new Button(">");
        rightBtn.setMinSize(40, 40);
        rightBtn.setMaxSize(40, 40);
        rightBtn.setLayoutX(750);
        rightBtn.setLayoutY(290);
        rightBtn.getStyleClass().add("blueButton");
        rightBtn.getStyleClass().add("small-square");
        this.getChildren().add(rightBtn);
        rightBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addBrightness();
            }
        });

        Label contrastText = new Label("Brighness");
        contrastText.setFont(new Font(20));
        contrastText.setTextFill(Color.web("#000"));
        BorderPane bpp = new BorderPane(contrastText);
        bpp.setLayoutX(610);
        bpp.setLayoutY(260);
        bpp.setMaxSize(180, 40);
        bpp.setMinSize(180, 40);
        this.getChildren().add(bpp);

        this.brightnessLabel = new Label("0");
        brightnessLabel.setFont(new Font(30));
        brightnessLabel.setTextFill(Color.web("#000"));
        BorderPane bp = new BorderPane(brightnessLabel);
        bp.setLayoutX(660);
        bp.setLayoutY(290);
        bp.setMaxSize(90, 40);
        bp.setMinSize(90, 40);
        this.getChildren().add(bp);

    }

    private void addMovementControls() {

        Button upBtn = new Button("^");
        upBtn.setMinSize(40, 40);
        upBtn.setMaxSize(40, 40);
        upBtn.setLayoutX(635);
        upBtn.setLayoutY(170);
        upBtn.getStyleClass().add("blueButton");
        upBtn.getStyleClass().add("movement");
        this.getChildren().add(upBtn);
        upBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        Button downBtn = new Button("v");
        downBtn.setMinSize(40, 40);
        downBtn.setMaxSize(40, 40);
        downBtn.setLayoutX(635);
        downBtn.setLayoutY(220);
        downBtn.getStyleClass().add("blueButton");
        downBtn.getStyleClass().add("movement");
        this.getChildren().add(downBtn);
        downBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

    }

    private void addPrintControls() {
        Button printBtn = new Button("Print");
        printBtn.setMinSize(180, 60);
        printBtn.setMaxSize(180, 60);
        printBtn.setLayoutX(615);
        printBtn.setLayoutY(10);
        printBtn.getStyleClass().add("blueButton");
        printBtn.getStyleClass().add("movement");
        this.getChildren().add(printBtn);
        printBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });

        Button leftBtn = new Button("<");
        leftBtn.setMinSize(40, 40);
        leftBtn.setMaxSize(40, 40);
        leftBtn.setLayoutX(615);
        leftBtn.setLayoutY(100);
        leftBtn.getStyleClass().add("blueButton");
        leftBtn.getStyleClass().add("small-square");
        this.getChildren().add(leftBtn);
        leftBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                subtractQuantity();
            }
        });

        Button rightBtn = new Button(">");
        rightBtn.setMinSize(40, 40);
        rightBtn.setMaxSize(40, 40);
        rightBtn.setLayoutX(750);
        rightBtn.setLayoutY(100);
        rightBtn.getStyleClass().add("blueButton");
        rightBtn.getStyleClass().add("small-square");
        this.getChildren().add(rightBtn);
        rightBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addQuantity();
            }
        });

        Label quantityText = new Label("Quantity");
        quantityText.setFont(new Font(20));
        quantityText.setTextFill(Color.web("#000"));
        BorderPane bpp = new BorderPane(quantityText);
        bpp.setLayoutX(610);
        bpp.setLayoutY(70);
        bpp.setMaxSize(180, 40);
        bpp.setMinSize(180, 40);
        this.getChildren().add(bpp);

        this.quantityLabel = new Label(String.valueOf(quantity));
        quantityLabel.setFont(new Font(30));
        quantityLabel.setTextFill(Color.web("#000"));
        BorderPane bp = new BorderPane(quantityLabel);
        bp.setLayoutX(660);
        bp.setLayoutY(100);
        bp.setMaxSize(90, 40);
        bp.setMinSize(90, 40);
        this.getChildren().add(bp);
    }

    private void addBackButton() {
        Button printBtn = new Button("Back");
        printBtn.setMinSize(180, 50);
        printBtn.setMaxSize(180, 50);
        printBtn.setLayoutX(615);
        printBtn.setLayoutY(420);
        printBtn.getStyleClass().add("folderButton");
        this.getChildren().add(printBtn);
        printBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Global.getInstance().setSceneRoot(backPanel);
            }
        });
    }

}
