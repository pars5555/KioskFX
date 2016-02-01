/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrinterResolution;


/**
 *
 * @author default
 */
public class PrintManager {

    private static PrintManager instance = null;

    public PrintManager() {

    }

    public static PrintManager getInstance() {
        if (instance == null) {
            instance = new PrintManager();
        }
        return instance;
    }

    private boolean printLabelWithLabelPrinter(BufferedImage image, PrinterJob job, int qty) {
        try {           
          
            Book book = new Book();
            PageFormat pf = job.defaultPage();
            Paper paper = new Paper();
            double paperWidth = 1800;
            double paperHeight = 1200;
            paper.setSize(paperWidth, paperHeight);
            paper.setImageableArea(0, 0, paperWidth, paperHeight);
            pf.setPaper(paper);

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new Copies(qty));
            pras.add(new PrinterResolution(300, 300, PrinterResolution.DPI));
            pras.add(new MediaPrintableArea(0, 0, 100, 150, MediaPrintableArea.MM));

            book.append(new Printable10x15(image), pf, 1);
            job.setPageable(book);

            job.print(pras);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(PrintManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
