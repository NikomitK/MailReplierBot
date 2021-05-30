package de.nikomitk.mailreplierbot;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class ApplicationSettings extends JPanel {

    public ApplicationSettings() {
        setLayout(new GridLayout(12, 1));

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel themeLabel = new JLabel("Darcula Theme");
        JCheckBox themeCheckBox = new JCheckBox();
        themeCheckBox.setSelected(Main.storage.isDarktheme());
        themeCheckBox.addActionListener(e -> {
            if (themeCheckBox.isSelected()) {
                try {
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                } catch (Exception ex) {
                }
            } else {

                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } catch (Exception ex) {
                }
                SwingUtilities.updateComponentTreeUI(Main.gui);
            }
            SwingUtilities.updateComponentTreeUI(Main.gui);
            Main.storage.setDarktheme(themeCheckBox.isSelected());
            Main.storage.save();

        });
        themeCheckBox.setToolTipText("Select light or dark theme");
        themePanel.add(themeLabel);
        themePanel.add(themeCheckBox);
        add(themePanel);

        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel delayLabel = new JLabel("Search Delay");
        System.out.println(Main.storage.getSearchdelay());
        JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(Main.storage.getSearchdelay(), 1, 120, 1));
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
