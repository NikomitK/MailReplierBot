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

    public static void main(String[] args) throws IOException, InterruptedException {
        // move to method
        if (storageFile.exists()) {
            storage = (Storage) YAPIONDeserializer.deserialize(YAPIONParser.parse(storageFile));
        } else {
            storage = new Storage();
        }
        credsset = storage.isCredsset();
        searchdelay = storage.getSearchdelay();

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

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
                Properties properties = new Properties();
                EMailProvider provider = storage.getMailProvider();
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
                            if (!today.equals(dateFormat.format(message.getSentDate()))) {
                                break;
                            }
                            if (!getMailAdress(InternetAddress.toString(message.getFrom())).equals(senderMail)) {
                                continue;
                            }
                            if (lastSubject != null && message.getSubject().contains(lastSubject)) {
                                break;
                            }
                            if (!new MimeMessageParser(message).parse().getPlainContent().contains(triggerText)) {
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

}
