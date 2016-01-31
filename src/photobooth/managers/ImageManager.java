/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
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

    public static BufferedImage preparePictureToPrint(BufferedImage im, int cropTopOffset, PrintType pt) {
        BufferedImage finalBi = null;
        BufferedImage bi = null;
        switch (pt) {
            case DONT_CROP:
                if (im.getHeight() > ImagePane.printImageHeight) {
                    bi = Scalr.resize(im, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, ImagePane.printImageHeight, (BufferedImageOp) null);
                } else {
                    bi = Scalr.resize(im, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, ImagePane.printImageWidth, (BufferedImageOp) null);
                }
                finalBi = createEmptyBufferedImage(ImagePane.printImageWidth, ImagePane.printImageHeight, bi.getType());
                copySrcIntoDstAt(bi, finalBi, (finalBi.getWidth() - bi.getWidth()) / 2, (finalBi.getHeight() - bi.getHeight()) / 2);
                break;
            case CROP_HEIGHT_FIT_WIDTH:
                finalBi = im.getSubimage(0, cropTopOffset, 1800, 1200);
                break;
            case CROP_HEIGHT_NO_FIT_WIDTH:
                int h = ImagePane.printViewHeight * im.getHeight() / ImagePane.imageViewHeight;
                bi = im.getSubimage(0, cropTopOffset, im.getWidth(), h);
                bi = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, ImagePane.printImageHeight, (BufferedImageOp) null);
                finalBi = createEmptyBufferedImage(ImagePane.printImageWidth, ImagePane.printImageHeight, bi.getType());
                copySrcIntoDstAt(bi, finalBi, (finalBi.getWidth() - bi.getWidth()) / 2, (finalBi.getHeight() - bi.getHeight()) / 2);
                break;
        }
        return finalBi;

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
