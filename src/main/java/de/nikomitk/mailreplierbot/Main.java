package de.nikomitk.mailreplierbot;

import lombok.Getter;
import lombok.Setter;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


@Getter
@Setter
public class Main implements ActionListener {
    public static final Object lock = new Object();
    @Getter
    private static final String FALSE = "false";
    public static Storage storage;
    @Getter
    public static File storageFile;
    public static SettingsGui gui;
    @Setter
    private static boolean credsset;
    @Setter
    @Getter
    private static long searchdelay;

    public static void main(String[] args) throws IOException, InterruptedException {
        storageFile = new File("storage.yapion");
        if (storageFile.exists()) {
            storage = (Storage) YAPIONDeserializer.deserialize(YAPIONParser.parse(storageFile));
        } else {
            storage = new Storage();
        }
        credsset = storage.isCredsset();
        searchdelay = storage.getSearchdelay();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        gui = new SettingsGui();
            while(!credsset) {
                synchronized (lock) {
                    lock.wait();
                }
            }
        //mail part
        while (true) {

            if(credsset){
                System.out.println("mailschleife");
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
                            Message message = messages[i];
                            System.out.println(InternetAddress.toString(message.getFrom()));
                            if (!today.equals(dateFormat.format(message.getSentDate()))){
                                System.out.println("erstes if");
                                break;
                            }
                            if(!(lastSubject == null)){
                                if(message.getSubject().contains(lastSubject)){
                                    System.out.println("zweites if");
                                    break;
                                }
                            }
                            if (!getMailAdress(InternetAddress.toString(message.getFrom())).equals(senderMail)){
                                System.out.println("drittes if");
                                continue;
                            }
                            if(!message.getContent().toString().contains(triggerText)){
                                System.out.println(message.getContent().toString());
                                System.out.println(triggerText);
                                System.out.println("viertes if");
                                continue;
                            }
                            //write reply

                            sendMessage(session, message);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (gui.getYourMail().equals("lulsafenicht")) {
                    break;
                }

                // not entirely sure how to do this correct but not the main focus because it works for now
                try {
                    Thread.sleep(searchdelay * 60000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }
    }

    private static void sendMessage(Session session, Message message) throws MessagingException {
        System.out.println("sendMessage");
        Message replyMessage = message.reply(false);
        replyMessage.setFrom(new InternetAddress(InternetAddress.toString(message
                .getRecipients(Message.RecipientType.TO))));
        replyMessage.setText(gui.getYourReply());
        replyMessage.setReplyTo(message.getReplyTo());
        Transport t = session.getTransport("smtp");
        try {
            t.connect(gui.getYourMail(), gui.getYourPassword());
            t.sendMessage(replyMessage,
                    replyMessage.getAllRecipients());
            t.close();
            storage.setLastsubject(message.getSubject());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMailAdress(String from) {
        for (int i = 0; i < from.length(); i++)
            if (from.charAt(i) == '<') return from.substring(i + 1, from.length() - 1);
        return from;
    }

    // I probably move this to settingsgui, as it doesn't need to be here anymore with the new storage system
    @Override
    public void actionPerformed(ActionEvent e) {

            storage.setYourmail(gui.getYourMail());
            storage.setSendermail(gui.getSenderMail());
            storage.setPassword(gui.getYourPassword());
            storage.setYourreply(gui.getYourReply());
            storage.setTrigger(gui.getTrigger());
            if (!storage.getYourmail().equals(FALSE) && !storage.getPassword().equals(FALSE) && !storage.getSendermail().equals(FALSE)
                    && !storage.getYourreply().equals(FALSE) && !storage.getTrigger().equals(FALSE)) {
                credsset = true;
                storage.setCredsset(true);
                storage.save();
                gui.getServerSettings().checkProvider();
                synchronized (Main.lock) {
                    Main.lock.notifyAll();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Data", "Incorrect Data", JOptionPane.ERROR_MESSAGE);
            }

    }
}
