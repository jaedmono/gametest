package com.jaedmono.game.client.component;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

import static com.jaedmono.game.utils.AppMessages.*;

public class GameFrame extends JFrame {

    private JLabel messageLabel ;
    private Square[][] board ;

    public GameFrame(PrintWriter out){
        new JFrame(FRAME_LABEL);
        messageLabel = new JLabel(WAITING_TURN);
        messageLabel.setBackground(Color.lightGray);
        board = new Square[6][9];
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setVisible(true);
        setResizable(false);
        getContentPane().add(messageLabel, BorderLayout.NORTH);
        getContentPane().add(new EntryData(NOTICE_TURN, out, messageLabel), BorderLayout.AFTER_LAST_LINE);
        getContentPane().add(new BoardPanel(board), BorderLayout.CENTER);
    }

    public void repaintBoard(int row, int column, char mark) {
        board[row][column].setText(mark);
        board[row][column].repaint();
    }

    public void setTextMessageLabel(String text) {
        this.messageLabel.setText(text);
    }

    public void showMessageDialog(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
