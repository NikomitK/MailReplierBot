package de.nikomitk.mailreplierbot;


import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.Locale;

public class MailServerSettings extends JPanel {

    private final JComboBox<String> mailProvider;
    JTextField imapServer, imapPort, smtpServer, smtpPort;
    EMailProvider[] providerlist;


    public MailServerSettings(Font textfieldfont) throws IOException {
        setLayout(new GridLayout(6, 1, 0, 2));
        providerlist = new EMailProvider[6];
        providerlist[0] = new EMailProvider("OTHER", "Imap server", "Imap port", "Smtp server", "Smtp port");
        providerlist[1] = new EMailProvider("GMAIL", "imap.gmail.com", "993", "smtp.gmail.com", "25");
        providerlist[2] = new EMailProvider("WEB", "imap.web.de", "993", "smtp.web.de", "25");
        providerlist[3] = new EMailProvider("GMX", "imap.gmx.net", "993", "mail.gmx.net", "25");
        providerlist[4] = new EMailProvider("ICLOUD", "imap.mail.me.com", "993", "smtp.mail.me.com", "25");
        providerlist[5] = new EMailProvider("T-ONLINE", "secureimap.t-online.de", "993", "securesmtp.t-online.de", "25");

        // DropDown menu
        mailProvider = new JComboBox(new String[]{providerlist[0].getName(), providerlist[1].getName(), providerlist[2].getName(), providerlist[3].getName(), providerlist[4].getName(), providerlist[5].getName()});
        mailProvider.setSelectedIndex(Main.storage.getPreselect());
        mailProvider.setFont(textfieldfont);
        mailProvider.setFocusable(false);
        mailProvider.addActionListener(e -> setProviderData());
        add(mailProvider);

        EMailProvider provider = Main.storage.getMailProvider();

        // TextFields
        imapServer = new JTextField(provider.getImapserver());
        imapServer.setFont(textfieldfont);
        imapServer.setToolTipText("Example \"imap.gmail.com\"");
        imapServer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (imapServer.getText().equals("Imap server"))
                    imapServer.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (imapServer.getText().equals("")) {
                    imapServer.setText("Imap server");
                }
            }
        });
        add(imapServer);

        imapPort = new JTextField(provider.getImapport());
        imapPort.setFont(textfieldfont);
        imapPort.setToolTipText("Example \"993\"");
        imapPort.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (imapPort.getText().equals("Imap port"))
                    imapPort.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (imapPort.getText().equals("")) {
                    imapPort.setText("Imap port");
                }
            }
        });
        add(imapPort);

        smtpServer = new JTextField(provider.getSmtpserver());
        smtpServer.setFont(textfieldfont);
        smtpServer.setToolTipText("Example \"smtp.gmail.com\"");
        smtpServer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (smtpServer.getText().equals("Smtp server"))
                    smtpServer.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (smtpServer.getText().equals("")) {
                    smtpServer.setText("Smtp server");
                }
            }
        });
        add(smtpServer);

        smtpPort = new JTextField(provider.getSmtpport());
        smtpPort.setFont(textfieldfont);
        smtpPort.setToolTipText("Example \"20\"");
        smtpPort.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (smtpPort.getText().equals("Smtp port"))
                    smtpPort.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (smtpPort.getText().equals("")) {
                    smtpPort.setText("Smtp port");
                }
            }
        });
        add(smtpPort);

        // JButton
        JButton save;
        save = new JButton("Save!");
        save.setFont(textfieldfont);
        save.setFocusable(false);
        save.addActionListener(e -> {
            if (imapServer.getText().equals("Imap server") || imapPort.getText().equals("Imap port") ||
                    smtpServer.getText().equals("Smtp server") || smtpPort.getText().equals("Smtp port")) {
                JOptionPane.showMessageDialog(null, "You need to configure the email server data in order to use the programm");
            }
            provider.setImapserver(imapServer.getText());
            provider.setImapport(imapPort.getText());
            provider.setSmtpserver(smtpServer.getText());
            provider.setSmtpport(smtpPort.getText());
            Main.storage.setMailProvider(provider);
            Main.storage.setPreselect(mailProvider.getSelectedIndex());
            Main.storage.save();
        });
        add(save);

    }

    /**
     * This method gets called to check if the mail adress contains an existing provider to set this automatically
      */
    public void checkProvider() {
        int setSelectedProvider;
        if (Main.storage.getYourmail().toLowerCase(Locale.ROOT).contains("gmail")) setSelectedProvider = 1;
        else if (Main.storage.getYourmail().toLowerCase(Locale.ROOT).contains("web")) setSelectedProvider = 2;
        else if (Main.storage.getYourmail().toLowerCase(Locale.ROOT).contains("gmx")) setSelectedProvider = 3;
        else if (Main.storage.getYourmail().toLowerCase(Locale.ROOT).contains("icloud")) setSelectedProvider = 4;
        else if (Main.storage.getYourmail().toLowerCase(Locale.ROOT).contains("t-online")) setSelectedProvider = 5;
        else setSelectedProvider = 0;
        mailProvider.setSelectedIndex(setSelectedProvider);
        setProviderData();
    }

    private void setProviderData() {
        imapServer.setText(providerlist[mailProvider.getSelectedIndex()].getImapserver());
        imapPort.setText(providerlist[mailProvider.getSelectedIndex()].getImapport());
        smtpServer.setText(providerlist[mailProvider.getSelectedIndex()].getSmtpserver());
        smtpPort.setText(providerlist[mailProvider.getSelectedIndex()].getSmtpport());
    }
}
