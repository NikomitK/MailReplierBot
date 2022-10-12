package de.nikomitk.mailreplierbot;

import lombok.Getter;
import lombok.Setter;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.mail.util.MimeMessageParser;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

//TODO change yapion to gson
public class Main {
    public static final Object lock = new Object();
    @Getter
    private static final String FALSE = "false";
    private static final File storageFile = new File("storage.json");
    public static Storage storage;
    @Getter
    @Setter
    private static SettingsGui gui;
    @Setter
    private static boolean credsset;
    @Setter
    @Getter
    private static long searchdelay;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    public static void main(String[] args) throws IOException, InterruptedException {

        initStorage()


        gui = new SettingsGui();
        
        while (!credsset) {
            synchronized (lock) {
                lock.wait();
            }
        }

        //mail part
        while (true) {
            if (credsset) {
                String yourMail = storage.getYourmail();
                String yourPassword = storage.getPassword();
                String senderMail = storage.getSendermail();
                String triggerText = storage.getTrigger();
                String lastSubject = storage.getLastsubject();
                String today = dateFormat.format(new Date());
                EMailProvider provider = storage.getMailProvider();

                Properties properties = new Properties();
                properties.put("mail.imap.host", provider.getImapserver());
                properties.put("mail.imap.port", provider.getImapport());
                properties.put("mail.imap.starttls.enable", "true");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", provider.getSmtpserver());
                properties.put("mail.smtp.port", provider.getSmtpport());

                Session session = Session.getDefaultInstance(properties);

                try {
                    Store store = session.getStore("imaps");
                    store.connect(provider.getImapserver(), yourMail,
                            yourPassword);
                    Folder folder = store.getFolder("Inbox");
                    folder.open(Folder.READ_ONLY);

                    Message[] messages = folder.getMessages();

                    if (messages.length != 0) {
                        for (int i = messages.length - 1; i > 0; i--) {
                            MimeMessage message = (MimeMessage) messages[i];
                            if (dateMatches(message.getSentDate(), today)) {
                                break;
                            }
                            if (addressMatches(message.getFrom(), senderMail)) {
                                continue;
                            }
                            if (subjectMatches(message.getSubject(), lastSubject)) {
                                break;
                            }
                            if (textMatches(message, triggerText)) {
                                continue;
                            }
                            //write reply

                            sendMessage(session, message);
                            storage.save();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Exit condition that shouldn't trigger, but loops should have an exit condition?
                // If you know how to solve this the correct way, please tell me xD
                if (storage.getYourmail().equals("lulsafenicht")) {
                    break;
                }

                // Not entirely sure how to do this correct but not the main focus because it works for now
                try {
                    Thread.sleep(searchdelay * 60000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }
    }

    private static void initStorage(){
        if (storageFile.exists()) {
            storage = (Storage) YAPIONDeserializer.deserialize(YAPIONParser.parse(storageFile));
        } else {
            storage = new Storage();
        }
        credsset = storage.isCredsset();
        searchdelay = storage.getSearchdelay();
    }


    private static void sendMessage(Session session, Message message) throws MessagingException {

        // Define the message that is sent
        Message replyMessage = message.reply(false);
        replyMessage.setFrom(new InternetAddress(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO))));
        replyMessage.setText(Main.storage.getYourreply());
        replyMessage.setReplyTo(message.getReplyTo());

        // Try to send a reply
        try {
            Transport t = session.getTransport("smtp");
            t.connect(Main.storage.getYourmail(), Main.storage.getPassword());
            t.sendMessage(replyMessage, replyMessage.getAllRecipients());
            t.close();
            storage.setLastsubject(message.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method returns "mail-adress" from "name <mail-adress>"
    public static String getMailAdress(String from) {
        for (int i = 0; i < from.length(); i++) {
            if (from.charAt(i) == '<') return from.substring(i + 1, from.length() - 1);
        }
        return from;
    }

    private static boolean dateMatches(Date receivedDate, String today){
        return today.equals(dateFormat.format(receivedDate))
    }

    private static boolean addressMatches(String receivedAdress, String providedAddress) {
        return getMailAdress(InternetAddress.toString(receivedAdress)).equals(providedAddress)
    }

    private static boolean subjectMatches(String receivedSubject, String providedSubject){
        return providedSubject != null && receivedSubject.contains(providedSubject)
    }

    private static boolean textMatches(String receivedMessage, String providedText) {
        String messageText = new MimeMessageParser(message).parse().getPlainContent()
        return messageText.contains(providedText)
    }

}
