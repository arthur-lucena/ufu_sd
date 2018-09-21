package br.ufu.sd.work.log;

import br.ufu.sd.work.model.Metadata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by ismaley on 19/09/18.
 */
public class LogManager {

    private Path filePath;

    public LogManager(String path) {
        this.filePath = Paths.get(path);
    }

    public void createFile() {
        if(!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void append(Metadata metadata) {
        try {
            Files.write(filePath, getMetadataAsWritableString(metadata).getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Metadata> read() {
        List<String> loggedOperations = new ArrayList<>();
        loggedOperations = getLines(loggedOperations);

        if(!loggedOperations.isEmpty()) {
            return loggedOperations.stream().map(this::toMetadata).collect(toList());
        }
        return new ArrayList<>();
    }

    private List<String> getLines(List<String> loggedOperations) {
        try {
            loggedOperations = Files.readAllLines(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loggedOperations;
    }

    private Metadata toMetadata(String log) {
        List<String> data = Arrays.asList(log.split(","));
        return new Metadata(data.get(0), data.get(1), LocalDateTime.parse(data.get(2)),
                data.get(3), LocalDateTime.parse(data.get(4)));
    }

    private String getMetadataAsWritableString(Metadata metadata) {
        return String.format("%s,%s,%s,%s,%s\n", metadata.getMessage(), metadata.getCreatedBy(),
                metadata.getCreatedAt().toString(), metadata.getUpdatedBy(), metadata.getUpdatedAt().toString());
    }

}
