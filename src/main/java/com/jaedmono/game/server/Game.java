package com.jaedmono.game.server;

import java.util.Arrays;

import static com.jaedmono.game.utils.AppMessages.*;

public class Game {

    private Player[][] board = new Player[6][9];
    private Player currentPlayer;

    public boolean hasWinner(int x, int y) {
        return validateHorizontal(x, y) || validateDiagonals(x, y) || validateVertical(x, y) ;
    }

    private boolean validateVertical(int x, int y) {
        try {
            return board[x][y] == board[x + 1][y] &&
                    board[x][y] == board[x + 2][y] &&
                    board[x][y] == board[x + 3][y] &&
                    board[x][y] == board[x + 4][y] ;
        }catch (ArrayIndexOutOfBoundsException e){
            return false;
        }

    }

    private boolean validateHorizontal(int x, int y) {
        int countTimes = 0;
        for(int i=0; i < 9; i++ ){
            if(board[x][i] == null || board[x][i] != board[x][y]){
                countTimes = 0;
                continue;
            }
            countTimes++;
            if(countTimes == 5){
                return true;
            }
        }
        return false;
    }

    private boolean validateDiagonals(int x, int y) {
        int countTimes = 1;
        int indexX = x + 1;
        int indexY = y - 1;
        while(indexX < 6 && indexY >= 0) {
            if(board[indexX][indexY] == null || board[x][y] != board[indexX][indexY]){
                break;
            }
            countTimes++;
            indexX++;
            indexY--;
        }
        if(countTimes == 5) return true;

        countTimes = 1;
        indexX = x - 1;
        indexY = y - 1 ;
        while(indexX >= 0 && indexY >= 0) {
            if(board[indexX][indexY] == null || board[x][y] != board[indexX][indexY]){
                break;
            }
            countTimes++;
            indexX--;
            indexY--;
        }
        if(countTimes == 5) return true;

        countTimes = 1;
        indexX = x + 1;
        indexY = y + 1;
        while(indexX < 6 && indexY < 9) {
            if(board[indexX][indexY] == null || board[x][y] != board[indexX][indexY]){
                break;
            }
            countTimes++;
            indexX++;
            indexY++;
        }
        if(countTimes == 5) return true;

        countTimes = 1;
        indexX = x - 1;
        indexY = y + 1;
        while(indexX >= 0 && indexY < 9) {
            if(board[indexX][indexY] == null || board[x][y] != board[indexX][indexY]){
                break;
            }
            countTimes++;
            indexX--;
            indexY++;
        }

        return countTimes == 5;
    }

    public boolean boardFilledUp() {
        return Arrays.stream(board).allMatch(p -> Arrays.stream(p).allMatch(j -> j != null) );
    }

    public synchronized String move(int columnLocation , Player player) {
        --columnLocation;
        if (player != currentPlayer) {
            throw new IllegalStateException(NOT_YOUR_TURN);
        } else if (player.getOpponent() == null) {
            throw new IllegalStateException(NOT_OPPONENT);
        } else if (board[0][columnLocation] != null) {
            throw new IllegalStateException(FULL_COLUMN);
        }
        for(int i=5; i >= 0; i--){
            if(board[i][columnLocation] == null) {
                board[i][columnLocation] = currentPlayer;
                currentPlayer = currentPlayer.getOpponent();
                return ""+i+columnLocation;
            }
        }
        return "";
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
