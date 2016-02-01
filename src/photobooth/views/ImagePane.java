/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RenderedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import photobooth.Global;
import photobooth.managers.ImageManager;

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
    public static int imageViewWidth = 600;
    public static int imageViewHeight = 460;
    public static int printViewWidth = 600;
    public static int printViewHeight = 400;
    private ExplorerPane backPanel;
    private BufferedImage baseBuffImage;
    private ImageView imageView;
    private static ImagePane instance;

    public static ImagePane getInstance() {
        if (instance == null) {
            instance = new ImagePane();
        }
        return instance;
    }
    private int rectangleInitialTop = 30;
    public static int printImageWidth = 1800;
    public static int printImageHeight = 1200;
    private int rectangleTop;
    private Rectangle rect;
    private boolean noRectMove;
    private int minRectVerticalMargin;
    private boolean printFullImageBtnVisible;
    private VBox dontCropContainer;
    private int scaledImageTopFromImageView;
    private int imageScaledHeightInImageView;
    private CheckBox noCropCheckbox;

    public enum PrintType {

        DONT_CROP, CROP_HEIGHT_FIT_WIDTH, CROP_HEIGHT_NO_FIT_WIDTH
    }
    private PrintType pictureType;

    private ImagePane() {
        this.contrast = 0;
        this.brightness = 0;
        this.quantity = 1;
        rectangleTop = rectangleInitialTop;
        addImagePane();
        addContrastControls();
        addBrightnessControls();
        addMovementControls();
        addPrintControls();
        addBackButton();
    }

    public void init(ExplorerPane backPanel, final File imageFile) {
        this.backPanel = backPanel;
        this.contrast = 0;
        this.brightness = 0;
        this.quantity = 1;
        rectangleTop = rectangleInitialTop;
        rect.setLayoutY(rectangleTop + 10);
        updateContrastLabel();
        updateBrightnessLabel();
        updateQuantityLabel();
        noCropCheckbox.setSelected(false);

        try {
            baseBuffImage = ImageIO.read(imageFile);
            baseBuffImage = Scalr.resize(baseBuffImage, Scalr.Method.BALANCED, printImageWidth, printImageWidth);
            if (baseBuffImage.getHeight() > baseBuffImage.getWidth()) {
                baseBuffImage = Scalr.rotate(baseBuffImage, Scalr.Rotation.CW_90, (BufferedImageOp) null);
            }
            noRectMove = false;
            printFullImageBtnVisible = false;
            minRectVerticalMargin = 0;
            if (((double) baseBuffImage.getHeight()) / baseBuffImage.getWidth() <= ((double) printViewHeight) / printViewWidth) {
                //means width is fit to image view and height is less that rectangle height
                pictureType = PrintType.DONT_CROP;
                noRectMove = true;
            } else {
                printFullImageBtnVisible = true;

                if (((double) baseBuffImage.getHeight()) / baseBuffImage.getWidth() <= ((double) imageViewHeight) / imageViewWidth) {
                    //means width is fit to image view height is more that rectangle
                    pictureType = PrintType.CROP_HEIGHT_FIT_WIDTH;
                    imageScaledHeightInImageView = (int) (imageViewWidth * baseBuffImage.getHeight() / ((double) baseBuffImage.getWidth()));
                    scaledImageTopFromImageView = (imageViewHeight - imageScaledHeightInImageView) / 2;
                    minRectVerticalMargin = scaledImageTopFromImageView;
                } else {
                    pictureType = PrintType.CROP_HEIGHT_NO_FIT_WIDTH;
                    //means height is fit to image view
                    minRectVerticalMargin = 0;

                }

            }
            WritableImage toFXImage = SwingFXUtils.toFXImage(baseBuffImage, null);
            imageView.setImage(toFXImage);
            dontCropContainer.setVisible(printFullImageBtnVisible);
        } catch (IOException ex) {
            Logger.getLogger(ImagePane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static javafx.scene.image.Image convertToJavaFXImage(java.awt.Image image){
        try {
            if (!(image instanceof RenderedImage)) {
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                        image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                image = bufferedImage;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) image, "png", out);
            out.flush();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            Image image1 = new Image(in);
            
            return image1;
        } catch (IOException ex) {
            Logger.getLogger(ImagePane.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
  }

    private void addImagePane() {

        BorderPane borderPane = new BorderPane();
        borderPane.setLayoutX(10);
        borderPane.setLayoutY(10);
        borderPane.setMaxSize(imageViewWidth, imageViewHeight);
        borderPane.setMinSize(imageViewWidth, imageViewHeight);
        borderPane.setStyle("-fx-background-color: white");

        rect = new Rectangle(printViewWidth, printViewHeight);
        rect.setLayoutX(10);
        rect.setLayoutY(rectangleTop + 10);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(2);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.setFill(Color.TRANSPARENT);
        imageView = new ImageView();

        imageView.setFitWidth(imageViewWidth);
        imageView.setFitHeight(imageViewHeight);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        //imageView.setCache(true);

        borderPane.setCenter(imageView);
        this.getChildren().add(borderPane);
        this.getChildren().add(rect);

    }

    private void addBrightness() {

        this.brightness++;
        if (this.brightness > 10) {
            this.brightness = 10;
        }
        updateBrightnessLabel();
        updateImageBrightnessContrast();
    }

    private void addContrast() {

        this.contrast++;
        if (this.contrast > 10) {
            this.contrast = 10;
        }
        updateContrastLabel();
        updateImageBrightnessContrast();

    }

    private void updateImageBrightnessContrast() {
        RescaleOp rescaleOp = new RescaleOp(1 + contrast / 10f, brightness * 10, null);
        BufferedImage bi = new BufferedImage(baseBuffImage.getWidth(), baseBuffImage.getHeight(), baseBuffImage.getType());
        rescaleOp.filter(baseBuffImage, bi);
        WritableImage toFXImage = SwingFXUtils.toFXImage(bi, null);
        imageView.setImage(toFXImage);
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
        updateImageBrightnessContrast();
    }

    private void subtractBrightness() {
        this.brightness--;
        if (this.brightness < -10) {
            this.brightness = -10;
        }
        updateBrightnessLabel();
        updateImageBrightnessContrast();
    }

    private void setRectUp() {
        if (noRectMove) {
            return;
        }
        rectangleTop -= 10;
        if (rectangleTop < minRectVerticalMargin) {
            rectangleTop = minRectVerticalMargin;
        }
        rect.setLayoutY(rectangleTop + 10);
    }

    private void resetRect() {
        rectangleTop = rectangleInitialTop;
        rect.setLayoutY(rectangleTop + 10);
    }

    private void setRectDown() {
        if (noRectMove) {
            return;
        }
        rectangleTop += 10;
        if (rectangleTop > 60 - minRectVerticalMargin) {
            rectangleTop = 60 - minRectVerticalMargin;
        }
        rect.setLayoutY(rectangleTop + 10);
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

                setRectUp();
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
                setRectDown();

            }
        });

        dontCropContainer = new VBox();
        noCropCheckbox = new CheckBox();
        BorderPane bp = new BorderPane(noCropCheckbox);
        dontCropContainer.setLayoutX(700);
        dontCropContainer.setLayoutY(190);
        noCropCheckbox.getStyleClass().add("check-box");
        Label text = new Label("Don't Crop");
        text.setTextAlignment(TextAlignment.CENTER);
        dontCropContainer.getChildren().addAll(bp, text);
        dontCropContainer.setSpacing(5);
        this.getChildren().add(dontCropContainer);
        noCropCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (noCropCheckbox.isSelected()) {
                    imageView.setFitWidth(printViewWidth);
                    imageView.setFitHeight(printViewHeight);
                    noRectMove = true;
                    resetRect();

                } else {
                    imageView.setFitWidth(imageViewWidth);
                    imageView.setFitHeight(imageViewHeight);
                    noRectMove = false;
                    resetRect();
                }
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
        ImagePane imagePanel = this;
        printBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Global.getInstance().setSceneRoot(LoadingPane.getInstance());

                Platform.runLater(() -> {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            BufferedImage preparePictureToPrint = null;
                            if (pictureType == PrintType.DONT_CROP || noRectMove) {
                                //mean print without cropping (whole picture)
                                preparePictureToPrint = ImageManager.preparePictureToPrint(baseBuffImage, 0, PrintType.DONT_CROP);
                            } else {
                                if (pictureType == PrintType.CROP_HEIGHT_FIT_WIDTH) {
                                    int originalImageHeight = baseBuffImage.getHeight();
                                    int delta = rectangleTop - scaledImageTopFromImageView;
                                    int pixelFromTopToCrop = delta * originalImageHeight / imageScaledHeightInImageView;
                                    preparePictureToPrint = ImageManager.preparePictureToPrint(baseBuffImage, pixelFromTopToCrop, PrintType.CROP_HEIGHT_FIT_WIDTH);
                                }
                                if (pictureType == PrintType.CROP_HEIGHT_NO_FIT_WIDTH) {
                                    int originalImageHeight = baseBuffImage.getHeight();
                                    int pixelFromTopToCrop = rectangleTop * originalImageHeight / imageViewHeight;
                                    System.err.println(pixelFromTopToCrop);
                                    preparePictureToPrint = ImageManager.preparePictureToPrint(baseBuffImage, pixelFromTopToCrop, PrintType.CROP_HEIGHT_NO_FIT_WIDTH);
                                }
                            }
                            InsertCoinPane.getInstance().init(quantity, preparePictureToPrint, imagePanel);
                            Global.getInstance().setSceneRoot(InsertCoinPane.getInstance());
                        }
                    }).start();
                });

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
                System.gc();
            }
        });
    }

}
