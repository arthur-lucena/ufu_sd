package br.ufu.sd.work.server;

import br.ufu.sd.work.util.ClientSocketCommand;
import br.ufu.sd.work.util.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class ReceiveCommand implements Runnable {
    private BlockingQueue<ClientSocketCommand> queue;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private volatile boolean running = true;

    public ReceiveCommand(ObjectInputStream inFromClient, ObjectOutputStream outToClient,
                          BlockingQueue<ClientSocketCommand> queue) {
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        this.queue = queue;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Command command = (Command) inFromClient.readObject();
                queue.add(new ClientSocketCommand(outToClient, command));
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                running = false;
            }
        }

        return;
    }
}
