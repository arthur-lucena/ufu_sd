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

    public void append(Metadata metadata, ETypeCommand commandType) {
        try {
            Files.write(filePath, getMetadataAsWritableString(metadata, commandType).getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedHashMap<Long, Metadata> read() {
        List<String> loggedOperations = new ArrayList<>();
        List<Long> removedItems = new ArrayList<>();
        loggedOperations = getLines(loggedOperations);

        if(!loggedOperations.isEmpty()) {
            List<Metadata> metadataList = loggedOperations.stream().map(Metadata::fromLogString).collect(toList());
             loggedOperations.forEach(s -> findRemovedObjects(s, removedItems));
            return mergeInformation(metadataList, removedItems);
        }
        return new LinkedHashMap<>();
    }

    private List<String> getLines(List<String> loggedOperations) {
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
}
