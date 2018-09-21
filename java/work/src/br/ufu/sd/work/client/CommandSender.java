package br.ufu.sd.work.client;


import br.ufu.sd.work.util.Command;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommandSender {

    private ObjectOutputStream outToServer;

    public CommandSender(ObjectOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public void send(String commandString) {
        try {
            Command commandSend = new Command();
            commandSend.setCommandString(commandString);
            outToServer.writeObject(commandSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
