package de.nikomitk.mailreplierbot;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class ApplicationSettings extends JPanel {

    public ApplicationSettings(Font textfieldfont) {
        setLayout(new GridLayout(15, 1));

        // Checkbox to select the theme
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel themeLabel = new JLabel("Darcula Theme");
        JCheckBox themeCheckBox = new JCheckBox();
        themeLabel.setFont(textfieldfont);
        themeCheckBox.setSelected(Main.storage.isDarktheme());
        themeCheckBox.addActionListener(e -> {
            if (themeCheckBox.isSelected()) {
                try {
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {

                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(Main.getGui());
            }
            SwingUtilities.updateComponentTreeUI(Main.getGui());
            Main.storage.setDarktheme(themeCheckBox.isSelected());
            Main.storage.save();

        });
        themeCheckBox.setToolTipText("Select light or dark theme");
        themePanel.add(themeLabel);
        themePanel.add(themeCheckBox);
        add(themePanel);

        // Checkbox to select if frame should be minimized to SystemTray
        JPanel minimizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel minimizeLabel = new JLabel("Minimize to tray");
        JCheckBox minimizeBox = new JCheckBox();
        minimizeLabel.setFont(textfieldfont);
        minimizeBox.setSelected(Main.storage.isMinimizetotray());
        minimizeBox.addActionListener(e -> {
            Main.storage.setMinimizetotray(minimizeBox.isSelected());
            Main.storage.save();
            if (minimizeBox.isSelected()) {
                Main.getGui().setDefaultCloseOperation(Frame.ICONIFIED);
                Main.getGui().addToSystemTray();
                Main.getGui().setFrameOptions();
            } else {
                Main.getGui().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Main.getGui().removeFromSystemTray();
                Main.getGui().setFrameOptions();
            }
        });
        minimizeBox.setToolTipText("Select if the app should close or get minimized to system tray");
        minimizePanel.add(minimizeLabel);
        minimizePanel.add(minimizeBox);
        add(minimizePanel);

        // Spinner to select the time between each check of the mailbox
        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel delayLabel = new JLabel("Search Delay");
        JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(Main.storage.getSearchdelay(), 1, 120, 1));
        delayLabel.setFont(textfieldfont);
        delaySpinner.addChangeListener(e -> {
            Double delay = (Double) delaySpinner.getValue();
            Main.setSearchdelay(delay.intValue() * 60000L);
            Main.storage.setSearchdelay(delay.intValue());
            Main.storage.save();
        });
        delaySpinner.setToolTipText("Sets the time in minutes between each check if a mail arrived");
        delayPanel.add(delayLabel);
        delayPanel.add(delaySpinner);
        add(delayPanel);

    }
}
