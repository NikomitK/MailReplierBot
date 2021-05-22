package de.nikomitk.mailreplierbot;

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

//TODO
// - make delay that is searched for configurable
public class Main implements ActionListener {
    public static final Object lock = new Object();
    public static SettingsGui gui;
    public static boolean credsSet = false;
    public static boolean startupTheme;
    private static final File dataDir = new File("data");
    private static final File credsFile = new File("data" + File.separator + "creds.txt");
    private static final File replyFile = new File("data" + File.separator + "reply.txt");
    private static final File triggerFile = new File("data" + File.separator + "trigger.txt");

    public static void main(String[] args) throws IOException, InterruptedException {
        dataDir.mkdir();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        gui = new SettingsGui();
        gui.startupTheme = startupTheme;
        if (!credsSet) {
            synchronized (lock) {
                lock.wait();
            }
        }
        //mail part
        while (true) {
            String yourMail = gui.getYourMail();
            String yourPassword = gui.getYourPassword();
            String senderMail = gui.getSenderMail();
            String yourReply = gui.getYourReply();
            String triggerText = gui.getTrigger();
            String lastSubject = gui.getLastSubject();
            String today = dateFormat.format(new Date());
            Properties properties = new Properties();
            String[] data = gui.getServerCreds();
            properties.put("mail.imap.host", data[0]);
            properties.put("mail.imap.port", data[1]);
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", data[2]);
            properties.put("mail.smtp.port", data[3]);
            Session session = Session.getDefaultInstance(properties);
            try {
                Store store = session.getStore("imaps");
                store.connect(data[0], yourMail,
                        yourPassword);
                Folder folder = store.getFolder("Inbox");
                folder.open(Folder.READ_ONLY);

                Message[] messages = folder.getMessages();
                if (messages.length != 0) {
                    for (int i = messages.length - 1; i > 0; i--) {
                        Message message = messages[i];
                        System.out.println(dateFormat.format(message.getSentDate()));
                        if (!today.equals(dateFormat.format(message.getSentDate()))) break;
                        if (!getMailAdress(InternetAddress.toString(message.getFrom())).equals(senderMail)) continue;
                        if (message.getSubject().contains(lastSubject)) break;
                        if (!message.getContent().toString().contains(triggerText)) continue;
                        //antwort schreibben
                        System.out.println("gefunden");
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
                            new File("data" + File.separator + "lastSubject.txt").createNewFile();
                            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("data/lastSubject.txt", false)), true);
                            pw.println(message.getSubject());
                            pw.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(6000);
        }
    }

    public static String getMailAdress(String from) {
        for (int i = 0; i < from.length(); i++)
            if (from.charAt(i) == '<') return from.substring(i + 1, from.length() - 1);
        return from;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            String replierMail = gui.getYourMail();
            String senderMail = gui.getSenderMail();
            String replierPass = gui.getYourPassword();
            if (!replierMail.equals("false") || !senderMail.equals("false") || !replierPass.equals("false")) {
                credsFile.createNewFile();
                String printCreds = "";
                if (!replierMail.equals("false")) printCreds += replierMail;
                printCreds += "\n";
                if (!replierPass.equals("false")) printCreds += replierPass;
                printCreds += "\n";
                if (!senderMail.equals("false")) printCreds += senderMail;
                printCreds += "\n";
                PrintWriter pw;
                if (!replierMail.equals("false") && !senderMail.equals("false") && !replierPass.equals("false")) {
                    gui.serverSettings.checkProvider(gui.getYourMail());
                    pw = new PrintWriter(new BufferedWriter(new FileWriter("data/creds.txt", false)), true);
                    pw.println(printCreds);
                    pw.close();
                    synchronized (Main.lock) {
                        Main.lock.notifyAll();
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Incorrect Data", "Incorrect Data", JOptionPane.ERROR_MESSAGE);

                replyFile.createNewFile();
                if (!gui.getYourReply().equals("false")) {
                    pw = new PrintWriter(new BufferedWriter(new FileWriter("data/reply.txt", false)), true);
                    pw.println(gui.getYourReply());
                    pw.close();
                }
                triggerFile.createNewFile();
                if (!gui.getTrigger().equals("false")) {
                    pw = new PrintWriter(new BufferedWriter(new FileWriter("data/trigger.txt", false)), true);
                    pw.println(gui.getTrigger());
                    pw.close();
                }
            } else JOptionPane.showMessageDialog(null, "Incorrect Data");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
