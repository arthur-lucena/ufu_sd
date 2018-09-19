package br.ufu.sd.work.server;

import br.ufu.sd.work.util.ClientSocketCommand;
import br.ufu.sd.work.util.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ReceiveCommand implements Runnable {
    private Socket clientSocket;
    private BlockingQueue<ClientSocketCommand> queue;
    private BufferedReader in;

    public ReceiveCommand(Socket clientSocket, BlockingQueue<ClientSocketCommand> queue) {
        this.clientSocket = clientSocket;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println(clientSocket);

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Command c = new Command();

            String sc = in.readLine();
            c.setNome(sc);

            queue.add(new ClientSocketCommand(clientSocket, c));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

}
