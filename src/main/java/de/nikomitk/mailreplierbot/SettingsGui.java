package de.nikomitk.mailreplierbot;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class SettingsGui extends JFrame{

    public JTextField yourMail, senderMail;
    private JPasswordField yourPassword;
    private JTextArea yourReply, replyTo;
    public JButton submit;
    boolean openGui = true;
    Scanner scc = null;
    private static File credsFile = new File(File.separator + "creds.txt");
    private static File replyFile = new File(File.separator + "reply.txt");
    private static File triggerFile = new File(File.separator + "trigger.txt");

    public SettingsGui() throws IOException {
        final JPanel everyThing, left, right, lTop, lBottom;
        JScrollPane  replyPane, areaPane;
        setVisible(false);
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Settings");
            defaultItem.addActionListener(e -> {
                setVisible(true);
                setAlwaysOnTop(true);
            });
            popup.add(defaultItem);
            defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(e -> System.exit(0));
            popup.add(defaultItem);
            TrayIcon trayIcon = new TrayIcon(ImageIO.read(new FileInputStream("pics/MailReplierLogo.png")), "Mail Reply Bot", popup);
            trayIcon.setImageAutoSize(true);
            addWindowStateListener(e -> {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                }
                if (e.getNewState() == WindowEvent.WINDOW_CLOSING) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            });
        }
        //setVisible(openGui);
        setTitle("Mail-Replier Settings GMAIL EDITION");
        setIconImage(ImageIO.read(new FileInputStream("pics/MailReplierLogo.png")));
        setDefaultCloseOperation(JFrame.ICONIFIED);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExtendedState(JFrame.ICONIFIED);
            }
        });
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
        yourMail = new JTextField("Your Mail address");
        yourMail.setForeground(Color.lightGray);
        yourMail.setFont(new Font("Arial", Font.PLAIN, 16));
        yourMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yourMail.getText().equals("Your Mail address"))
                    yourMail.setText("");
                yourMail.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (yourMail.getText().equals("")) {
                    yourMail.setForeground(Color.lightGray);
                    yourMail.setText("Your Mail address");
                }
            }
        });
        lTop.add(yourMail);
        yourPassword = new JPasswordField();
        yourPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        yourPassword.setText("Your Password");
        yourPassword.setEchoChar((char) 0);
        yourPassword.setForeground(Color.lightGray);
        yourPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(yourPassword.getPassword()).equals("Your Password")) {
                    yourPassword.setForeground(Color.black);
                    yourPassword.setEchoChar('*');
                    yourPassword.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(yourPassword.getPassword()).equals("")) {
                    yourPassword.setText("Your Password");
                    yourPassword.setEchoChar((char) 0);
                    yourPassword.setForeground(Color.lightGray);
                }
            }
        });
        lTop.add(yourPassword);
        senderMail = new JTextField("Who you want to reply to");
        senderMail.setForeground(Color.lightGray);
        senderMail.setFont(new Font("Arial", Font.PLAIN, 16));
        senderMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (senderMail.getText().equals("Who you want to reply to"))
                    senderMail.setText("");
                senderMail.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (senderMail.getText().equals("")) {
                    senderMail.setForeground(Color.lightGray);
                    senderMail.setText("Who you want to reply to");
                }
            }
        });
        lTop.add(senderMail);
        replyTo = new JTextArea("Text to reply to");
        replyTo.setForeground(Color.lightGray);
        replyTo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (replyTo.getText().equals("Text to reply to"))
                    replyTo.setText("");
                replyTo.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (replyTo.getText().equals("")) {
                    replyTo.setForeground(Color.lightGray);
                    replyTo.setText("Text to reply to");
                }
            }
        });
        replyTo.setColumns(50);
        replyTo.setRows(50);
        replyTo.setFont(new Font("Arial", Font.PLAIN, 16));
        yourReply = new JTextArea("Your reply");
        yourReply.setForeground(Color.lightGray);
        yourReply.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yourReply.getText().equals("Your reply"))
                    yourReply.setText("");
                yourReply.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (yourReply.getText().equals("")) {
                    yourReply.setForeground(Color.lightGray);
                    yourReply.setText("Your reply");
                }
            }
        });
        yourReply.setColumns(5);
        yourReply.setRows(5);
        yourReply.setFont(new Font("Arial", Font.PLAIN, 16));
        areaPane = new JScrollPane(replyTo);
        right.add(areaPane, BorderLayout.PAGE_START);
        replyPane = new JScrollPane(yourReply);
        lBottom.add(replyPane, BorderLayout.PAGE_START);
        submit = new JButton("Submit!");
        submit.setFont(new Font("Arial", Font.PLAIN, 16));
        submit.setFocusable(false);
        submit.addActionListener(new Main());
        lTop.add(submit);
        pack();
        setSize(500, 500);
        try{
            scc = new Scanner(new File("creds.txt"));
            yourMail.setText(scc.nextLine() + "");
            yourMail.setForeground(Color.black);
            openGui = false;
        }
        catch (Exception e){
            e.printStackTrace();
            openGui = true;
        }
        try{
            yourPassword.setText(scc.nextLine());
            yourPassword.setForeground(Color.black);
            yourPassword.setEchoChar('*');
            openGui = false;
        }
        catch(Exception e){
            e.printStackTrace();
            openGui = true;
        }
        try{
            String sendErMail = scc.nextLine();
            if(!sendErMail.equals(yourMail.getText())) {
                senderMail.setText(sendErMail);
                senderMail.setForeground(Color.black);
                openGui = false;
            }
            else throw new Exception("No Sender Email");
        }
        catch(Exception e){
            e.printStackTrace();
            senderMail.setText("Who you want to reply to");
            senderMail.setForeground(Color.lightGray);
            openGui = true;
        }
        try{
            Scanner scr = new Scanner(new File("reply.txt"));
            yourReply.setText(scr.nextLine());
            yourReply.setForeground(Color.black);
        }
        catch(Exception e){
            e.printStackTrace();
            openGui = true;
        }
        try{
            Scanner sct = new Scanner(new File("trigger.txt"));
            replyTo.setText(sct.nextLine());
            replyTo.setForeground(Color.black);
        }
        catch(Exception e) {
            e.printStackTrace();
            openGui = true;
        }
        Main.credsSet = !openGui;
        yourMail.grabFocus();
        setVisible(true);
        if(!openGui) setExtendedState(JFrame.ICONIFIED);
    }

    public String getYourMail() {
        if(yourMail.getText().equals("Your Mail address") || yourMail.getText().equals("") || !yourMail.getText().contains("@")){
            //throw new Exception("no mail adress provided");
            return "false";
        }
        else return yourMail.getText();
    }

    public String getYourPassword() {
        if(new String(yourPassword.getPassword()).length() == 0)
        return "false";
        else return new String(yourPassword.getPassword());
    }

    public String getSenderMail(){
        if(yourMail.getText().equals("Who you want to reply to") ||
                yourMail.getText().equals("") ||
                !yourMail.getText().contains("@"))
            return "false";
        else return senderMail.getText();
    }

    public String getYourReply(){
        if(yourReply.getText().equals("Your reply") || yourReply.getText().equals(""))return "false";
        else return yourReply.getText();
    }

    public String getTrigger(){
        if(replyTo.getText().equals("Text to reply to") || replyTo.getText().equals("")) return "false";
        else return replyTo.getText();
    }

    public String getLastSubject(){
        try{
            Scanner scl = new Scanner(new File("lastSubject.txt"));
            return scl.nextLine();
        }
        catch(Exception e){
            e.printStackTrace();
            new File(File.separator + "lastSubject.txt");
            return null;
        }
    }

    public void folderNotExist(){

    }
}
