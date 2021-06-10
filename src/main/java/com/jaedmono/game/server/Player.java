package com.jaedmono.game.server;

import com.jaedmono.game.utils.StateResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static com.jaedmono.game.utils.AppMessages.*;

public class Player implements Runnable {

    private char mark;
    private Player opponent;
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private Game game;

    public Player(Socket socket, char mark, Game game, Scanner input, PrintWriter output ) {
        this.socket = socket;
        this.mark = mark;
        this.game = game;
        this.input = input;
        this.output = output;
    }

    public void run() {
        try {
            setup();
            processCommands();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (opponent != null && opponent.output != null) {
                opponent.output.println(StateResponse.OTHER_PLAYER_LEFT.name());
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    private void setup() {
        output.println(StateResponse.WELCOME.name() + " " + mark);
        if (mark == 'X') {
            game.setCurrentPlayer(this);
            output.println(StateResponse.MESSAGE.name() + WAITING_OPPONENT);
        } else {
            opponent = game.getCurrentPlayer();
            opponent.opponent = this;
            opponent.output.println(StateResponse.MESSAGE.name() + YOUR_MOVE);
        }
    }

    private void processCommands() {
        while (input.hasNextLine()) {
            String command = input.nextLine();
            if (command.startsWith(StateResponse.QUIT.name())) {
                return;
            } else if (command.startsWith(StateResponse.MOVE.name())) {
                processMoveCommand(Integer.parseInt(command.substring(5)));
            }
        }
    }

    private void processMoveCommand(int location) {
        try {
            String coordinates = game.move(location, this);
            output.println(StateResponse.VALID_MOVE.name() + coordinates);
            notifyOpponent(StateResponse.OPPONENT_MOVED.name() + " " + coordinates);
            if (game.hasWinner(Integer.parseInt(coordinates.substring(0,1)),location-1)) {
                output.println(StateResponse.VICTORY.name());
                notifyOpponent(StateResponse.DEFEAT.name());
            } else if (game.boardFilledUp()) {
                output.println(StateResponse.TIE.name());
                notifyOpponent(StateResponse.TIE.name());
            }
        } catch (IllegalStateException e) {
            output.println(StateResponse.MESSAGE.name() + e.getMessage());
        }
    }

    private void notifyOpponent(String message) {
        if (opponent != null && opponent.output != null) {
            opponent.output.println(message);
        }
    }

    public Player getOpponent() {
        return opponent;
    }
}
