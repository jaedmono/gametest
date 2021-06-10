package com.jaedmono.game.client.component;

import com.jaedmono.game.utils.StateResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import static com.jaedmono.game.utils.AppMessages.COLUMN_ERROR;

public class EntryData extends JPanel implements ActionListener{

    private JLabel entryLabel;
    private JTextField entry;
    private PrintWriter out;
    private JLabel messageLabel;

    public EntryData(String textLabel, PrintWriter out, JLabel messageLabel){
        entryLabel = new JLabel(textLabel);
        entry = new JTextField();
        this.out = out;
        this.messageLabel = messageLabel;

        entry.addActionListener(this);

        setBackground(Color.GRAY);
        setLayout(new GridLayout(1, 2, 2, 2));
        add(entryLabel);
        add(entry);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String move = entry.getText();
        entry.setText("");
        if(move == null || move.length() < 0 || move.length() > 1 || !move.matches("[0-9]+") ){
            messageLabel.setText(COLUMN_ERROR);
        }else {
            out.println(StateResponse.MOVE.name()+" " + move);
        }
    }


}
