package br.ufu.sd.work.client;


import br.ufu.sd.work.util.Command;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommandSender {

    private ObjectOutputStream outToServer;

    public CommandSender(ObjectOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public void send(String commandString) {
        try {
            Command command = new Command();
            command.setName(commandString);
            outToServer.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
