import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LoggerManagerTest {

    private String filePath = "src/test/";
    private String expectedLogFileName = "[server-1]log-0.log";
    private String expectedSnapFileName = "[server-1]snapshot-0.log";
    private Integer serverId = 1;
    private String message = "hello you";
    private String message1 = "update you";
    private String message2 = "update you again";
    private String createdBy = "process_create";
    private String updatedBy = "process_update";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now().plusMinutes(1);

    @After
    @Before
    public void clean() {
        deleteTestFiles(filePath);
    }

    @Test
    public void should_create_log_file() {
        LogManager logManager = new LogManager(filePath, serverId);
        logManager.createLogFile();

        Assert.assertTrue(Files.exists(Paths.get(filePath+expectedLogFileName)));
    }

    @Test
    public void should_log_metadata() {

        Metadata metadata = new Metadata(1L, message, createdBy, createdAt, createdBy, updatedAt);
        LogManager logManager = new LogManager(filePath, serverId);
        logManager.createLogFile();
        logManager.appendLog(metadata, ETypeCommand.INSERT);
        LinkedHashMap<Long, Metadata> metadataMap = logManager.recoverInformation();
        Assert.assertEquals(1, metadataMap.size());
        Assert.assertEquals(message, metadataMap.get(1L).getMessage());
        Assert.assertEquals(createdBy, metadataMap.get(1L).getCreatedBy());
        Assert.assertEquals(createdAt, metadataMap.get(1L).getCreatedAt());
        Assert.assertEquals(createdBy, metadataMap.get(1L).getUpdatedBy());
        Assert.assertEquals(updatedAt, metadataMap.get(1L).getUpdatedAt());

    }

    @Test
    public void should_retrieve_updated_info_from_log() {

        Metadata metadata = new Metadata(1L, message, createdBy, createdAt, createdBy, updatedAt);
        Metadata metadata1 = new Metadata(1L, message1, null, null, updatedBy, updatedAt.plusSeconds(1));
        Metadata metadata2 = new Metadata(2L, message, createdBy, createdAt.plusSeconds(2), createdBy, updatedAt.plusSeconds(2));
        Metadata metadata3 = new Metadata(2L, null, null, null, updatedBy, updatedAt.plusSeconds(3));
        Metadata metadata4 = new Metadata(3L, message, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));
        Metadata metadata5 = new Metadata(1L, message2, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));

        LogManager logManager = new LogManager(filePath, serverId);
        logManager.createLogFile();
        logManager.appendLog(metadata, ETypeCommand.INSERT);
        logManager.appendLog(metadata2, ETypeCommand.INSERT);
        logManager.appendLog(metadata1, ETypeCommand.UPDATE);
        logManager.appendLog(metadata3, ETypeCommand.DELETE);
        logManager.appendLog(metadata4, ETypeCommand.INSERT);
        logManager.appendLog(metadata5, ETypeCommand.UPDATE);

        LinkedHashMap<Long, Metadata> metadataMap = logManager.recoverInformation();
        Assert.assertEquals(2, metadataMap.size());
        Assert.assertEquals(message2, metadataMap.get(1L).getMessage());
        Assert.assertEquals(null, metadataMap.get(2L));
        Assert.assertEquals(message, metadataMap.get(3L).getMessage());

    }

    @Test
    public void should_create_snapshot_file() {
        Metadata metadata = new Metadata(1L, message, createdBy, createdAt, createdBy, updatedAt);
        LogManager logManager = new LogManager(filePath, serverId);
        logManager.createLogFile();
        logManager.appendLog(metadata, ETypeCommand.INSERT);

        logManager.snapshot();

        Assert.assertEquals(1, (int) logManager.getCurrentCount().get("log"));
        Assert.assertEquals(1, (int) logManager.getCurrentCount().get("snapshot"));
        Assert.assertTrue(Files.exists(Paths.get(filePath+expectedSnapFileName)));

    }

    @Test
    public void should_not_create_snapshot_file_if_current_log_is_empty() {
        LogManager logManager = new LogManager(filePath, serverId);
        logManager.createLogFile();
        logManager.snapshot();

        Assert.assertEquals(0, (int) logManager.getCurrentCount().get("log"));
        Assert.assertEquals(0, (int) logManager.getCurrentCount().get("snapshot"));
        Assert.assertFalse(Files.exists(Paths.get(filePath+expectedSnapFileName)));

    }

    @Test
    public void should_retrieve_updated_info_from_log_and_snapshot() {

        Metadata metadata = new Metadata(1L, message, createdBy, createdAt, createdBy, updatedAt);
        Metadata metadata1 = new Metadata(1L, message1, null, null, updatedBy, updatedAt.plusSeconds(1));
        Metadata metadata2 = new Metadata(2L, message, createdBy, createdAt.plusSeconds(2), createdBy, updatedAt.plusSeconds(2));
        Metadata metadata3 = new Metadata(2L, null, null, null, updatedBy, updatedAt.plusSeconds(3));
        Metadata metadata4 = new Metadata(3L, message, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));
        Metadata metadata5 = new Metadata(1L, message2, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));
        Metadata metadata6 = new Metadata(3L, message2, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));
        Metadata metadata7 = new Metadata(4L, message, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));

        LogManager logManager = new LogManager(filePath, serverId);
        logManager.createLogFile();
        logManager.appendLog(metadata, ETypeCommand.INSERT);
        logManager.appendLog(metadata2, ETypeCommand.INSERT);
        logManager.appendLog(metadata1, ETypeCommand.UPDATE);
        logManager.appendLog(metadata3, ETypeCommand.DELETE);
        logManager.appendLog(metadata4, ETypeCommand.INSERT);
        logManager.appendLog(metadata5, ETypeCommand.UPDATE);

        logManager.snapshot();

        logManager.appendLog(metadata6, ETypeCommand.UPDATE);
        logManager.appendLog(metadata7, ETypeCommand.INSERT);

        logManager.snapshot();

        LinkedHashMap<Long, Metadata> metadataMap = logManager.recoverInformation();
        Assert.assertEquals(3, metadataMap.size());
        Assert.assertEquals(message2, metadataMap.get(1L).getMessage());
        Assert.assertEquals(null, metadataMap.get(2L));
        Assert.assertEquals(message2, metadataMap.get(3L).getMessage());
        Assert.assertEquals(message, metadataMap.get(4L).getMessage());

    }

    private void deleteTestFiles(String filePath) {
        try {
            List<Path> files = Files.list(Paths.get(filePath))
                    .filter(s -> s.toString().endsWith(".log"))
                    .collect(toList());

            files.forEach(f -> {
                if(Files.exists(f)) {
                    try {
                        Files.delete(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
