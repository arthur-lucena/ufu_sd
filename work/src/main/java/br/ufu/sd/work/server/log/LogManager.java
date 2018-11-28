package br.ufu.sd.work.server.log;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.Server;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static br.ufu.sd.work.model.ETypeCommand.DELETE;
import static br.ufu.sd.work.model.ETypeCommand.SNAPSHOT;
import static java.util.stream.Collectors.toList;

/**
 * Created by ismaley on 19/09/18.
 */
public class LogManager {

    private String logFileLocation;
    //TODO: usar mesmo location pra log e snapshot
    private long serverId;
    private int currentLogFileNumber;
    private int currentSnapshotFileNumber;
    private final String log = "log";
    private final String snapshot = "snapshot";
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public LogManager(String logFileLocation, long serverId) {
        this.logFileLocation = logFileLocation;
        this.serverId = serverId;
        this.currentLogFileNumber = getCurrentFileNumber(logFileLocation, serverId, log);
        this.currentSnapshotFileNumber = getCurrentFileNumber(logFileLocation, serverId, snapshot);
    }

    public ImmutableMap<String, Integer> getCurrentCount() {
        return ImmutableMap.of(log, currentLogFileNumber, snapshot, currentSnapshotFileNumber);
    }

    public void createLogFile() {
        if (!Files.exists(currentLogFilePath())) {
            try {
                createDir(logFileLocation);
                Files.createFile(currentLogFilePath());
                deleteOldFileIfNeeded(logFileLocation, log, currentLogFileNumber - 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void appendLog(Metadata metadata, ETypeCommand commandType) {
        appendInformation(metadata, commandType, currentLogFilePath());
    }

    public LinkedHashMap<Long, Metadata> recoverInformation() {
        currentLogFileNumber = getCurrentFileNumber(logFileLocation, serverId, log);
        currentSnapshotFileNumber = getCurrentFileNumber(logFileLocation, serverId, snapshot);

        LinkedHashMap<Long, Metadata> itemsFromLog = recoverLogInformation(currentLogFilePath());
        LinkedHashMap<Long, Metadata> itemsFromSnapShot = recoverSnapShotInformation(currentSnapshotFilePath());

        itemsFromSnapShot.putAll(itemsFromLog);
        return itemsFromSnapShot;
    }

    private LinkedHashMap<Long, Metadata> recoverSnapShotInformation(Path filepath) {
        LinkedHashMap<Long, Metadata> objects = new LinkedHashMap<>();
        List<String> snapshotedObjects = new ArrayList<>();
        snapshotedObjects = getLines(snapshotedObjects, filepath);

        if (!snapshotedObjects.isEmpty()) {
            List<Metadata> metadataList = snapshotedObjects.stream().map(Metadata::fromLogString).collect(toList());
            metadataList.forEach(s -> objects.put(s.getId(), s));
            return objects;
        }
        return objects;
    }

    private LinkedHashMap<Long, Metadata> recoverLogInformation(Path filePath) {
        List<String> loggedOperations = new ArrayList<>();
        List<Long> removedItems = new ArrayList<>();
        loggedOperations = getLines(loggedOperations, filePath);

        if (!loggedOperations.isEmpty()) {
            List<Metadata> metadataList = loggedOperations.stream().map(Metadata::fromLogString).collect(toList());
            loggedOperations.forEach(s -> findRemovedObjects(s, removedItems));
            return mergeInformation(metadataList, removedItems);
        }
        return new LinkedHashMap<>();
    }

    public void snapshot() {
        if (Files.exists(currentLogFilePath())) {
            logger.info("creating new snapshot file: " + currentSnapshotFilePath().toString());
            LinkedHashMap<Long, Metadata> currentLogState = recoverLogInformation(currentLogFilePath());
            if (!currentLogState.isEmpty()) {
                createSnapshot(currentLogState);
                currentSnapshotFileNumber++;
                deleteOldFileIfNeeded(logFileLocation, snapshot, currentLogFileNumber - 3);
                currentLogFileNumber++;
                createLogFile();
            } else {
                logger.info("no snapshot creation needed, current log file is empty");
            }
        }
    }

    private void appendSnapshotInformation(Metadata metadata, ETypeCommand commandType) {
        appendInformation(metadata, commandType, currentSnapshotFilePath());
    }

    private void appendInformation(Metadata metadata, ETypeCommand commandType, Path filePath) {
        try {
            Files.write(filePath, getMetadataAsWritableString(metadata, commandType).getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getLines(List<String> loggedOperations, Path filePath) {
        if (Files.exists(filePath)) {
            try {
                loggedOperations = Files.readAllLines(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loggedOperations;
        }
        System.out.println("file with path: " + filePath.toString() + " requested to recover information does not exist");
        return new ArrayList<>();
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
        if (DELETE.name().equals(log.get(6))) {
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
        if (m.getCreatedAt() != null) {
            current.setCreatedAt(m.getCreatedAt());
        }
        if (m.getCreatedBy() != null) {
            current.setCreatedBy(m.getCreatedBy());
        }
    }

    private Path previousSnapshotFilePath() {
        return Paths.get(resolveCurrentPath(logFileLocation, "snapshot", currentSnapshotFileNumber - 1));
    }

    private Path currentSnapshotFilePath() {
        return Paths.get(resolveCurrentPath(logFileLocation, "snapshot", currentSnapshotFileNumber));
    }

    private Path currentLogFilePath() {
        return Paths.get(resolveCurrentPath(logFileLocation, "log", currentLogFileNumber));
    }

    //use on all filePath calls
    private String resolveCurrentPath(String filePath, String fileType, int current) {
        return String.format("%s[server-%s]%s-%s.%s", filePath, serverId, fileType, current, "log");
    }

    private void deleteOldFileIfNeeded(String fileLocation, String fileType, int fileNumber) {
        Path fileTobeDeleted = Paths.get(resolveCurrentPath(fileLocation, fileType, fileNumber));
        if (Files.exists(fileTobeDeleted)) {
            logger.info("deleting old " + fileType + "file: " + fileTobeDeleted.toString());
            try {
                Files.delete(fileTobeDeleted);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Integer getCurrentFileNumber(String fileLocation, long serverId, String fileType) {
        List<Path> files = new ArrayList<>();
        try {
            files = Files.list(Paths.get(fileLocation))
                    .filter(s -> s.toString().endsWith(".log"))
                    .filter(s -> s.toString().contains(fileType))
                    .collect(toList());

        } catch (IOException e) {
            // e.printStackTrace();
        }
        if (!files.isEmpty()) {
            List<Integer> fileNumbers = files.stream().map(s -> getCurrent(logFileLocation, serverId, fileType, s.toString())).collect(toList());
            return Collections.max(fileNumbers);
        }
        return 0;
    }

    private Integer getCurrent(String fileLocation, long serverId, String fileType, String filePath) {
        Matcher m = Pattern.compile(String.format("%s\\[server-%s]%s-(\\d+)\\.log", fileLocation, serverId, fileType)).matcher(filePath);
        if (m.matches()) {
            return Integer.valueOf(m.group(1));
        }
        return 0;
    }

    private void createSnapshot(LinkedHashMap<Long, Metadata> currentLogState) {
        Path currentSnapshotFileName = currentSnapshotFilePath();
        try {
            Files.createFile(currentSnapshotFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinkedHashMap<Long, Metadata> previousSnapshotState = recoverSnapShotInformation(previousSnapshotFilePath());
        previousSnapshotState.putAll(currentLogState);
        previousSnapshotState.forEach((k, v) -> appendSnapshotInformation(v, SNAPSHOT));
    }

    private void createDir(String path) {
        if (!Files.isDirectory(Paths.get(path))) {
            logger.info("creating new directory to keep log and snapshot files");
            try {
                Files.createDirectories(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("log and snapshot files directory already exists, skipping...");
        }
    }

}
