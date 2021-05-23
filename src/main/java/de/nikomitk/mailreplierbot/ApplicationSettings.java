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

    private final String[] savedSettings;

    public ApplicationSettings(boolean darculaTheme, int searchDelay) {
        Main.searchDelay = searchDelay * 60000L;
        savedSettings = new String[2];
        Arrays.fill(savedSettings, "" + "\n");
        setLayout(new GridLayout(15, 1));

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel themeLabel = new JLabel("Darcula Theme");
        JCheckBox themeCheckBox = new JCheckBox();
        themeCheckBox.setSelected(darculaTheme);
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
            try {
                PrintWriter pw;
                pw = new PrintWriter(new BufferedWriter(new FileWriter("data/replierConf.conf", false)), true);
                savedSettings[0] = themeCheckBox.isSelected() + "\n";
                pw.println(savedSettings[0] + savedSettings[1]);
                pw.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });
        themeCheckBox.setToolTipText("Select light or dark theme");
        themePanel.add(themeLabel);
        themePanel.add(themeCheckBox);
        add(themePanel);
        Main.startupTheme = darculaTheme;

        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel delayLabel = new JLabel("Search Delay");
        JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(searchDelay, 1, 120, 1));
        delaySpinner.addChangeListener(e -> {
            int delay = (int) delaySpinner.getValue();
            Main.searchDelay = delay * 60000;
            savedSettings[1] = delay + "\n";
            try {
                PrintWriter pw;
                pw = new PrintWriter(new BufferedWriter(new FileWriter("data/replierConf.conf", false)), true);
                pw.println(savedSettings[0] + savedSettings[1]);
                pw.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        delaySpinner.setToolTipText("Sets the time in minutes between each check if a mail arrived");
        delayPanel.add(delayLabel);
        delayPanel.add(delaySpinner);
        add(delayPanel);
    }
}
