package br.ufu.sd.work.log;

import br.ufu.sd.work.model.Metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by ismaley on 19/09/18.
 */
public class LogManager {

    private static File logFile;

    public static void createFile() {
        if(!logFile.exists()) {
            logFile = new File("/", "log.txt");
        }
    }

    public static void append(Metadata metadata, OperationStatus status) {
        try {
            Files.write(Paths.get("/log.txt"), getMetadata(metadata, status).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Metadata> read() {
        List<String> loggedOperations = new ArrayList<>();
        try {
            loggedOperations = Files.readAllLines(Paths.get("/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!loggedOperations.isEmpty()) {
            return loggedOperations.stream().map(LogManager::toMetadata).collect(toList());
        }
        return new ArrayList<>();
    }

    private static Metadata toMetadata(String log) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<String> data = Arrays.asList(log.split(","));
        return Metadata.builder()
                .message(data.get(0))
                .createdAt(LocalDateTime.parse(data.get(1), formatter))
                .updatedAt(LocalDateTime.parse(data.get(2), formatter))
                .build();
    }

    private static String getMetadata(Metadata metadata, OperationStatus status) {
        return String.format("%s, %s, %s, %s", metadata.getMessage(), metadata.getCreatedAt().toString(),
                metadata.getUpdatedAt().toString(), status.name());
    }
}
