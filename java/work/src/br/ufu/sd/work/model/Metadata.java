package br.ufu.sd.work.model;

import java.time.LocalDateTime;

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
        return "message: "+ message + ", " +
                "createdBy: " + createdBy + ", " +
                "createdAt: " + createdAt.toString() + ", " +
                "updatedBy: " + updatedBy + ", " +
                "updatedAt: " + updatedAt;
    }
 }
