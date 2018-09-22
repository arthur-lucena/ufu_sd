package br.ufu.sd.work.util;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.Serializable;

public class MessageCommand implements Serializable {
    private ETypeCommand typeCommand;
    private ICommand command;
    private String[] args;
    private int idClient;
    private long timeStamp;
    private boolean executed;

    public ICommand getCommand() {
        return command;
    }

    public void setCommand(ICommand command) {
        this.command = command;
    }

    public ETypeCommand getTypeCommand() {
        return typeCommand;
    }

    public void setTypeCommand(ETypeCommand typeCommand) {
        this.typeCommand = typeCommand;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean getExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
