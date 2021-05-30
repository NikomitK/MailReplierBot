package de.nikomitk.mailreplierbot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ReplySettings extends JPanel {

    private final JTextField yourMail;
    private final JTextField senderMail;
    private final JPasswordField yourPassword;
    private final JTextArea yourReply;
    private final JTextArea replyTo;
    private static final String STDYOURMAIL = "Your Mail address";
    private static final String STDYOURPASSWORD = "Your Password";
    private static final String STDSENDERMAIL = "Who you want to reply to";
    private static final String STDYOURREPLY ="Your reply";
    private static final String STDREPLYTO = "Text to reply to";

    public ReplySettings(Font textfieldfont) {
        setLayout(new GridLayout());
        final JPanel everyThing;
        final JPanel left;
        final JPanel right;
        final JPanel lTop;
        final JPanel lBottom;
        JScrollPane replyPane;
        JScrollPane areaPane;

        // JPanel Stuff
        everyThing = new JPanel(new GridLayout(0, 2));
        add(everyThing);
        left = new JPanel(new GridLayout(2, 0));
        everyThing.add(left);
        right = new JPanel(new GridLayout());
        everyThing.add(right);
        lTop = new JPanel(new GridLayout(4, 0, 0, 2));
        left.add(lTop);
        lBottom = new JPanel(new GridLayout());
        MatteBorder test = BorderFactory.createMatteBorder(5, 0, 0, 0, Color.gray);
        lBottom.setBorder(test);
        left.add(lBottom);

        // LTOP stuff
        yourMail = new JTextField(Main.storage.getYourmail());
        yourMail.setFont(textfieldfont);
        yourMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yourMail.getText().equals(STDYOURMAIL))
                    yourMail.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (yourMail.getText().equals("")) {
                    yourMail.setText(STDYOURMAIL);
                }
            }
        });
        lTop.add(yourMail);

        yourPassword = new JPasswordField();
        yourPassword.setFont(textfieldfont);
        yourPassword.setText(Main.storage.getPassword());
        if (Main.storage.getPassword().equals(STDYOURPASSWORD)) {
            yourPassword.setEchoChar((char) 0);
        }
        yourPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(yourPassword.getPassword()).equals(STDYOURPASSWORD)) {
                    yourPassword.setEchoChar('*');
                    yourPassword.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(yourPassword.getPassword()).equals("")) {
                    yourPassword.setText(STDYOURPASSWORD);
                    yourPassword.setEchoChar((char) 0);
                }
            }
        });
        lTop.add(yourPassword);

        senderMail = new JTextField(Main.storage.getSendermail());
        senderMail.setFont(textfieldfont);
        senderMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (senderMail.getText().equals(STDSENDERMAIL))
                    senderMail.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (senderMail.getText().equals("")) {
                    senderMail.setText(STDSENDERMAIL);
                }
            }
        });
        lTop.add(senderMail);

        JButton save = new JButton("Save!");
        save.setFont(textfieldfont);
        save.setFocusable(false);
        save.addActionListener(e ->{
            Main.storage.setYourmail(getYourMail());
            Main.storage.setSendermail(getSenderMail());
            Main.storage.setPassword(getYourPassword());
            Main.storage.setYourreply(getYourReply());
            Main.storage.setTrigger(getTrigger());
            if (!Main.storage.getYourmail().equals(Main.getFALSE()) && !Main.storage.getPassword().equals(Main.getFALSE()) && !Main.storage.getSendermail().equals(Main.getFALSE())
                    && !Main.storage.getYourreply().equals(Main.getFALSE()) && !Main.storage.getTrigger().equals(Main.getFALSE())) {
                Main.storage.setCredsset(true);
                Main.storage.save();
                Main.getGui().getServerSettings().checkProvider();
                synchronized (Main.lock) {
                    Main.lock.notifyAll();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Data", "Incorrect Data", JOptionPane.ERROR_MESSAGE);
            }
        });
        lTop.add(save);

        // LBottom stuff
        yourReply = new JTextArea(Main.storage.getYourreply());
        yourReply.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yourReply.getText().equals(STDYOURREPLY))
                    yourReply.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (yourReply.getText().equals("")) {
                    yourReply.setText(STDYOURREPLY);
                }
            }
        });
        yourReply.setFont(textfieldfont);
        replyPane = new JScrollPane(yourReply);
        lBottom.add(replyPane, BorderLayout.PAGE_START);

        // Right stuff
        replyTo = new JTextArea(Main.storage.getTrigger());
        replyTo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (replyTo.getText().equals(STDREPLYTO))
                    replyTo.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (replyTo.getText().equals("")) {
                    replyTo.setText(STDREPLYTO);
                }
            }
        });
        replyTo.setFont(textfieldfont);
        areaPane = new JScrollPane(replyTo);
        right.add(areaPane, BorderLayout.PAGE_START);

    }

    public String getYourMail() {
        if (yourMail.getText().equals(STDYOURMAIL) || yourMail.getText().equals("") || !yourMail.getText().contains("@")) {
            return Main.getFALSE();
        } else return yourMail.getText();
    }

    public String getYourPassword() {
        if (new String(yourPassword.getPassword()).length() == 0 && new String(yourPassword.getPassword()).equals(STDYOURPASSWORD))
            return Main.getFALSE();
        else return new String(yourPassword.getPassword());
    }

    public String getSenderMail() {
        if (senderMail.getText().equals(STDSENDERMAIL) || senderMail.getText().equals("") || !senderMail.getText().contains("@"))
            return STDSENDERMAIL;
        else return senderMail.getText();
    }

    public String getYourReply() {
        if (yourReply.getText().equals(STDYOURREPLY) || yourReply.getText().equals(""))
            return Main.getFALSE();
        else return yourReply.getText();
    }

    public String getTrigger() {
        if (replyTo.getText().equals(STDREPLYTO) || replyTo.getText().equals(""))
            return Main.getFALSE();
        else return replyTo.getText();
    }

}
