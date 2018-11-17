package br.ufu.sd.work.log;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static br.ufu.sd.work.model.ETypeCommand.DELETE;
import static br.ufu.sd.work.model.ETypeCommand.SNAPSHOT;
import static java.util.stream.Collectors.toList;

/**
 * Created by ismaley on 19/09/18.
 */
public class LogManager {

    private Path logFilePath;
    private Path snapshotFilePath;
    private String logFileLocation;
    private String snapshotFileLocation;
    private int currentLogFile;
    private int currentSnapshotFile;

    public LogManager(String logFileLocation, String snapshotFileLocation) {
        this.logFilePath = Paths.get(logFileLocation);
        this.snapshotFilePath = Paths.get(snapshotFileLocation);
    }

    public void createFile() {
        if(!Files.exists(logFilePath)) {
            try {
                Files.createFile(logFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void appendLog(Metadata metadata, ETypeCommand commandType) {
        appendInformation(metadata, commandType, logFilePath);
    }

    public LinkedHashMap<Long, Metadata> recoverInformation() {
        return recoverInformation(logFilePath);
        //recoverInformation(snapFilePath)
        //return merged informations
    }

    private LinkedHashMap<Long, Metadata> recoverInformation(Path filePath) {
        List<String> loggedOperations = new ArrayList<>();
        List<Long> removedItems = new ArrayList<>();
        loggedOperations = getLines(loggedOperations, filePath);

        if(!loggedOperations.isEmpty()) {
            List<Metadata> metadataList = loggedOperations.stream().map(Metadata::fromLogString).collect(toList());
             loggedOperations.forEach(s -> findRemovedObjects(s, removedItems));
            return mergeInformation(metadataList, removedItems);
        }
        return new LinkedHashMap<>();
    }

    public void snapshot() {
        if(Files.exists(logFilePath)) {
            createSnapshot();
        }
        //recoverInformation current log file
        //create snapshot file
        //update current snapshot and log count
        System.out.println("creating snapshot file");
    }

    private void appendSnapshotInformation(Metadata metadata, ETypeCommand commandType) {
        appendInformation(metadata, commandType, snapshotFilePath);
    }

    private void appendInformation(Metadata metadata, ETypeCommand commandType, Path filePath) {
        try {
            Files.write(filePath, getMetadataAsWritableString(metadata, commandType).getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getLines(List<String> loggedOperations, Path filePath) {
        try {
            loggedOperations = Files.readAllLines(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loggedOperations;
    }

    private String getMetadataAsWritableString(Metadata metadata, ETypeCommand commandType) {
        return String.format("%s,%s,%s,%s,%s,%s,%s\n",
                String.valueOf(metadata.getId()),
                metadata.getMessage(),
                metadata.getCreatedBy(),
                Optional.ofNullable(metadata.getCreatedAt()).map(LocalDateTime::toString).orElse(null),
                metadata.getUpdatedBy(),
                Optional.ofNullable(metadata.getUpdatedAt()).map(LocalDateTime::toString).orElse(null),
                commandType.name());
    }

    private LinkedHashMap<Long, Metadata> mergeInformation(List<Metadata> metadataList, List<Long> removed) {
        LinkedHashMap<Long, Metadata> metadataMap = new LinkedHashMap<>();
        Map<Long, List<Metadata>> groupedById = metadataList.stream().collect(Collectors.groupingBy(Metadata::getId,
                Collectors.toList()));
        removed.forEach(groupedById::remove);
        groupedById.forEach((k, v) -> metadataMap.put(k, resolveLatestInformation(v)));
        return metadataMap;
    }

    private void findRemovedObjects(String loggedOperations, List<Long> removedIds) {
        List<String> log = Arrays.asList(loggedOperations.split(","));
        if(DELETE.name().equals(log.get(6))) {
            removedIds.add(Long.valueOf(log.get(0)));
        }
    }

    private Metadata resolveLatestInformation(List<Metadata> metadatas) {
        Metadata metadata = new Metadata();
        metadatas.sort(Comparator.comparing(Metadata::getUpdatedAt));
        Collections.reverse(metadatas);
        metadatas.forEach(m -> findCreationInfo(m, metadata));
        metadatas.stream().findFirst().ifPresent(m -> {
            metadata.setId(m.getId());
            metadata.setMessage(m.getMessage());
            metadata.setUpdatedAt(m.getUpdatedAt());
            metadata.setUpdatedBy(m.getUpdatedBy());
        });
        return metadata;
    }

    private void findCreationInfo(Metadata m, Metadata current) {
        if(m.getCreatedAt() != null) {
            current.setCreatedAt(m.getCreatedAt());
        }
        if(m.getCreatedBy() != null) {
            current.setCreatedBy(m.getCreatedBy());
        }
    }

    //use on all filePath calls
    private String resolveCurrentPath(String filePath, int current) {
        return String.format("%s.%s.%s", filePath, current, "txt");
    }

    private void createSnapshot() {
        String currentSnapshotFileName = resolveCurrentPath(snapshotFileLocation, currentSnapshotFile);
        try {
            Files.createFile(Paths.get(currentSnapshotFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        LinkedHashMap<Long, Metadata> currentState = recoverInformation(Paths.get(resolveCurrentPath(logFileLocation, currentLogFile)));
        currentState.forEach((k,v) -> appendSnapshotInformation(v, SNAPSHOT));
    }

}
