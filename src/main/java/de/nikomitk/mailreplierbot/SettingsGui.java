package de.nikomitk.mailreplierbot;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

public class SettingsGui extends JFrame{

    private JPanel everyThing, left, right, lTop, lBottom;
    private JScrollPane  replyPane, areaPane;
    public JTextField yourMail, senderMail;
    private JPasswordField mailPassword;
    private JTextArea reply, replyTo;
    public JButton submit;
    public String testlul = "koot";

    public SettingsGui(){
        setVisible(true);
        setTitle("Mail-Replier Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        everyThing = new JPanel(new GridLayout(0, 2));
        add(everyThing);
        left = new JPanel(new GridLayout(2, 0));
        everyThing.add(left);
        right = new JPanel(new GridLayout());
        everyThing.add(right);
        lTop = new JPanel(new GridLayout(4,0, 0,2));
        left.add(lTop);
        lBottom = new JPanel(new GridLayout());
        MatteBorder test = BorderFactory.createMatteBorder(5,0,0,0, Color.gray);
        lBottom.setBorder(test);
        left.add(lBottom);
        yourMail = new JTextField("Your Mail address");
        yourMail.setForeground(Color.lightGray);
        yourMail.setFont(new Font("Arial", Font.PLAIN, 16));
        yourMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(yourMail.getText().equals("Your Mail address"))
                    yourMail.setText("");
                    yourMail.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(yourMail.getText().equals("")) {
                    yourMail.setForeground(Color.lightGray);
                    yourMail.setText("Your Mail address");
                }
            }
        });
        lTop.add(yourMail);
        mailPassword = new JPasswordField();
        mailPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        mailPassword.setText("Your Password");
        mailPassword.setEchoChar((char) 0);
        mailPassword.setForeground(Color.lightGray);
        mailPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("lul");
                System.out.println();
                if(new String(mailPassword.getPassword()).equals("Your Password")) {
                    mailPassword.setForeground(Color.black);
                    mailPassword.setEchoChar('*');
                    mailPassword.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(new String(mailPassword.getPassword()).equals("")){
                    mailPassword.setText("Your Password");
                    mailPassword.setEchoChar((char) 0);
                    mailPassword.setForeground(Color.lightGray);
                }
            }
        });
        lTop.add(mailPassword);
        senderMail = new JTextField("Who you want to reply to");
        senderMail.setForeground(Color.lightGray);
        senderMail.setFont(new Font("Arial", Font.PLAIN, 16));
        senderMail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(senderMail.getText().equals("Who you want to reply to"))
                    senderMail.setText("");
                senderMail.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(senderMail.getText().equals("")) {
                    senderMail.setForeground(Color.lightGray);
                    senderMail.setText("Who you want to reply to");
                }
            }
        });
        lTop.add(senderMail);
        replyTo = new JTextArea("Text to reply too");
        replyTo.setForeground(Color.lightGray);
        replyTo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(replyTo.getText().equals("Text to reply too"))
                    replyTo.setText("");
                replyTo.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(replyTo.getText().equals("")) {
                    replyTo.setForeground(Color.lightGray);
                    replyTo.setText("Text to reply too");
                }
            }
        });
        replyTo.setColumns(50);
        replyTo.setRows(50);
        replyTo.setFont(new Font("Arial", Font.PLAIN, 16));
        reply = new JTextArea("Your reply");
        reply.setForeground(Color.lightGray);
        reply.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(reply.getText().equals("Your reply"))
                    reply.setText("");
                reply.setForeground(Color.black);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(reply.getText().equals("")) {
                    reply.setForeground(Color.lightGray);
                    reply.setText("Your reply");
                }
            }
        });
        reply.setColumns(5);
        reply.setRows(5);
        reply.setFont(new Font("Arial", Font.PLAIN, 16));
        areaPane = new JScrollPane(replyTo);
        right.add(areaPane, BorderLayout.PAGE_START);
        replyPane = new JScrollPane(reply);
        lBottom.add(replyPane, BorderLayout.PAGE_START);
        submit = new JButton("Submit!");
        submit.setFont(new Font("Arial", Font.PLAIN, 16));
        submit.setFocusable(false);
        submit.addActionListener(new Main());
        lTop.add(submit);
        pack();
        setSize(500, 500);
    }

    public String getReplyAdress() {
        if(yourMail.getText().equals("Your Mail address") || yourMail.getText().equals("") || !yourMail.getText().contains("@")){
            //throw new Exception("no mail adress provided");
            return "false";
        }
        else return yourMail.getText();
    }

    public String getReplyPass() {
        return new String(mailPassword.getPassword());
    }

    public String getSenderMail(){
        if(yourMail.getText().equals("Who you want to reply to") || yourMail.getText().equals("") || !yourMail.getText().contains("@")){
            return "false";
        }
        return senderMail.getText();
    }

}
