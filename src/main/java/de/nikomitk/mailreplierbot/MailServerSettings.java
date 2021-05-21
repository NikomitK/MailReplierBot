package de.nikomitk.mailreplierbot;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.Scanner;

public class MailServerSettings extends JPanel{

    private static File serverCredFile = new File("data" + File.separator + "serverCreds.txt");

    public MailServerSettings() throws IOException {
        setLayout(new GridLayout(6,1,0,2));
        String [] providers = { "OTHER", "GMAIL", "WEB", "GMX", "ICLOUD", "T-ONLINE"};
        String [][] providerData = {
                {"Imap server", "Imap port", "Smtp server", "Smtp port"},
                {"imap.gmail.com", "993", "smtp.gmail.com", "25"},
                {"imap.web.de", "993", "smtp.web.de", "25"},
                {"imap.gmx.net", "993", "mail.gmx.net", "25"},
                {"imap.mail.me.com", "993", "smtp.mail.me.com", "25"},
                {"secureimap.t-online.de", "993", "securesmtp.t-online.de", "25"}
        };
        JComboBox mailProvider;
        JTextField imapServer, imapPort, smtpServer, smtpPort;
        JButton save;

        // DropDown stuff#
        mailProvider = new JComboBox(providers);
        mailProvider.setSelectedIndex(0);
        mailProvider.setFont(new Font("Arial", Font.BOLD, 16));
        mailProvider.setFocusable(false);
        add(mailProvider);
        // TextField stuff

        imapServer = new JTextField(providerData[mailProvider.getSelectedIndex()][0]);
        imapServer.setFont(new Font("Arial", Font.PLAIN, 16));
        imapServer.setToolTipText("Example \"imap.gmail.com\"");
        if(mailProvider.getSelectedIndex() == 0) imapServer.setForeground(Color.lightGray);
        imapServer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (imapServer.getText().equals("Imap server"))
                    imapServer.setText("");
                imapServer.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (imapServer.getText().equals("")) {
                    imapServer.setForeground(Color.lightGray);
                    imapServer.setText("Imap server");
                }
            }
        });
        add(imapServer);
        imapPort = new JTextField(providerData[mailProvider.getSelectedIndex()][1]);
        imapPort.setFont(new Font("Arial", Font.PLAIN, 16));
        imapPort.setToolTipText("Example \"993\"");
        if(mailProvider.getSelectedIndex() == 0) imapPort.setForeground(Color.lightGray);
        imapPort.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (imapPort.getText().equals("Imap port"))
                    imapPort.setText("");
                imapPort.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (imapPort.getText().equals("")) {
                    imapPort.setForeground(Color.lightGray);
                    imapPort.setText("Imap port");
                }
            }
        });
        add(imapPort);
        smtpServer = new JTextField(providerData[mailProvider.getSelectedIndex()][2]);
        smtpServer.setFont(new Font("Arial", Font.PLAIN, 16));
        smtpServer.setToolTipText("Example \"smtp.gmail.com\"");
        if(mailProvider.getSelectedIndex() == 0) smtpServer.setForeground(Color.lightGray);
        smtpServer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (smtpServer.getText().equals("Smtp server"))
                    smtpServer.setText("");
                smtpServer.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (smtpServer.getText().equals("")) {
                    smtpServer.setForeground(Color.lightGray);
                    smtpServer.setText("Smtp server");
                }
            }
        });
        add(smtpServer);
        smtpPort = new JTextField(providerData[mailProvider.getSelectedIndex()][3]);
        smtpPort.setFont(new Font("Arial", Font.PLAIN, 16));
        smtpPort.setToolTipText("Example \"20\"");
        if(mailProvider.getSelectedIndex() == 0) smtpPort.setForeground(Color.lightGray);
        smtpPort.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (smtpPort.getText().equals("Smtp port"))
                    smtpPort.setText("");
                smtpPort.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (smtpPort.getText().equals("")) {
                    smtpPort.setForeground(Color.lightGray);
                    smtpPort.setText("Smtp port");
                }
            }
        });
        add(smtpPort);

        String [] data = getStaticServerCreds();
        imapServer.setText(data[0]);
        if (imapServer.getText().equals("")) {
            imapServer.setForeground(Color.lightGray);
            imapServer.setText("Imap server");
        }
        else imapServer.setForeground(Color.black);
        imapPort.setText(data[1]);
        if (imapPort.getText().equals("")) {
            imapPort.setForeground(Color.lightGray);
            imapPort.setText("Imap port");
        }
        else imapPort.setForeground(Color.black);
        smtpServer.setText(data[2]);
        if (smtpServer.getText().equals("")) {
            smtpServer.setForeground(Color.lightGray);
            smtpServer.setText("Smtp server");
        }
        else smtpServer.setForeground(Color.black);
        smtpPort.setText(data[3]);
        if (smtpPort.getText().equals("")) {
            smtpPort.setForeground(Color.lightGray);
            smtpPort.setText("Smtp port");
        }
        else smtpPort.setForeground(Color.black);

        // JButton Stuff
        save = new JButton("Save!");
        save.setFont(new Font("Arial", Font.PLAIN, 16));
        save.setFocusable(false);
        save.addActionListener(e -> {
            if(imapServer.getText().equals(providerData[0][0]) || imapPort.getText().equals(providerData[0][1]) ||
                    smtpServer.getText().equals(providerData[0][2]) || smtpPort.getText().equals(providerData[0][3]) ||
                    imapServer.getText().equals("") || imapPort.getText().equals("") ||
                    smtpServer.getText().equals("") || smtpPort.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Incorrect Data");
            }
            else{
                try {
                    serverCredFile.createNewFile();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new BufferedWriter(new FileWriter("data/serverCreds.txt", false)), true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                pw.println(imapServer.getText() + "\n" + imapPort.getText() + "\n" + smtpServer.getText() + "\n" + smtpPort.getText());
                pw.close();
            }

        });
        add(save);

        mailProvider.addActionListener(e -> {
            imapServer.setText(providerData[mailProvider.getSelectedIndex()][0]);
            if(mailProvider.getSelectedIndex() == 0) imapServer.setForeground(Color.lightGray);
            else imapServer.setForeground(Color.black);
            imapPort.setText(providerData[mailProvider.getSelectedIndex()][1]);
            if(mailProvider.getSelectedIndex() == 0) imapPort.setForeground(Color.lightGray);
            else imapPort.setForeground(Color.black);
            smtpServer.setText(providerData[mailProvider.getSelectedIndex()][2]);
            if(mailProvider.getSelectedIndex() == 0) smtpServer.setForeground(Color.lightGray);
            else smtpServer.setForeground(Color.black);
            smtpPort.setText(providerData[mailProvider.getSelectedIndex()][3]);
            if(mailProvider.getSelectedIndex() == 0) smtpPort.setForeground(Color.lightGray);
            else smtpPort.setForeground(Color.black);
        });
    }

    public static String [] getStaticServerCreds() throws IOException {
        Scanner scs;
        String [] serverCreds = new String[4];
        try{
            scs = new Scanner(serverCredFile);
        }
        catch (Exception e){
            serverCredFile.createNewFile();
            scs = new Scanner(serverCredFile);
        }
        try{
            serverCreds[0] = scs.nextLine();
        }
        catch(Exception e){
            serverCreds[0] = "";
        }
        try {
            serverCreds[1] = scs.nextLine();
        }
        catch(Exception e){
            serverCreds[1] = "";
        }
        try{
            serverCreds[2] = scs.nextLine();
        }
        catch (Exception e){
            serverCreds[2] = "";
        }
        try{
            serverCreds[3] = scs.nextLine();
        }
        catch (Exception e){
            serverCreds[3] = "";
        }
        return serverCreds;
    }
}
