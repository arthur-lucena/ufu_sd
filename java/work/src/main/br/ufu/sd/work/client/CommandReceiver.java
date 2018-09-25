package br.ufu.sd.work.client;

import br.ufu.sd.work.util.MessageCommand;

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
        try {
            inFromServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object objectReceived = inFromServer.readObject();

                if (objectReceived != null) {
                    MessageCommand messageCommand = (MessageCommand) objectReceived;

                    if (messageCommand.getCommand() == null) {
                        System.out.println("Conectado!");
                    } else {
                        if (messageCommand.getResponse() != null) {
                            System.out.println(messageCommand.getResponse());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }
}
