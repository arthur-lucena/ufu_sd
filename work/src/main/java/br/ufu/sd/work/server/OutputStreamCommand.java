package br.ufu.sd.work.server;

import br.ufu.sd.work.util.MessageCommand;

import java.io.ObjectOutputStream;

public class OutputStreamCommand {
    private ObjectOutputStream outputClient;
    private MessageCommand messageCommand;

    public OutputStreamCommand(ObjectOutputStream outputClient, MessageCommand messageCommand) {
        this.outputClient = outputClient;
        this.messageCommand = messageCommand;
    }

    public ObjectOutputStream getOutputClient() {
        return outputClient;
    }

    public MessageCommand getMessageCommand() {
        return messageCommand;
    }
}
