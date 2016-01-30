/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import photobooth.views.ImagePane;
import photobooth.views.ImagePane.PrintType;

/**
 *
 * @author default
 */
abstract public class ImageManager {

    public static void preparePictureToPrint(BufferedImage im, int cropTopOffset, PrintType pt) {

        switch (pt) {
            case DONT_CROP:
                BufferedImage bi = Scalr.resize(im, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, ImagePane.printImageWidth, ImagePane.printImageHeight, null);
                bi = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, ImagePane.printImageWidth, ImagePane.printImageHeight, null);
                BufferedImage finalBi = createEmptyBufferedImage(ImagePane.printImageWidth, ImagePane.printImageHeight, bi.getType());
                copySrcIntoDstAt(bi, finalBi, 100, 0);
                break;
            case CROP_HEIGHT_FIT_WIDTH:
                break;
            case CROP_HEIGHT_NO_FIT_WIDTH:
                break;
        }

    }

    private static BufferedImage createEmptyBufferedImage(int width, int height, int type) {
        BufferedImage ret = new BufferedImage(width, height, type);
        Graphics2D g = ret.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ret.getWidth(), ret.getHeight());
        g.dispose();
        return ret;
    }

    private static void copySrcIntoDstAt(final BufferedImage src,
            final BufferedImage dst, final int dx, final int dy) {
        int[] srcbuf = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
        int[] dstbuf = ((DataBufferInt) dst.getRaster().getDataBuffer()).getData();
        int width = src.getWidth();
        int height = src.getHeight();
        int dstoffs = dx + dy * dst.getWidth();
        int srcoffs = 0;
        for (int y = 0; y < height; y++, dstoffs += dst.getWidth(), srcoffs += width) {
            System.arraycopy(srcbuf, srcoffs, dstbuf, dstoffs, width);
        }
    }
}
