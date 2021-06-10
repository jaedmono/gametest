package com.jaedmono.game;

import com.jaedmono.game.client.GameClient;
import com.jaedmono.game.client.component.GameFrame;
import com.jaedmono.game.server.Game;
import com.jaedmono.game.server.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jaedmono.game.utils.AppMessages.SERVER_STARTED;

public class AppGame {

    public static void main(String... args) throws Exception{
        if(args[0] != null && args[0].equals("client")){
            Socket socket = new Socket("localhost", 58901);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            GameClient gameClient = new GameClient(socket, in, out, new GameFrame(out));
            gameClient.setUp();
            gameClient.play();
        }else{
            try (ServerSocket listener = new ServerSocket(58901)) {
                System.out.println(SERVER_STARTED);
                ExecutorService pool = Executors.newFixedThreadPool(200);
                while (true) {
                    Game game = new Game();
                    pool.execute(createPlayer(listener.accept(), 'X', game));
                    pool.execute(createPlayer(listener.accept(), 'O', game));
                }
            }
        }
    }

    private static Player createPlayer(Socket socket, char mark, Game game ) throws IOException {
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        return new Player(socket, mark, game, input, output);
    }
}
