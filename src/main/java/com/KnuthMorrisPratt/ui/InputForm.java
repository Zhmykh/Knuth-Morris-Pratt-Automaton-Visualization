package com.KnuthMorrisPratt.ui;

import javax.swing.*;

public class InputForm extends JDialog {
    private JPanel panel1;
    private JTextField textFieldPattern;
    private JButton buttonOK;
    private JTextArea textAreaText;

    public InputForm(String[] data) {
        setContentPane(panel1);
        setTitle("Enter values");
        setSize(400, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        buttonOK.addActionListener(e -> {
            data[0] = textFieldPattern.getText();
            data[1] = textAreaText.getText();
            System.out.println("aaaaa");
            dispose();
        });
        setModal(true);
        setVisible(true);
    }
}
