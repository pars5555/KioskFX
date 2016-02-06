/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;

import photobooth.views.HomePane;
import photobooth.managers.LanguageManager;
import photobooth.views.NotWorkingPane;
import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import photobooth.views.ExplorerPane;
import photobooth.views.LoadingPane;

/**
 *
 * @author default
 */
public class Global {

    private static Global instance;
    private Scene scene;
    private final Stage stage;

    private static final int screenWidth = 800;
    private static final int screenHeight = 480;
    private Parent currentPane = null;
    private final String token;

    private Global(Stage stage) {
        this.stage = stage;
        token = LinuxCommandsUtil.getPiToken();
        HomePane.getInstance();
        NotWorkingPane.getInstance();
    }

    public void start() {

        if (Config.getInstance().getInt("not_working") == 1) {
            currentPane = NotWorkingPane.getInstance();
        } else {
            currentPane = HomePane.getInstance();
        }
        this.scene = new Scene(currentPane, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource("/photobooth/css/style.css").toExternalForm());
        this.stage.setScene(this.scene);
        //this.stage.setFullScreen(true);
        this.stage.show();
        //Core.getInstance().init();
        ServerConnection.getInstance().init();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }
    
    public static int getScreenHeight() {
        return screenHeight;
    }

    public static Global getInstance(Stage stage) {
        if (instance == null) {
            instance = new Global(stage);
        }
        return instance;
    }

    public static void print(String s) {
        if (Config.getInstance().getInt("enable_output") == 1) {
            System.out.println(s);
        }
    }

    public String getToken() {
        return "serialnumber1";
        //return token;
    }

    public static Global getInstance() {
        return instance;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene stage) {
        this.scene = stage;
    }

    public void setSceneRoot(Parent p) {
        if (currentPane != p) {
            currentPane = p;
            Platform.runLater(() -> {
                this.scene.setRoot(p);
            });
        }
    }

    public Parent getCurrentPane() {
        return currentPane;
    }

    public static void delay(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Compute the absolute file path to the jar file. The framework is based on
     * http://stackoverflow.com/a/12733172/1614775 But that gets it right for
     * only one of the four cases.
     *
     * @return A File object for the directory in which the jar file resides.
     * During testing with NetBeans, the result is ./build/classes/, which is
     * the directory containing what will be in the jar.
     */
    public static String getJarDir() {
        File f = null;
        try {
            f = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
        }
        File dir = f.getAbsoluteFile().getParentFile();
        return dir.toString();
    }

    public static String getPhrase(String key) {
        return LanguageManager.getInstance().getPhrase(key);
    }

}
