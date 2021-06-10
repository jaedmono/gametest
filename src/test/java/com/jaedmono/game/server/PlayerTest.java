package com.jaedmono.game.server;

import com.jaedmono.game.utils.StateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.jaedmono.game.utils.AppMessages.WAITING_OPPONENT;
import static com.jaedmono.game.utils.AppMessages.YOUR_MOVE;

public class PlayerTest {

    private Player player;
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private Game game;

    @BeforeEach
    public void init(){
        socket = Mockito.mock(Socket.class);
        game = Mockito.mock(Game.class);
        input = Mockito.mock(Scanner.class);
        output = Mockito.mock(PrintWriter.class);
    }

    @Test
    public void setUpPlayer() throws InterruptedException {
        player = new Player(socket, 'X', game, input, output);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(player);
        executor.awaitTermination(5, TimeUnit.SECONDS);
        Mockito.verify(game).setCurrentPlayer(player);
        Mockito.verify(output).println(StateResponse.MESSAGE.name() + WAITING_OPPONENT);
    }

    @Test
    public void setUPOpponent() throws InterruptedException {
        Mockito.when(game.getCurrentPlayer()).thenReturn(new Player(socket, 'X', game, input, output));
        player = new Player(socket, 'O', game, input, output);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(player);
        executor.awaitTermination(5, TimeUnit.SECONDS);
        Mockito.verify(game).getCurrentPlayer();
        Mockito.verify(output).println(StateResponse.MESSAGE.name() + YOUR_MOVE);
    }

    @Test
    public void processCommandsQuit() throws InterruptedException {
        player = new Player(socket, 'X', game, input, output);
        Mockito.when(input.hasNextLine()).thenReturn(true);
        Mockito.when(input.nextLine()).thenReturn(StateResponse.QUIT.name());
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(player);
        executor.awaitTermination(5, TimeUnit.SECONDS);
        Mockito.verify(game).setCurrentPlayer(player);
        Mockito.verify(output).println(StateResponse.MESSAGE.name() + WAITING_OPPONENT);
    }

    @Test
    public void processCommandsHasAWinner() throws InterruptedException {
        player = new Player(socket, 'X', game, input, output);
        Player player2 = new Player(socket, 'O', game, input, output);
        Mockito.when(game.getCurrentPlayer()).thenReturn(player2);
        Mockito.when(game.move(Mockito.anyInt(), Mockito.any(Player.class))).thenReturn("51");
        Mockito.when(game.hasWinner(5, 1)).thenReturn(true);
        Mockito.when(input.hasNextLine()).thenReturn(true);
        Mockito.when(input.nextLine()).thenReturn(StateResponse.MOVE.name()+" 2");
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(player);
        executor.awaitTermination(5, TimeUnit.SECONDS);
        Mockito.verify(game).setCurrentPlayer(player);
        Mockito.verify(game, Mockito.atLeast(1)).hasWinner(5, 1);
        Mockito.verify(output, Mockito.atLeast(1)).println(StateResponse.VICTORY.name());
    }

    @Test
    public void processCommandsBoardFilledUp() throws InterruptedException {
        player = new Player(socket, 'X', game, input, output);
        Player player2 = new Player(socket, 'O', game, input, output);
        Mockito.when(game.getCurrentPlayer()).thenReturn(player2);
        Mockito.when(game.move(Mockito.anyInt(), Mockito.any(Player.class))).thenReturn("51");
        Mockito.when(game.hasWinner(5, 1)).thenReturn(false);
        Mockito.when(game.boardFilledUp()).thenReturn(true);
        Mockito.when(input.hasNextLine()).thenReturn(true);
        Mockito.when(input.nextLine()).thenReturn(StateResponse.MOVE.name()+" 2");
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(player);
        executor.awaitTermination(5, TimeUnit.SECONDS);
        Mockito.verify(game).setCurrentPlayer(player);
        Mockito.verify(game, Mockito.atLeast(1)).hasWinner(5, 1);
        Mockito.verify(output, Mockito.atLeast(1)).println(StateResponse.TIE.name());
    }

}
