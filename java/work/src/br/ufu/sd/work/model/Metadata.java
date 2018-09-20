package br.ufu.sd.work.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by ismaley on 19/09/18.
 */
@Getter
@Builder
public class Metadata {

    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
