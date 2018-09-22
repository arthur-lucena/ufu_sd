package br.ufu.sd.work.util;

import java.io.ObjectOutputStream;

public class ClientSocketCommand {
    private ObjectOutputStream outToClient;
    private MessageCommand messageCommand;

    public ClientSocketCommand(ObjectOutputStream outToClient, MessageCommand command) {
        this.outToClient = outToClient;
        this.messageCommand = messageCommand;
    }

    public ObjectOutputStream getOutToClient() {
        return outToClient;
    }

    public MessageCommand getMessageCommand() {
        return messageCommand;
    }
}
