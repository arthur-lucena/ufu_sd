package br.ufu.sd.work.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        Server server = new Server();
        server.start(61666);
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);

            while (true)
                new ServerThread(serverSocket.accept()).run();
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
