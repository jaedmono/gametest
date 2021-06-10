package com.jaedmono.game.client;

import com.jaedmono.game.client.component.GameFrame;
import com.jaedmono.game.utils.StateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static com.jaedmono.game.utils.AppMessages.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;

public class GameClientTest {

    private GameClient gameClient;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private GameFrame gameFrame;

    @BeforeEach
    public void init() {
        socket = Mockito.mock(Socket.class);
        in = Mockito.mock(Scanner.class);
        out = Mockito.mock(PrintWriter.class);
        gameFrame = Mockito.mock(GameFrame.class);

        try {
            gameClient = new GameClient(socket, in, out, gameFrame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void playTestVictory() throws Exception {
        Mockito.when(in.hasNextLine()).thenReturn(true);
        Mockito.when(in.nextLine()).thenReturn(StateResponse.VICTORY.name());
        gameClient.play();
        Mockito.verify(gameFrame, atLeast(1)).showMessageDialog(WIN_MESSAGE);
    }

    @Test
    public void playTestTie() throws Exception {
        Mockito.when(in.hasNextLine()).thenReturn(true);
        Mockito.when(in.nextLine()).thenReturn(StateResponse.TIE.name());
        gameClient.play();
        Mockito.verify(gameFrame, atLeast(1)).showMessageDialog(TIE_MESSAGE);
    }

    @Test
    public void playTestDefeat() throws Exception {
        Mockito.when(in.hasNextLine()).thenReturn(true);
        Mockito.when(in.nextLine()).thenReturn(StateResponse.DEFEAT.name());
        gameClient.play();
        Mockito.verify(gameFrame, atLeast(1)).showMessageDialog(DEFEAT_MESSAGE);
    }

    @Test
    public void playTestPlayerLeft() throws Exception {
        Mockito.when(in.hasNextLine()).thenReturn(true);
        Mockito.when(in.nextLine()).thenReturn(StateResponse.OTHER_PLAYER_LEFT.name());
        gameClient.play();
        Mockito.verify(gameFrame, atLeast(1)).showMessageDialog(PLAYER_LEFT);
    }

}
