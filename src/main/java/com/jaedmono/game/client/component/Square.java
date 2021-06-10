package com.jaedmono.game.client.component;

import javax.swing.*;
import java.awt.*;

public class Square extends JPanel {

    JLabel label = new JLabel();

    public Square() {
        setBackground(Color.white);
        setLayout(new GridBagLayout());
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label);
    }

    public void setText(char text) {
        label.setForeground(text == 'X' ? Color.BLUE : Color.RED);
        label.setText(text + "");
    }
}
