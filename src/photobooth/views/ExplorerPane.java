/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import org.apache.commons.io.FilenameUtils;
import photobooth.Global;

/**
 *
 * @author default
 */
public class ExplorerPane extends MyPane {

    private static ExplorerPane instance;

    public static ExplorerPane getInstance() {
        if (instance == null) {
            instance = new ExplorerPane();
        }
        return instance;
    }

    private String dir;
    private int offset;
    private int limit;
    private int directoryLevel;

    private ExplorerPane() {

    }

    public void setDir(String dir) {
        setDir(dir, 0);
    }

    public void setDir(String dir, int offset) {
        setDir(dir, offset, 18, 0);
    }

    public void setDir(String dir, int offset, int limit, int directoryLevel) {
        init(dir, offset, limit, directoryLevel);
    }

    private void init(String dir, int offset, int limit, int directoryLevel) {

        this.getChildren().removeAll(this.getChildren());
        this.dir = dir;
        this.offset = offset;
        this.limit = limit;
        this.directoryLevel = directoryLevel;
        
        try {
            addXButton();
        } catch (IOException ex) {
            Logger.getLogger(ExplorerPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (directoryLevel > 0) {
            try {
                addUpButton();
            } catch (IOException ex) {
                Logger.getLogger(ExplorerPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        addLabel();

        TilePane tile = new TilePane();
        tile.setHgap(12);
        tile.setVgap(12);

        File folder = new File(dir);
        File[] listOfDirs = folder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        File[] allFileAndDiresctoies = new File[0];
        if (listOfDirs != null) {
            Arrays.sort(listOfDirs);

            File[] listOfImages = folder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    String ext = FilenameUtils.getExtension(file.getAbsolutePath());
                    return ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png");
                }
            });
            Arrays.sort(listOfImages);
            allFileAndDiresctoies = new File[listOfDirs.length + listOfImages.length];
            System.arraycopy(listOfDirs, 0, allFileAndDiresctoies, 0, listOfDirs.length);
            System.arraycopy(listOfImages, 0, allFileAndDiresctoies, listOfDirs.length, listOfImages.length);

            File[] subArray = new File[limit];
            int countToCopy = limit;
            if (offset + limit > allFileAndDiresctoies.length) {
                countToCopy -= offset + limit - allFileAndDiresctoies.length;
            }
            System.arraycopy(allFileAndDiresctoies, offset, subArray, 0, countToCopy);

            for (final File file : subArray) {
                if (file == null) {
                    break;
                }
                if (file.isFile()) {
                    tile.getChildren().add(createImageView(file));
                } else {
                    Button button = new Button(file.getName());
                    button.setMaxSize(100, 100);
                    button.setMinSize(100, 100);
                    button.setWrapText(true);
                    button.getStyleClass().add("folderButton");

                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            Global.getInstance().setSceneRoot(LoadingPane.getInstance());

                            Platform.runLater(() -> {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        ExplorerPane.getInstance().setDir(file.getAbsolutePath(), 0, limit, directoryLevel + 1);
                                        Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                                    }
                                }).start();
                            });

                        }
                    });
                    tile.getChildren().add(button);

                }
            }
        }
        this.getChildren().add(tile);
        tile.setMinWidth(670);
        tile.setMaxWidth(670);
        tile.setLayoutX(65);       
        tile.setLayoutY(70);
        tile.setMaxHeight(390);

        if (allFileAndDiresctoies.length > offset + limit) {
            try {
                addNextButton();
            } catch (IOException ex) {
                Logger.getLogger(ExplorerPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (offset > 0) {
            try {
                addPrevButton();
            } catch (IOException ex) {
                Logger.getLogger(ExplorerPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private BorderPane createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing
        ExplorerPane explorerPane = this;
        ImageView imageView = null;
        BorderPane borderPane = new BorderPane();
        try {
            borderPane.setMaxSize(100, 100);
            borderPane.setMinSize(100, 100);
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            final Image image = new Image(fileInputStream, 100, 100, true, true);
            imageView = new ImageView(image);
            fileInputStream.close();
            imageView.getStyleClass().add("image-view");
            borderPane.setCenter(imageView);
            //imageView.setFitWidth(80);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                        if (mouseEvent.getClickCount() == 1) {
                            Global.getInstance().setSceneRoot(LoadingPane.getInstance());

                            Platform.runLater(() -> {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        ImagePane.getInstance().init(explorerPane, imageFile);
                                        Global.getInstance().setSceneRoot(ImagePane.getInstance());
                                    }
                                }).start();
                            });

                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ExplorerPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borderPane;
    }

    private void addXButton() throws IOException {
        Button button = new Button();
        button.setGraphic(new ImageView(new Image(getClass().getResource("/photobooth/images/exit.png").openStream())));
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
                Global.getInstance().setSceneRoot(HomePane.getInstance());
            }
        });
    }

    private void addLabel() {
        Label label = new Label("Select Picture");
        label.setLayoutX(300);
        label.setLayoutY(10);
        label.setFont(new Font(30));
        this.getChildren().add(label);
    }

    private void addUpButton() throws IOException {
         Button button = new Button();
        button.setGraphic(new ImageView(new Image(getClass().getResource("/photobooth/images/up.png").openStream())));
        button.setStyle("-fx-background-color: transparent;");
        button.setMaxSize(50, 50);
        button.setMinSize(50, 50);
        button.setLayoutX(120);
        button.setLayoutY(10);
        this.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Global.getInstance().setSceneRoot(LoadingPane.getInstance());

                Platform.runLater(() -> {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ExplorerPane.getInstance().setDir(new File(dir).getParentFile().getAbsolutePath(), 0, limit, directoryLevel - 1);
                            Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                        }
                    }).start();
                });

            }
        });

    }

    private void addNextButton() throws IOException {
        Button button = new Button();
        button.setGraphic(new ImageView(new Image(getClass().getResource("/photobooth/images/next.png").openStream())));
        button.setStyle("-fx-background-radius: 50%; ");
        button.setStyle("-fx-background-color: transparent;");
        button.setLayoutX(740);
        button.setLayoutY(220);
        button.setMaxSize(50, 50);
        button.setMinSize(50, 50);
        this.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Global.getInstance().setSceneRoot(LoadingPane.getInstance());
                Platform.runLater(() -> {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ExplorerPane.getInstance().setDir(dir, offset + limit, limit, directoryLevel);
                            Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                        }
                    }).start();
                });
            }
        });
    }

    private void addPrevButton() throws IOException {
        Button button = new Button();
        button.setGraphic(new ImageView(new Image(getClass().getResource("/photobooth/images/prev.png").openStream())));
        button.setStyle("-fx-background-radius: 50%; ");
        button.setStyle("-fx-background-color: transparent;");
        button.setLayoutX(10);
        button.setLayoutY(220);
        button.setMaxSize(50, 50);
        button.setMinSize(50, 50);
        this.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Global.getInstance().setSceneRoot(LoadingPane.getInstance());
                Platform.runLater(() -> {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ExplorerPane.getInstance().setDir(dir, offset - limit, limit, directoryLevel);
                            Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                        }
                    }).start();
                });
            }
        });
    }

}
