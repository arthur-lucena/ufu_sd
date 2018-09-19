package br.ufu.sd.work.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 61666;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("digite um comando");
            String command = s.nextLine();

            new Thread(new CommandHandler(IP, PORT, command)).start();
        }
    }
}