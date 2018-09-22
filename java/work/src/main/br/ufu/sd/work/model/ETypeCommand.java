package br.ufu.sd.work.model;

public enum ETypeCommand {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    SELECT("select"),
    EXIT("exit");

    private String commandString;

    ETypeCommand(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}
