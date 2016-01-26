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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import org.apache.commons.io.FilenameUtils;
import photobooth.Global;

/**
 *
 * @author default
 */
public class ExplorerPane extends Pane {

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
        this.setStyle("-fx-background-color: #777;");
        addXButton();
        if (directoryLevel > 0) {
            addUpButton();
        }
        addLabel();

        TilePane tile = new TilePane();
        tile.setPadding(new Insets(10, 10, 10, 10));
        tile.setHgap(10);
        tile.setVgap(10);

        File folder = new File(dir);
        File[] listOfDirs = folder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        Arrays.sort(listOfDirs);

        File[] listOfImages = folder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                String ext = FilenameUtils.getExtension(file.getAbsolutePath());
                return ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png");
            }
        });
        Arrays.sort(listOfImages);
        File[] allFileAndDiresctoies = new File[listOfDirs.length + listOfImages.length];
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
                        ExplorerPane.getInstance().setDir(file.getAbsolutePath(), 0, limit, directoryLevel + 1);
                        Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
                    }
                });
                tile.getChildren().add(button);

            }
        }

        this.getChildren().add(tile);
        tile.setMinWidth(800);
        tile.setMaxWidth(800);
        tile.setLayoutY(50);
        tile.setMaxHeight(380);

        if (allFileAndDiresctoies.length > offset + limit) {
            addNextButton();
        }
        if (offset > 0) {
            addPrevButton();
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
            borderPane.setMaxSize(110, 110);
            borderPane.setMinSize(110, 110);
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            final Image image = new Image(fileInputStream, 110, 110, true, true);
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
                            Global.getInstance().setSceneRoot(new ImagePane(explorerPane, imageFile));

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

    private void addXButton() {
        Button exitButton = new Button("X");
        exitButton.setLayoutX(720);
        exitButton.setLayoutY(10);
        exitButton.getStyleClass().add("blueButton");
        this.getChildren().add(exitButton);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
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

    private void addUpButton() {
        Button button = new Button("^");
        button.setLayoutX(120);
        button.setLayoutY(10);
        button.getStyleClass().add("blueButton");
        this.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.err.println(new File(dir).getParentFile().getAbsolutePath());
                ExplorerPane.getInstance().setDir(new File(dir).getParentFile().getAbsolutePath(), 0, limit, directoryLevel - 1);
                Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
            }
        });

    }

    private void addNextButton() {
        Button button = new Button(">");
        button.setLayoutX(470);
        button.setLayoutY(420);
        button.getStyleClass().add("blueButton");
        this.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ExplorerPane.getInstance().setDir(dir, offset + limit, limit, directoryLevel);
                Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
            }
        });
    }

    private void addPrevButton() {
        Button button = new Button("<");
        button.setLayoutX(270);
        button.setLayoutY(420);
        button.getStyleClass().add("blueButton");
        this.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ExplorerPane.getInstance().setDir(dir, offset - limit, limit, directoryLevel);
                Global.getInstance().setSceneRoot(ExplorerPane.getInstance());
            }
        });
    }

}
