package br.ufu.sd.work.client;


import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommandSender {

    private ObjectOutputStream outToServer;

    public CommandSender(ObjectOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public void send(String commandString) {
        try {
            MessageCommand messageCommand = new MessageCommand();
            messageCommand.setTypeCommand(ETypeCommand.valueOf(commandString.toUpperCase()));
            outToServer.writeObject(messageCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
