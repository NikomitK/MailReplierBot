package de.nikomitk.mailreplierbot;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Main implements ActionListener{
    private static File credsFile = new File(File.separator + "creds.txt");
    private static File replyFile = new File(File.separator + "reply.txt");
    private static File triggerFile = new File(File.separator + "trigger.txt");
    private static SettingsGui gui;
    private static String senderMail, yourMail, yourPassword, yourReply, triggerText, lastSubject;
    public static boolean credsSet = false;
    public static final Object lock = new Object();
    public static void main(String [] args) throws IOException, InterruptedException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        gui = new SettingsGui();
        if(!credsSet){
            synchronized (lock) {
                lock.wait();
            }
        }
        //mail part
        while(true){
            yourMail = gui.getYourMail();
            yourPassword = gui.getYourPassword();
            senderMail = gui.getSenderMail();
            yourReply = gui.getYourReply();
            triggerText = gui.getTrigger();
            lastSubject = gui.getLastSubject();
            String heute = dateFormat.format(new Date());
            Properties properties = new Properties();
            //properties.put("mail.store.protocol", "imap");
            properties.put("mail.imap.host", "imap.gmail.com");
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "25");
            Session session = Session.getDefaultInstance(properties);
            try {
                Store store = session.getStore("imaps");
                store.connect("imap.gmail.com", yourMail,
                        yourPassword);
                Folder folder = store.getFolder("Inbox");
                if (!folder.exists()) gui.folderNotExist();
                folder.open(Folder.READ_ONLY);

                Message[] messages = folder.getMessages();
                if (messages.length != 0) {
                    for (int i = messages.length - 1; i > 0; i--) {
                        Message message = messages[i];
                        if (!heute.equals(dateFormat.format(message.getSentDate()))) continue;
                        if (!getMailAdress(InternetAddress.toString(message.getFrom())).equals(senderMail)) continue;
                        if (message.getSubject().equals(lastSubject)) break;
                        if (!message.getContent().toString().contains(triggerText)) continue;
                        //antwort schreibben
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
                            new File(File.separator + "lastSubject.txt").createNewFile();
                            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("lastSubject.txt", false)), true);
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
            Thread.sleep(600000);
        }
    }

//    private static boolean replyToMailIfAvailable(){
//
//    }

    public static String getMailAdress(String from){
        for(int i = 0; i<from.length(); i++)if(from.charAt(i) == '<') return from.substring(i+1, from.length()-1);
        return from;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try{
            String replierMail = gui.getYourMail();
            String senderMail = gui.getSenderMail();
            String replierPass = gui.getYourPassword();
            if(!replierMail.equals("false") || !senderMail.equals("false") || !replierPass.equals("false")){
                credsFile.createNewFile();
                String printCreds = "";
                if(!replierMail.equals("false")) printCreds += replierMail;
                printCreds += "\n";
                if(!replierPass.equals("false")) printCreds += replierPass;
                printCreds += "\n";
                if(!senderMail.equals("false")) printCreds += senderMail;
                printCreds += "\n";
                if(!replierMail.equals("false") && !senderMail.equals("false") && !replierPass.equals("false")) {
                    synchronized (Main.lock){
                        Main.lock.notifyAll();
                    }
                }
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("creds.txt", false)), true);
                pw.println(printCreds);
                pw.close();
                replyFile.createNewFile();
                pw = new PrintWriter(new BufferedWriter(new FileWriter("reply.txt", false)), true);
                pw.println(gui.getYourReply());
                pw.close();
                triggerFile.createNewFile();
                pw = new PrintWriter(new BufferedWriter(new FileWriter("trigger.txt", false)), true);
                pw.println(gui.getTrigger());
                pw.close();
            }
            else JOptionPane.showMessageDialog(null, "Falsche daten");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
