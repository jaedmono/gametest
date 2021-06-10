package com.jaedmono.game.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.jaedmono.game.utils.AppMessages.*;

public class GameTest {

    private Game game;
    private Player player_X;
    private Player player_O;

    @BeforeEach
    public void init(){
        game = new Game();
        player_X = Mockito.mock(Player.class);
        player_O = Mockito.mock(Player.class);
        game.setCurrentPlayer(player_X);
    }

    @Test()
    public void moveNotYourTurn(){
        Assertions.assertThrows(IllegalStateException.class, () -> game.move(1, player_O), NOT_YOUR_TURN);
    }

    @Test()
    public void moveNotOpponent(){
        Assertions.assertThrows(IllegalStateException.class, () -> game.move(1, player_X), NOT_OPPONENT);
    }

    @Test()
    public void moveFullColumn(){
        Mockito.when(player_X.getOpponent()).thenReturn(player_O);
        Mockito.when(player_O.getOpponent()).thenReturn(player_X);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            game.move(1, player_X);
            game.move(1, player_O);
            game.move(1, player_X);
            game.move(1, player_O);
            game.move(1, player_X);
            game.move(1, player_O);
            }, FULL_COLUMN);
    }

    @Test()
    public void boardFilledUpReturnTrue(){
        Mockito.when(player_X.getOpponent()).thenReturn(player_O);
        Mockito.when(player_O.getOpponent()).thenReturn(player_X);
        boolean nextTurn = false;
        for(int i=0; i < 6; i++){
            for(int j=0; j<9; j++){
                if(nextTurn){
                    game.move(j+1, player_O);
                    nextTurn = false;
                }else{
                    game.move(j+1, player_X);
                    nextTurn = true;
                }
            }
        }
        Assertions.assertTrue(game.boardFilledUp());
    }

    @Test()
    public void hasAWinnerReturnTrueVerticalValidation(){
        Mockito.when(player_X.getOpponent()).thenReturn(player_O);
        Mockito.when(player_O.getOpponent()).thenReturn(player_X);
        game.move(1, player_X);
        game.move(2, player_O);
        game.move(1, player_X);
        game.move(2, player_O);
        game.move(1, player_X);
        game.move(2, player_O);
        game.move(1, player_X);
        game.move(2, player_O);
        game.move(1, player_X);
        Assertions.assertTrue(game.hasWinner(1, 5));
    }

    @Test()
    public void hasAWinnerReturnTrueHorizontalValidation(){
        Mockito.when(player_X.getOpponent()).thenReturn(player_O);
        Mockito.when(player_O.getOpponent()).thenReturn(player_X);
        game.move(1, player_X);
        game.move(1, player_O);
        game.move(2, player_X);
        game.move(2, player_O);
        game.move(3, player_X);
        game.move(2, player_O);
        game.move(4, player_X);
        game.move(2, player_O);
        game.move(5, player_X);
        Assertions.assertTrue(game.hasWinner(5, 4));
    }

    @Test()
    public void hasAWinnerReturnTrueUPDiagonalValidation(){
        Mockito.when(player_X.getOpponent()).thenReturn(player_O);
        Mockito.when(player_O.getOpponent()).thenReturn(player_X);
        game.move(1, player_X);
        game.move(1, player_O);

        game.move(1, player_X);
        game.move(1, player_O);

        game.move(1, player_X);
        game.move(2, player_O);

        game.move(2, player_X);
        game.move(2, player_O);

        game.move(2, player_X);
        game.move(2, player_O);

        game.move(3, player_X);
        game.move(3, player_O);

        game.move(3, player_X);
        game.move(4, player_O);

        game.move(4, player_X);
        game.move(4, player_O);

        game.move(5, player_X);
        Assertions.assertTrue(game.hasWinner(5, 4));
    }

    @Test()
    public void hasAWinnerReturnTrueDownDiagonalValidation(){
        Mockito.when(player_X.getOpponent()).thenReturn(player_O);
        Mockito.when(player_O.getOpponent()).thenReturn(player_X);
        game.move(5, player_X);
        game.move(5, player_O);

        game.move(5, player_X);
        game.move(5, player_O);

        game.move(5, player_X);
        game.move(4, player_O);

        game.move(4, player_X);
        game.move(4, player_O);

        game.move(4, player_X);
        game.move(4, player_O);

        game.move(3, player_X);
        game.move(3, player_O);

        game.move(3, player_X);
        game.move(3, player_O);

        game.move(3, player_X);
        game.move(2, player_O);

        game.move(1, player_X);
        Assertions.assertTrue(game.hasWinner(1, 5));
    }
}
