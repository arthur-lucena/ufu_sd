package br.ufu.sd.work.server;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Server {
    private BlockingQueue<OutputStreamCommand> queue;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private Dictionary dictionary;

    public static void main(String[] args) {
        Server server = new Server();
        server.start(61666);
    }

    public void start(int port) {
        try {
            queue = new ArrayBlockingQueue<>(1000000);
            serverSocket = new ServerSocket(port);

            CommandQueueConsumption runnable = new CommandQueueConsumption(queue, dictionary);
            Thread t = new Thread(runnable);
            t.start();

            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println(clientSocket);

                outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                inFromClient = new ObjectInputStream(clientSocket.getInputStream());

                outToClient.writeObject(new MessageCommand());

                new Thread(new ReceiveCommand(inFromClient, outToClient, queue)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e1) {

            }
        }
    }
}
