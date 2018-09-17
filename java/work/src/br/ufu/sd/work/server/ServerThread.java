package br.ufu.sd.work.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            System.out.println(clientSocket);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = null;

            greeting = in.readLine();

            if ("hello server".equals(greeting)) {
                System.out.println("hello client");
                out.println("commando ok");
            } else {
                System.out.println("unrecognised greeting");
                out.println("command fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
