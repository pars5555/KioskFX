/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import org.imgscalr.Scalr;

/**
 *
 * @author default
 */
public class PrintManager {

    public static boolean print(BufferedImage image, int qty) {
        try {
            image = Scalr.rotate(image, Scalr.Rotation.CW_90, (BufferedImageOp) null);
            PrintRequestAttributeSet aset = createAsetForMedia();
            PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job = ps.createPrintJob();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            Doc doc = new SimpleDoc(is, DocFlavor.INPUT_STREAM.JPEG, null);
            job.print(doc, aset);
            is.close();
        } catch (PrintException ex) {
            Logger.getLogger(PrintManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrintManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private static PrintRequestAttributeSet createAsetForMedia() {
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(PrintQuality.NORMAL);
        aset.add(OrientationRequested.PORTRAIT);
        aset.add(new MediaPrintableArea(0, 0, 4, 6,
                MediaPrintableArea.INCH));
        return aset;
    }

}
