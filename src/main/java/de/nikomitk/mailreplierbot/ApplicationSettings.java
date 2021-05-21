package de.nikomitk.mailreplierbot;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class ApplicationSettings extends JPanel {

    public ApplicationSettings(){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel everything = new JPanel();
        JPanel emptyPanel = new JPanel();
        JCheckBox darculaMode = new JCheckBox("Darcula Theme");
        darculaMode.setSelected(true);
        darculaMode.addActionListener(e -> {
            if(darculaMode.isSelected()){
                try {
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    System.out.println("Darcula set");
                } catch (Exception ex) {
                    System.out.println("Darcula not set");
                }
            }
            else {

                try{
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    System.out.println("Light set");
                }
                catch (Exception ex) {
                    System.out.println("Light not set");
                }
                SwingUtilities.updateComponentTreeUI(Main.gui);
            }
            SwingUtilities.updateComponentTreeUI(Main.gui);
        });
        //everything.add(darculaMode);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        add(darculaMode);
    }
}
