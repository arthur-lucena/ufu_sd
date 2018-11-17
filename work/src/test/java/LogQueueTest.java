import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class LogQueueTest {

//    BlockingQueue<MessageCommand> logQueue;
//    LogManager logManager;
//    LogQueueConsumption logQueueConsumption;
//    private String filePath = "src/test/log.txt";
//    private String[] insert_args = {"test_data"};
//    private String[] update_args = {"1", "updated_data"};
//    private String[] delete_args = {"1"};

    @Before
    public void setup() {
//        logQueue = new ArrayBlockingQueue<>(10);
//        deleteFile();
//        logManager = new LogManager(filePath);
//        logManager.createFile();
//        logQueueConsumption = new LogQueueConsumption(logQueue, logManager);
    }

    @Test
    public void should_consume_from_log_queue_and_write_on_log_file() throws InterruptedException {
//        TODO AJUSTAR
//        logQueue.add(new MessageCommand(ETypeCommand.INSERT, new Insert(), insert_args, 1L, 100, LocalDateTime.now(), false, null));
//        logQueue.add(new MessageCommand(ETypeCommand.UPDATE, new Insert(), update_args, 1L, 101, LocalDateTime.now(), false, null));
//        logQueue.add(new MessageCommand(ETypeCommand.DELETE, new Insert(), update_args, 2L, 101, LocalDateTime.now(), false, null));
//
//        Thread t2 = new Thread(logQueueConsumption);
//        t2.start();
//
//        Thread.sleep(500);
//        LinkedHashMap<Long, Metadata> data = logManager.read();
//
//        Assert.assertTrue(logQueue.isEmpty());
//        Assert.assertEquals(1, data.size());
//
//        deleteFile();
    }


    private void deleteFile() {
//        try {
//            if (Files.exists(Paths.get(filePath))) {
//                Files.delete(Paths.get(filePath));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
