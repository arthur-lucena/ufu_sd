package br.ufu.sd.work.util;

import java.io.ObjectOutputStream;

public class ClientSocketCommand {
    private ObjectOutputStream outToClient;
    private Command command;

    public ClientSocketCommand(ObjectOutputStream outToClient, Command command) {
        this.outToClient = outToClient;
        this.command = command;
    }

    public ObjectOutputStream getOutToClient() {
        return outToClient;
    }

    public Command getCommand() {
        return command;
    }
}
