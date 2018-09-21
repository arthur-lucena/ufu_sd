package br.ufu.sd.work.log;

import br.ufu.sd.work.model.Metadata;

import java.time.LocalDateTime;

/**
 * Created by ismaley on 20/09/18.
 */
public class Logger {

    public static void main(String[] args) {
        Metadata metadata = new Metadata("hello you", LocalDateTime.now(), LocalDateTime.now());
        LogManager.createFile();
        LogManager.append(metadata);

    }

}
