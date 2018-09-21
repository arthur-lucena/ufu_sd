package br.ufu.sd.work.client;

import br.ufu.sd.work.util.Command;

import java.io.IOException;
import java.io.ObjectInputStream;

public class CommandReceiver implements Runnable {

    private ObjectInputStream inFromServer;

    public CommandReceiver(ObjectInputStream inFromServer) {
        this.inFromServer = inFromServer;
    }

    private volatile boolean running = true;

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Command command = (Command) inFromServer.readObject();
                System.out.println(command.getCommandString() + " executado!");
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                running = false;
            }
        }

        System.out.println("saindo");
        return;
    }
}
