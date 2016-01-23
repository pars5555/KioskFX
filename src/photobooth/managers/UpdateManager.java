/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import photobooth.Global;
import photobooth.LinuxCommandsUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author default
 */
public class UpdateManager {

    private static UpdateManager instance = null;

    public UpdateManager() {

    }

    public static UpdateManager getInstance() {
        if (instance == null) {
            instance = new UpdateManager();
        }
        return instance;
    }

    public void update(String url, String version) {
        String jarDir = Global.getJarDir();
        try {
            File f = new File(jarDir + "/photobooth_" + version + ".jar");
            FileUtils.copyURLToFile(new URL(url), f);
            if (LinuxCommandsUtil.setBootJarFile(f.getAbsolutePath())) {
                Global.delay(2000);
                LinuxCommandsUtil.rebootSystem();
            }
        } catch (IOException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
