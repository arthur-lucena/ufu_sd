package br.ufu.sd.work.log;

import br.ufu.sd.work.model.Metadata;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ismaley on 20/09/18.
 */
public class Logger {

    public static void main(String[] args) {

        Metadata metadata = new Metadata("hello you", "user_create", LocalDateTime.now(), "user_update", LocalDateTime.now());
        LogManager logManager = new LogManager();
        logManager.createFile();
        logManager.append(metadata);
        List<Metadata> metadataList = logManager.read();
        metadataList.forEach(s -> System.out.println(s.toString()));

    }

}
