package de.nikomitk.mailreplierbot;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@Setter
@Getter
public class SettingsGui extends JFrame {

    private final JTextField yourMail;
    private final JTextField senderMail;
    private final JPasswordField yourPassword;
    private final JTextArea yourReply;
    private final JTextArea replyTo;
    private final String[] serverCreds = new String[4];
    private final JButton save;
    private MailServerSettings serverSettings;
    private final Font textfieldFont;
    boolean openGui;
    private int searchDelay = 10;


    public SettingsGui() throws IOException {
    textfieldFont = new Font("Arial", Font.PLAIN, 16);
        FlatLightLaf.install();
        if (Main.storage.isDarktheme()) {
            try {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        } else {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }
        SwingUtilities.updateComponentTreeUI(this);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        SwingUtilities.updateComponentTreeUI(this);
        pack();
        setSize(500, 600);
        final JPanel everyThing;
        final JPanel left;
        final JPanel right;
        final JPanel lTop;
        final JPanel lBottom;
        JScrollPane replyPane;
        JScrollPane areaPane;
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
            TrayIcon trayIcon;
            try {
                trayIcon = new TrayIcon(ImageIO.read(new FileInputStream("pics/MailReplierLogo.png")), "Mail Reply Bot", popup);
            } catch (Exception e) {
                trayIcon = new TrayIcon(ImageIO.read(new URL("https://download1595.mediafire.com/d5q9y10gku3g/7cyat6wbcszlvy5/MailReplierLogo.png")), "Mail Reply Bot", popup);
            }
            trayIcon.setImageAutoSize(true);
            TrayIcon finalTrayIcon = trayIcon;
            addWindowStateListener(e -> {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(finalTrayIcon);
                        setVisible(false);
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                } else if (e.getNewState() == WindowEvent.WINDOW_CLOSING) {
                    try {
                        tray.add(finalTrayIcon);
                        setVisible(false);
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                } else if (e.getNewState() == 7) {
                    try {
                        tray.add(finalTrayIcon);
                        setVisible(false);
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                } else if (e.getNewState() == MAXIMIZED_BOTH || e.getNewState() == NORMAL) {
                    tray.remove(finalTrayIcon);
                    setVisible(true);
                    pack();
                    setSize(500, 600);
                }
            });
        }
        setTitle("Mail-Replier Settings");
        setIconImage(ImageIO.read(new FileInputStream("pics/MailReplierLogo.png")));
        setDefaultCloseOperation(ICONIFIED);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExtendedState(ICONIFIED);
            }
        });

        // JPanelception
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
        yourMail.setFont(textfieldFont);
        yourMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yourMail.getText().equals("Your Mail address"))
                    yourMail.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (yourMail.getText().equals("")) {
                    yourMail.setText("Your Mail address");
                }
            }
        });
        lTop.add(yourMail);
        yourPassword = new JPasswordField();
        yourPassword.setFont(textfieldFont);
        yourPassword.setText(Main.storage.getPassword());
        if (Main.storage.getPassword().equals("Your Password")) {
            yourPassword.setEchoChar((char) 0);
        }
        yourPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(yourPassword.getPassword()).equals("Your Password")) {
                    yourPassword.setEchoChar('*');
                    yourPassword.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(yourPassword.getPassword()).equals("")) {
                    yourPassword.setText("Your Password");
                    yourPassword.setEchoChar((char) 0);
                }
            }
        });
        lTop.add(yourPassword);
        senderMail = new JTextField(Main.storage.getSendermail());
        senderMail.setFont(textfieldFont);
        senderMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (senderMail.getText().equals("Who you want to reply to"))
                    senderMail.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (senderMail.getText().equals("")) {
                    senderMail.setText("Who you want to reply to");
                }
            }
        });
        lTop.add(senderMail);

        // TextArea stuff
        replyTo = new JTextArea(Main.storage.getTrigger());
        replyTo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (replyTo.getText().equals("Text to reply to"))
                    replyTo.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (replyTo.getText().equals("")) {
                    replyTo.setText("Text to reply to");
                }
            }
        });
        replyTo.setColumns(50);
        replyTo.setRows(50);
        replyTo.setFont(textfieldFont);
        yourReply = new JTextArea(Main.storage.getYourreply());
        yourReply.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yourReply.getText().equals("Your reply"))
                    yourReply.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (yourReply.getText().equals("")) {
                    yourReply.setText("Your reply");
                }
            }
        });
        yourReply.setColumns(5);
        yourReply.setRows(5);
        yourReply.setFont(textfieldFont);
        areaPane = new JScrollPane(replyTo);
        right.add(areaPane, BorderLayout.PAGE_START);
        replyPane = new JScrollPane(yourReply);
        lBottom.add(replyPane, BorderLayout.PAGE_START);
        save = new JButton("Save!");
        save.setFont(textfieldFont);
        save.setFocusable(false);
        save.addActionListener(new Main());
        lTop.add(save);
        pack();
        setSize(500, 600);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Reply Settings", null, everyThing, "Settings for the reply");
        serverSettings = new MailServerSettings();
        tabbedPane.addTab("Server Settings", null, serverSettings, "Settings for the mailserver");
        tabbedPane.addTab("Application Settings", null, new ApplicationSettings(), "Settings for this application");
        add(tabbedPane);
        tabbedPane.setSelectedIndex(0);
        pack();
        setSize(500, 600);
        setVisible(true);
    }


    public String getYourMail() {
        if (yourMail.getText().equals("Your Mail address") || yourMail.getText().equals("") || !yourMail.getText().contains("@")) {
            return "false";
        } else return yourMail.getText();
    }

    public String getYourPassword() {
        if (new String(yourPassword.getPassword()).length() == 0 && new String(yourPassword.getPassword()).equals("Your Password"))
            return "false";
        else return new String(yourPassword.getPassword());
    }

    public String getSenderMail() {
        if (yourMail.getText().equals("Who you want to reply to") ||
                yourMail.getText().equals("") ||
                !yourMail.getText().contains("@"))
            return "Who you want to reply to";
        else return senderMail.getText();
    }

    public String getYourReply() {
        if (yourReply.getText().equals("Your reply") || yourReply.getText().equals("")) return "false";
        else return yourReply.getText();
    }

    public String getTrigger() {
        if (replyTo.getText().equals("Text to reply to") || replyTo.getText().equals("")) return "false";
        else return replyTo.getText();
    }

}
