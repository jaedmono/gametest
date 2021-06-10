package com.jaedmono.game.client;

import com.jaedmono.game.client.component.GameFrame;
import com.jaedmono.game.utils.StateResponse;

import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

import static com.jaedmono.game.utils.AppMessages.*;

public class GameClient {

    private GameFrame frame;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private char mark;
    private char opponentMark;

    public GameClient(Socket socket, Scanner in, PrintWriter out, GameFrame frame) throws Exception {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.frame = frame;
    }

    public void setUp(){
        String response = in.nextLine();
        mark = response.charAt(8);
        opponentMark = mark == 'X' ? 'O' : 'X';
        frame.setTitle(TITLE_FRAME + mark);
    }

    public void play() throws Exception {
        try {
            String response;
            Integer column;
            Integer row;
            while (in.hasNextLine()) {
                response = in.nextLine();
                if (response.startsWith(StateResponse.VALID_MOVE.name())) {
                    column = Integer.parseInt(response.substring(11));
                    row = Integer.parseInt(response.substring(10,11));
                    frame.setTextMessageLabel(VALID_MOVE_MESSAGE);
                    frame.repaintBoard(row, column, mark);
                } else if (response.startsWith(StateResponse.OPPONENT_MOVED.name())) {
                    column = Integer.parseInt(response.substring(16));
                    row = Integer.parseInt(response.substring(15,16));
                    frame.repaintBoard(row, column, opponentMark);
                    frame.setTextMessageLabel(ALERT_TURN);
                } else if (response.startsWith(StateResponse.MESSAGE.name())) {
                    frame.setTextMessageLabel(response.substring(8));
                } else if (response.startsWith(StateResponse.VICTORY.name())) {
                    frame.showMessageDialog(WIN_MESSAGE);
                    break;
                } else if (response.startsWith(StateResponse.DEFEAT.name())) {
                    frame.showMessageDialog(DEFEAT_MESSAGE);
                    break;
                } else if (response.startsWith(StateResponse.TIE.name())) {
                    frame.showMessageDialog(TIE_MESSAGE);
                    break;
                } else if (response.startsWith(StateResponse.OTHER_PLAYER_LEFT.name())) {
                    frame.showMessageDialog(PLAYER_LEFT);
                    break;
                }
            }
            out.println(StateResponse.QUIT.name());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
            frame.dispose();
        }
    }

}
