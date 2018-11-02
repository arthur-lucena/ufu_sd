import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.MessageCommand;
import br.ufu.sd.work.util.commands.Insert;
import com.sun.xml.internal.ws.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static br.ufu.sd.work.model.ETypeCommand.INSERT;
import static br.ufu.sd.work.model.ETypeCommand.SELECT;

/**
 * Created by ismaley on 25/09/18.
 */
public class InsertionSequenceTest {

    private String serverLogFilePath = "src/main/br/ufu/sd/work/log/log.txt";

    @Test
    public void should_insert_with_sequence() throws IOException, ClassNotFoundException, InterruptedException {
        List<String> responses = new ArrayList<>();
        boolean wait = true;
        TestClient client = new TestClient(responses);

        while (wait) {
            client.start();
            int i;
            for(i = 1; i<=1000; i++) {
                client.sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, INSERT));
            }
            i--;
            client.sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, SELECT));
            Thread.sleep(6000);
            if(responses.get(i).contains("object with Id: " + i)) {
                i++;
                client.sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, INSERT));
                client.sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, SELECT));
                Thread.sleep(1000);
            }
//            client.stop();
            i++;
            Assert.assertTrue(responses.get(i).contains("object with Id: 1001"));
            wait = false;
            deleteFile();
        }

    }

    @Test
    public void should_insert_with_sequence_concurrently() throws IOException, ClassNotFoundException, InterruptedException {
        List<String> responsesClient1 = new ArrayList<>();
        List<String> responsesClient2 = new ArrayList<>();
        boolean wait = true;
        TestClient client1 = new TestClient(responsesClient1);
        TestClient client2 = new TestClient(responsesClient2);
        List<TestClient> clients = Arrays.asList(client1, client2);
        Random rand = new Random();

        while (wait) {
            client1.start();
            client2.start();
            int i;
            for(i = 1; i<=1000; i++) {
                getRandomClient(rand, clients).sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, INSERT));
            }
            i--;
            getRandomClient(rand, clients).sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, SELECT));
            Thread.sleep(6000);
//            if(responses.get(i).contains("object with Id: " + i)) {
//                i++;
//                getRandomClient(rand, clients).sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, INSERT));
//                getRandomClient(rand, clients).sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, SELECT));
//                Thread.sleep(1000);
//            }
//            client.stop();
            i++;
//            Assert.assertTrue(responses.get(i).contains("object with Id: 1001"));
            wait = false;
            deleteFile();
        }

    }

    private void deleteFile() {
        try {
            if (Files.exists(Paths.get(serverLogFilePath))) {
                Files.delete(Paths.get(serverLogFilePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MessageCommand createCommand (Long id, String[] args, ETypeCommand typeCommand) {
        return new MessageCommand(typeCommand, null, args,
                id, 101, LocalDateTime.now(), false, null);
    }

    private TestClient getRandomClient(Random random, List<TestClient> clients) {
        return clients.get(random.nextInt(clients.size()));
    }

}
