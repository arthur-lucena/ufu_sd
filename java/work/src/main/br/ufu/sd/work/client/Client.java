package br.ufu.sd.work.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 61666;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        Scanner s = new Scanner(System.in);
        createConnection();

        CommandSender cs = new CommandSender(outToServer);
        new Thread(new CommandReceiver(inFromServer)).start();

        while (true) {
            System.out.println("digite um comando");
            String command = s.nextLine();
            cs.send(command);
        }
    }

    private void createConnection() {
        try {
            clientSocket = new Socket(IP, PORT);
            outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}