package de.nikomitk.mailreplierbot;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ApplicationSettings extends JPanel {

    public ApplicationSettings(boolean darculaTheme) {


        setLayout(new FlowLayout(FlowLayout.LEFT));
        JCheckBox darculaMode = new JCheckBox("Darcula Theme");
        darculaMode.setSelected(darculaTheme);
        darculaMode.addActionListener(e -> {
            if (darculaMode.isSelected()) {
                try {
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    System.out.println("Darcula set");
                } catch (Exception ex) {
                    System.out.println("Darcula not set");
                }
            } else {

                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    System.out.println("Light set");
                } catch (Exception ex) {
                    System.out.println("Light not set");
                }
                SwingUtilities.updateComponentTreeUI(Main.gui);
            }
            SwingUtilities.updateComponentTreeUI(Main.gui);
            try {
                PrintWriter pw;
                pw = new PrintWriter(new BufferedWriter(new FileWriter("data/replierConf.conf", false)), true);
                pw.println(darculaMode.isSelected());
                pw.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });
        add(darculaMode);
        Main.startupTheme = darculaTheme;
    }
}
