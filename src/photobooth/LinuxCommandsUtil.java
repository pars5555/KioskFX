/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.tools.jar.resources.jar;

/**
 *
 * @author default
 */
public class LinuxCommandsUtil {

    public static String getPiToken() {
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStream inputStream = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String s = null;
            while ((s = br.readLine()) != null) {
                if (s.startsWith("Serial")) {
                    return s.substring(s.indexOf(":") + 1).trim();
                }
            }
            return "";
        } catch (IOException ex) {
            return "";
        }
    }

    public static void rebootSystem() {
        try {
            Runtime.getRuntime().exec("init 6");
        } catch (IOException ex) {

        }
    }

    public static boolean setBootJarFile(String fileName) {
        try {
            PrintWriter writer = new PrintWriter("/home/pi/photobooth.sh");
            writer.println("sudo java -jar "+ fileName);
            writer.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LinuxCommandsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
