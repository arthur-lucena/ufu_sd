package br.ufu.sd.work.server;

import br.ufu.sd.work.util.ClientSocketCommand;
import br.ufu.sd.work.util.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Server {
    private BlockingQueue<ClientSocketCommand> queue;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    public static void main(String[] args) {
        Server server = new Server();
        server.start(61666);
    }

    public void start(int port) {
        try {
            queue = new ArrayBlockingQueue<>(10000);
            serverSocket = new ServerSocket(port);

            new Thread(new CommandQueueConsumption(queue)).start();

            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println(clientSocket);

                outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                inFromClient = new ObjectInputStream(clientSocket.getInputStream());

                Command command = new Command();
                command.setCommandString("Conectado!");
                command.setExecuted(true);

                outToClient.writeObject(command);

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
