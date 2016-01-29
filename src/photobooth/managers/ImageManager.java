/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;
import photobooth.views.ImagePane.PrintType;

/**
 *
 * @author default
 */
abstract public class ImageManager {

    public static void preparePictureToPrint(BufferedImage im, int cropTopOffset, PrintType pt) {

        switch (pt) {
            case DONT_CROP:
               // baseBuffImage = Scalr.resize(baseBuffImage, Scalr.Method.BALANCED, 1800, 1800);
                break;
            case CROP_HEIGHT_FIT_WIDTH:
                break;
            case CROP_HEIGHT_NO_FIT_WIDTH:
                break;
        }

    }
}
