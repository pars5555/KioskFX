/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;

/**
 *
 * @author default
 */
public class EmailManager {

    private static EmailManager instance = null;
    private Folder inbox;

    public EmailManager() {
    }

    public static EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance;
    }

    public List<BufferedImage> getEmailImages(Date d) {
        try {
            System.out.println("Inside MailReader()...");
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

            /* Set the mail properties */
            Properties props = System.getProperties();
            // Set manual Properties
            props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.pop3.socketFactory.fallback", "false");
            props.setProperty("mail.pop3.port", "995");
            props.setProperty("mail.pop3.socketFactory.port", "995");
            props.put("mail.pop3.host", "pop.gmail.com");

            /* Create the session and get the store for read the mail. */
            Session session = Session.getDefaultInstance(System.getProperties(), null);

            Store store = session.getStore("pop3");

            store.connect("pop.gmail.com", 995, "komitas49kiosk@gmail.com", "Barev123");

            /* Mention the folder name which you want to read. */
            // inbox = store.getDefaultFolder();
            // inbox = inbox.getFolder("INBOX");
            inbox = store.getFolder("INBOX");

            /* Open the inbox using store. */
            inbox.open(Folder.READ_ONLY);

            /* Get the messages which is unread in the Inbox */
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            //Message msg = inbox.getMessage(inbox.getMessageCount());
            System.out.println("No. of Unread Messages : " + messages.length);

            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);

            fp.add(FetchProfile.Item.CONTENT_INFO);

            inbox.fetch(messages, fp);
            List<BufferedImage> images = new ArrayList<>();

            for (Message message : messages) {
                message.setFlag(Flag.SEEN, true);
                List<BufferedImage> fetchAttachments = fetchAttachments(message);
                System.err.println("sent:" + message.getSentDate());
                System.err.println("d:" + d);
                if (message.getSentDate().after(d)) {
                    images.addAll(fetchAttachments);
                }
            }
            inbox.close(true);
            store.close();
            return images;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmailManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmailManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

   

    private List<BufferedImage> fetchAttachments(Message message) throws MessagingException, IOException {
        String contentType = message.getContentType();
        List<BufferedImage> ret = new ArrayList<>();
        if (contentType.contains("multipart")) {
            // content may contain attachments
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                String disposition = part.getDisposition();
                String file = part.getFileName();
                //External attachments
                BufferedImage bi = null;
                if (disposition != null && Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
                    // this part is attachment
                    InputStream rawInputStream = part.getInputStream();
                    try {
                        bi = ImageIO.read(rawInputStream);
                    } catch (IOException ex) {
                        Logger.getLogger(EmailManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } //Inline Attachments
                else if (disposition != null && Part.INLINE.equalsIgnoreCase(disposition)) {
                    // this part is attachment
                    InputStream rawInputStream = part.getInputStream();
                    try {
                        bi = ImageIO.read(rawInputStream);
                    } catch (IOException ex) {
                        Logger.getLogger(EmailManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } //Inline icons and smileys
                else if (file != null && disposition == null) {
                    InputStream rawInputStream = part.getInputStream();
                    try {
                        bi = ImageIO.read(rawInputStream);
                    } catch (IOException ex) {
                        Logger.getLogger(EmailManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (bi != null) {
                    ret.add(bi);
                }
            }
        }

        return ret;
    }

}
