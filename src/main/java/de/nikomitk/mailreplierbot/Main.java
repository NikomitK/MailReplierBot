package de.nikomitk.mailreplierbot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main implements ActionListener{
    private static File storeFile = new File("\rofl.txt");
    private static SettingsGui gui;
    public static void main(String [] args) throws IOException {
        gui = new SettingsGui();
        System.out.println(gui.testlul);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            SettingsGui hierGui = gui;
            String replierMail = gui.getReplyAdress();
            System.out.println(replierMail);
            if(!replierMail.equals("false")){
                String senderMail = gui.getSenderMail();
                String replierPass = gui.getReplyPass();
                System.out.println(replierPass);
                storeFile.createNewFile();
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("creds.txt", false)), true);
                pw.println(replierMail + "\n" + replierPass + "\n" + senderMail);
                pw.close();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
