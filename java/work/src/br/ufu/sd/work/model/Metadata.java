package br.ufu.sd.work.model;

import java.time.LocalDateTime;

/**
 * Created by ismaley on 19/09/18.
 */
public class Metadata {

    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public Metadata(String message, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}
