package br.ufu.sd.work.server;

import br.ufu.sd.work.util.ClientSocketCommand;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Server {
    private ServerSocket serverSocket;
    private BlockingQueue<ClientSocketCommand> queue;

    public static void main(String[] args) {
        Server server = new Server();

        server.start(61666);
    }

    public void start(int port) {
        try {
            queue = new ArrayBlockingQueue<>(10000);
            serverSocket = new ServerSocket(port);

            new Thread(new CommandQueueConsumption(queue)).start();

            while (true)
                new Thread(new ReceiveCommand(serverSocket.accept(), queue)).start();
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
