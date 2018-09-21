package br.ufu.sd.work.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 61666;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    private final static String INSERT = "insert";
    private final static String UPDATE = "update";
    private final static String DELETE = "delete";
    private final static String SELECT = "select";
    private final static String EXIT = "exit";


    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        Scanner s = new Scanner(System.in);
        createConnection();
        boolean running = true;

        CommandSender cs = new CommandSender(outToServer);
        CommandReceiver runnable = new CommandReceiver(inFromServer);
        Thread commandReceiverThread = new Thread(runnable);
        commandReceiverThread.start();

        while (running) {
            System.out.println("digite um comando: (insert | update | delete | select) <argumento1:argumento2:argumentoN> - exit : para sair");
            String allCommand = s.nextLine();

            // TODO adicionar argumentos futuramente
            //String[] allCommandArray = allCommand.split(" ");
            //String stringCommand = allCommandArray[0];
            //String args = allCommandArray[1].split(":");

            String stringCommand = allCommand;

            if (INSERT.equals(stringCommand) || UPDATE.equals(stringCommand) ||
                DELETE.equals(stringCommand) || SELECT.equals(stringCommand)) {
                cs.send(stringCommand);
            } else if (EXIT.equals(stringCommand)) {
                running = false;

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runnable.terminate();

                try {
                    commandReceiverThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    inFromServer.close();
                    outToServer.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                System.out.println("commando invalido");
            }
        }

        System.out.println("saindo... =D");
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