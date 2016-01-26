/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.image.BufferedImage;

/**
 *
 * @author default
 */
public class ImageManager {

    private static ImageManager instance = null;

    public ImageManager() {

    }

    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }
}
