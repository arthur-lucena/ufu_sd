package br.ufu.sd.work.server;

import br.ufu.sd.work.util.ClientSocketCommand;
import br.ufu.sd.work.util.Command;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ReceiveCommand implements Runnable {
    private BlockingQueue<ClientSocketCommand> queue;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    public ReceiveCommand(ObjectInputStream inFromClient, ObjectOutputStream outToClient,
                          BlockingQueue<ClientSocketCommand> queue) {
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Command command = (Command) inFromClient.readObject();
                queue.add(new ClientSocketCommand(outToClient, command));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
