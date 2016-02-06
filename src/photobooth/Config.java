/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.ini4j.Wini;

/**
 *
 * @author Pars
 */
public class Config {

    private static Config instance;

    private Wini ini = null;

    private Config() {
        this.loadIniSettings();

    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    

    private boolean loadIniSettings() {
        String jarDir = Global.getJarDir();
         
        try {
            ini = new Wini(new File(jarDir + "/config.ini"));
        } catch (Exception ex) {

            return false;
        }
        return true;
    }

    public void setValue(String name, String value) {        
        ini.put("main", name, value);
        try {
            ini.store();
            ini.load();
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getInt(String name) {
        return ini.get("main", name, int.class);
    }

    public String getString(String name) {
        return ini.get("main", name, String.class);
    }

    public Double getDouble(String name) {
        return ini.get("main", name, double.class);
    }

}
