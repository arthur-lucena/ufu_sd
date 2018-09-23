package br.ufu.sd.work.model;

import br.ufu.sd.work.util.MessageCommand;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.ufu.sd.work.model.ETypeCommand.INSERT;
import static br.ufu.sd.work.model.ETypeCommand.UPDATE;

/**
 * Created by ismaley on 19/09/18.
 */
public class Metadata {

    private String message;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public Metadata(String message, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
        this.message = message;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String toString() {
        return "message: " + message + ", " +
                "createdBy: " + createdBy + ", " +
                "createdAt: " + createdAt + ", " +
                "updatedBy: " + updatedBy + ", " +
                "updatedAt: " + updatedAt;
    }

    public static Metadata fromLogString(String metadataLog) {
        List<String> data = Arrays.asList(metadataLog.split(","));
        return new Metadata(
                Optional.ofNullable(data.get(0)).orElse(null),
                Optional.ofNullable(data.get(1)).orElse(null),
                Optional.ofNullable(data.get(2)).map(Metadata::resolveDate).orElse(null),
                Optional.ofNullable(data.get(3)).orElse(null),
                Optional.ofNullable(data.get(4)).map(Metadata::resolveDate).orElse(null));
    }

    private static LocalDateTime resolveDate(String date) {
        if(date.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(date);
    }

    public static Metadata fromCommand(MessageCommand messageCommand) {
        if(INSERT.equals(messageCommand.getTypeCommand())) {
            return new Metadata(messageCommand.getArgs()[0], String.valueOf(messageCommand.getIdClient()),
                    messageCommand.getTimeStamp(), null, null);
        }

        if(UPDATE.equals(messageCommand.getTypeCommand())) {
            return new Metadata(messageCommand.getArgs()[0], null,
                    null, String.valueOf(messageCommand.getIdClient()), messageCommand.getTimeStamp());
        }

        else return null;
    }
}
