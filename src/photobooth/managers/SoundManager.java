/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import photobooth.Global;
import java.io.IOException;

/**
 *
 * @author default
 */
public class SoundManager {

    public static void stopOmxPlayer() {
        try {
            Runtime.getRuntime().exec("pkill -f omxplayer");
        } catch (IOException ex) {
        }
    }

    public static void startOmxPlayer(String file) {
        stopOmxPlayer();
        String root = Global.getInstance().getJarDir() + "/";
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] cmd = {
                    "/bin/sh",
                    "-c",
                    "omxplayer -o local " + root + file
                };
                Process process;
                try {
                    process = Runtime.getRuntime().exec(cmd);
                    process.waitFor();
                } catch (IOException | InterruptedException ex) {
                }
            }
        });
        t.start();
    }

}
