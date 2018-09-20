package br.ufu.sd.work.client;

import br.ufu.sd.work.util.Command;

import java.io.IOException;
import java.io.ObjectInputStream;

public class CommandReceiver implements Runnable {

    private ObjectInputStream inFromServer;

    public CommandReceiver(ObjectInputStream inFromServer) {
        this.inFromServer = inFromServer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Command command = (Command) inFromServer.readObject();
                System.out.println(command.getName() + " executado!");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
