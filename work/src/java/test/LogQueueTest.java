import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.LogQueueConsumption;
import br.ufu.sd.work.util.MessageCommand;
import br.ufu.sd.work.util.commands.Insert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LogQueueTest {

    private String filePath = "src/test/log.txt";
    private String[] insert_args = {"test_data"};
    private String[] update_args = {"1", "updated_data"};
    private String[] delete_args = {"1"};

    BlockingQueue<MessageCommand> logQueue;
    LogManager logManager;
    LogQueueConsumption logQueueConsumption;

    @Before
    public void setup() {
        logQueue = new ArrayBlockingQueue<>(10);
        deleteFile();
        logManager = new LogManager(filePath);
        logManager.createFile();
        logQueueConsumption = new LogQueueConsumption(logQueue, logManager);
    }

    @Test
    public void should_consume_from_log_queue_and_write_on_log_file() throws InterruptedException {

        logQueue.add(new MessageCommand(ETypeCommand.INSERT, new Insert(), insert_args, 1L, 100, LocalDateTime.now(), false, null));
        logQueue.add(new MessageCommand(ETypeCommand.UPDATE, new Insert(), update_args, 1L, 101, LocalDateTime.now(), false, null));
        logQueue.add(new MessageCommand(ETypeCommand.DELETE, new Insert(), update_args, 2L, 101, LocalDateTime.now(), false, null));

        Thread t2 = new Thread(logQueueConsumption);
        t2.start();

        Thread.sleep(500);
        LinkedHashMap<Long, Metadata> data = logManager.read();

        Assert.assertTrue(logQueue.isEmpty());
        Assert.assertEquals(1, data.size());

        deleteFile();
    }


    private void deleteFile() {
        try {
            if (Files.exists(Paths.get(filePath))) {
                Files.delete(Paths.get(filePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
