import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Metadata;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class LoggerManagerTest {

    private String filePath = "src/test/log.txt";
    private String message = "hello you";
    private String createdBy = "process_create";
    private String updatedBy = "process_update";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now().plusMinutes(1);


    @Test
    public void should_create_log_file() {
        LogManager logManager = new LogManager(filePath);
        logManager.createFile();

        Assert.assertTrue(Files.exists(Paths.get(filePath)));
        deleteFile();
    }

    @Test
    public void should_log_metadata() {

        Metadata metadata = new Metadata(message, createdBy, createdAt, updatedBy, updatedAt);
        LogManager logManager = new LogManager(filePath);
        logManager.createFile();
        logManager.append(metadata);
        List<Metadata> metadataList = logManager.read();
        Assert.assertEquals(1, metadataList.size());
        Assert.assertEquals(message, metadataList.get(0).getMessage());
        Assert.assertEquals(createdBy, metadataList.get(0).getCreatedBy());
        Assert.assertEquals(createdAt, metadataList.get(0).getCreatedAt());
        Assert.assertEquals(updatedBy, metadataList.get(0).getUpdatedBy());
        Assert.assertEquals(updatedAt, metadataList.get(0).getUpdatedAt());

        deleteFile();

    }

    private void deleteFile() {
        try {
            Files.delete(Paths.get(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
