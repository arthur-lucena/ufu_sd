package br.ufu.sd.work.util;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageCommand implements Serializable {
    private ETypeCommand typeCommand;
    private ICommand command;
    private String[] args;
    private int idClient;
    private LocalDateTime timeStamp;
    private boolean executed;

    public MessageCommand() {

    }

    public MessageCommand(MessageCommand messageCommand) {
        this.typeCommand = messageCommand.getTypeCommand();
        this.command = messageCommand.getCommand();
        this.args = messageCommand.getArgs();
        this.idClient = messageCommand.getIdClient();
        this.timeStamp = messageCommand.getTimeStamp();
        this.executed = messageCommand.getExecuted();
    }

    public MessageCommand(ETypeCommand typeCommand, ICommand command,
                          String[] args, int idClient, LocalDateTime timeStamp, boolean executed) {
        this.typeCommand = typeCommand;
        this.command = command;
        this.args = args;
        this.idClient = idClient;
        this.timeStamp = timeStamp;
        this.executed = executed;
    }

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

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean getExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
