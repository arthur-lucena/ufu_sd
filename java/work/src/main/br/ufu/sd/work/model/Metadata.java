package br.ufu.sd.work.model;

import br.ufu.sd.work.util.MessageCommand;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.ufu.sd.work.model.ETypeCommand.DELETE;
import static br.ufu.sd.work.model.ETypeCommand.INSERT;
import static br.ufu.sd.work.model.ETypeCommand.UPDATE;

/**
 * Created by ismaley on 19/09/18.
 */
public class Metadata implements Serializable {

    private Long id;
    private String message;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public Metadata() {
    }

    public Metadata(Long id, String message, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String toString() {
        return  "id: " + id + ", " +
                "message: " + message + ", " +
                "createdBy: " + createdBy + ", " +
                "createdAt: " + createdAt + ", " +
                "updatedBy: " + updatedBy + ", " +
                "updatedAt: " + updatedAt;
    }

    public static Metadata fromLogString(String metadataLog) {
        List<String> data = Arrays.asList(metadataLog.split(","));
        return new Metadata(
                Long.valueOf(data.get(0)),
                Optional.ofNullable(data.get(1)).orElse(null),
                Optional.ofNullable(data.get(2)).orElse(null),
                Optional.ofNullable(data.get(3)).map(Metadata::resolveDate).orElse(null),
                Optional.ofNullable(data.get(4)).orElse(null),
                Optional.ofNullable(data.get(5)).map(Metadata::resolveDate).orElse(null));
    }

    public static Metadata fromCommand(MessageCommand messageCommand) {
        if(INSERT.equals(messageCommand.getTypeCommand())) {
            return new Metadata(messageCommand.getObjectId(), messageCommand.getArgs()[0], String.valueOf(messageCommand.getIdClient()),
                    messageCommand.getTimeStamp(), String.valueOf(messageCommand.getIdClient()), messageCommand.getTimeStamp());
        }

        if(UPDATE.equals(messageCommand.getTypeCommand())) {
            return new Metadata(messageCommand.getObjectId(), messageCommand.getArgs()[0], null,
                    null, String.valueOf(messageCommand.getIdClient()), messageCommand.getTimeStamp());
        }

        if(DELETE.equals(messageCommand.getTypeCommand())) {
            return new Metadata(messageCommand.getObjectId(), null, null,
                    null, String.valueOf(messageCommand.getIdClient()), messageCommand.getTimeStamp());
        }

        else return null;
    }

    private static LocalDateTime resolveDate(String date) {
        if(date.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(date);
    }
}
