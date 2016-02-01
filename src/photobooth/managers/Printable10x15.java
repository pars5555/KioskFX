/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

/**
 *
 * @author Pars
 */
public class Printable10x15 implements Printable {

    private final BufferedImage image;

    public Printable10x15(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }      
        g.drawImage(image, 0, 0, (int) 1800, (int) 1200, null);
        return Printable.PAGE_EXISTS;
    }

}
