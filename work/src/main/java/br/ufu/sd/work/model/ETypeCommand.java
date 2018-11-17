package br.ufu.sd.work.model;

public enum ETypeCommand {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    SELECT("select"),
    EXIT("exit"),
    SNAPSHOT("snap");

    private String name;

    ETypeCommand(String name) {
        this.name = name;
    }

    public static ETypeCommand fromString(String s) {
        for (ETypeCommand b : ETypeCommand.values()) {
            if (b.name.equalsIgnoreCase(s)) {
                return b;
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }
}
