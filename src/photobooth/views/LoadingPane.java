/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author default
 */
public class LoadingPane extends Pane {

    private static LoadingPane instance = null;

    public LoadingPane() {
        try {
            Image m = new Image(getClass().getResource("/photobooth/images/loading.gif").openStream());
            ImageView iv = new ImageView(m);
            BorderPane bp = new BorderPane(iv);
            bp.setMinSize(800, 480);
            bp.setMaxSize(800, 480);
            this.getChildren().add(bp);
        } catch (IOException ex) {
            Logger.getLogger(LoadingPane.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static LoadingPane getInstance() {
        if (instance == null) {
            instance = new LoadingPane();
        }
        return instance;
    }

}
