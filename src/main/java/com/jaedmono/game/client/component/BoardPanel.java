package com.jaedmono.game.client.component;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    public BoardPanel(Square[][] board){
        setBackground(Color.black);
        setLayout(new GridLayout(6, 9, 2, 2));
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Square();
                this.add(board[i][j]);
            }
        }
    }
}
