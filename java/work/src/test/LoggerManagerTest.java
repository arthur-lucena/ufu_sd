import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class LoggerManagerTest {

    private String filePath = "src/test/log.txt";
    private String message = "hello you";
    private String message1 = "update you";
    private String message2 = "update you again";
    private String createdBy = "process_create";
    private String updatedBy = "process_update";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now().plusMinutes(1);

    @Before
    public void setUp() {
        deleteFile();
    }

    @Test
    public void should_create_log_file() {
        LogManager logManager = new LogManager(filePath);
        logManager.createFile();

        Assert.assertTrue(Files.exists(Paths.get(filePath)));
        deleteFile();
    }

    @Test
    public void should_log_metadata() {

        Metadata metadata = new Metadata(1L , message, createdBy, createdAt, createdBy, updatedAt);
        LogManager logManager = new LogManager(filePath);
        logManager.createFile();
        logManager.append(metadata, ETypeCommand.INSERT);
        LinkedHashMap<Long, Metadata> metadataMap = logManager.read();
        Assert.assertEquals(1, metadataMap.size());
        Assert.assertEquals(message, metadataMap.get(1L).getMessage());
        Assert.assertEquals(createdBy, metadataMap.get(1L).getCreatedBy());
        Assert.assertEquals(createdAt, metadataMap.get(1L).getCreatedAt());
        Assert.assertEquals(createdBy, metadataMap.get(1L).getUpdatedBy());
        Assert.assertEquals(updatedAt, metadataMap.get(1L).getUpdatedAt());

        deleteFile();
    }

    @Test
    public void should_retrieve_updated_info_from_log() {

        Metadata metadata = new Metadata(1L , message, createdBy, createdAt, createdBy, updatedAt);
        Metadata metadata1 = new Metadata(1L , message1, null, null, updatedBy, updatedAt.plusSeconds(1));
        Metadata metadata2 = new Metadata(2L , message, createdBy, createdAt.plusSeconds(2), createdBy, updatedAt.plusSeconds(2));
        Metadata metadata3 = new Metadata(2L , null, null, null, updatedBy, updatedAt.plusSeconds(3));
        Metadata metadata4 = new Metadata(3L , message, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));
        Metadata metadata5 = new Metadata(1L , message2, createdBy, createdAt.plusSeconds(4), createdBy, updatedAt.plusSeconds(4));

        LogManager logManager = new LogManager(filePath);
        logManager.createFile();
        logManager.append(metadata, ETypeCommand.INSERT);
        logManager.append(metadata2, ETypeCommand.INSERT);
        logManager.append(metadata1, ETypeCommand.UPDATE);
        logManager.append(metadata3, ETypeCommand.DELETE);
        logManager.append(metadata4, ETypeCommand.INSERT);
        logManager.append(metadata5, ETypeCommand.UPDATE);

        LinkedHashMap<Long, Metadata> metadataMap = logManager.read();
        Assert.assertEquals(2, metadataMap.size());
        Assert.assertEquals(message2, metadataMap.get(1L).getMessage());
        Assert.assertEquals(null, metadataMap.get(2L));
        Assert.assertEquals(message, metadataMap.get(3L).getMessage());

        deleteFile();
    }

    private void deleteFile() {
        try {
            if(Files.exists(Paths.get(filePath))) {
                Files.delete(Paths.get(filePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
