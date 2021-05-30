package de.nikomitk.mailreplierbot;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;

@Setter
@Getter
public class SettingsGui extends JFrame {

    @Getter
    private static final Font TEXTFIELDFONT = new Font("Arial", Font.PLAIN, 16);
    boolean openGui;
    private TrayIcon trayIcon;
    private ReplySettings replySettings;
    private MailServerSettings serverSettings;
    private ApplicationSettings applicationSettings;

    public SettingsGui() throws IOException {
        FlatLightLaf.install();
        setLook();
        setVisible(false);
        if (Main.storage.isMinimizetotray() && SystemTray.isSupported()) {
            addToSystemTray();
        }
        setFrameOptions();

        // Create tabs and add the panels to them
        JTabbedPane tabbedPane = new JTabbedPane();
        replySettings = new ReplySettings(TEXTFIELDFONT);
        tabbedPane.addTab("Reply Settings", null, replySettings, "Settings for the reply");
        serverSettings = new MailServerSettings(TEXTFIELDFONT);
        tabbedPane.addTab("Server Settings", null, serverSettings, "Settings for the mailserver");
        applicationSettings = new ApplicationSettings(TEXTFIELDFONT);
        tabbedPane.addTab("Application Settings", null, applicationSettings, "Settings for this application");
        add(tabbedPane);
        tabbedPane.setSelectedIndex(0);

        pack();
        setSize(500, 600);
        setVisible(true);
        if (Main.storage.isCredsset()) {
            // this minimizes the frame to systemtray if all data is set
            setExtendedState(Frame.ICONIFIED);
        }
    }

    private void setLook() {
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
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void addToSystemTray() {
        try {
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
            trayIcon = new TrayIcon(ImageIO.read(new FileInputStream("pics/MailReplierLogo.png")), "Mail Reply Bot", popup);
            trayIcon.setImageAutoSize(true);
            addWindowStateListener(e -> {
                if (e.getNewState() == ICONIFIED || e.getNewState() == WindowEvent.WINDOW_CLOSING || e.getNewState() == 7) {
                    addTrayIcon();
                } else if (e.getNewState() == MAXIMIZED_BOTH || e.getNewState() == NORMAL) {
                    SystemTray.getSystemTray().remove(trayIcon);
                    setVisible(true);
                    pack();
                    setSize(500, 600);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTrayIcon(){
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
            setVisible(false);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
    }

    public void removeFromSystemTray() {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
    }

    public void setFrameOptions() {
        setTitle("Mail-Replier Settings");
        try {
            setIconImage(ImageIO.read(new FileInputStream("pics/MailReplierLogo.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Main.storage.isMinimizetotray()) {
            setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            setResizable(false);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    setExtendedState(ICONIFIED);
                }
            });
        } else {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }


}
