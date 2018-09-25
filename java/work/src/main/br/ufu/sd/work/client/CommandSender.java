package br.ufu.sd.work.client;


import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;

import static br.ufu.sd.work.model.ETypeCommand.UPDATE;
import static java.lang.Thread.currentThread;

public class CommandSender {

    private ObjectOutputStream outToServer;

    public CommandSender(ObjectOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public void send(String commandString, String[] args) {
        try {
            ETypeCommand commandType = ETypeCommand.valueOf(commandString.toUpperCase());
            MessageCommand messageCommand = new MessageCommand();
            if(mustSetId(commandType)) {
                messageCommand.setObjectId(Long.valueOf(args[0]));
            }
            messageCommand.setTypeCommand(commandType);
            messageCommand.setArgs(args);
            messageCommand.setTimeStamp(LocalDateTime.now());
            messageCommand.setIdClient(Math.toIntExact(currentThread().getId()));
            outToServer.writeObject(messageCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean mustSetId(ETypeCommand commandType) {
        return ETypeCommand.SELECT.equals(commandType) || UPDATE.equals(commandType) ||
                ETypeCommand.DELETE.equals(commandType);
    }
}
