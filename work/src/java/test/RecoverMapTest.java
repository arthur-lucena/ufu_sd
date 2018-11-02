import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.server.Server;
import br.ufu.sd.work.util.MessageCommand;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.ufu.sd.work.model.ETypeCommand.INSERT;
import static br.ufu.sd.work.model.ETypeCommand.SELECT;

/**
 * Created by ismaley on 27/09/18.
 */
public class RecoverMapTest {

    private String serverLogFilePath = "src/main/br/ufu/sd/work/log/log.txt";

    @Test
    public void dictionary_recover_test() throws IOException, ClassNotFoundException, InterruptedException {

        Server server = new Server();
        List<String> responseLog = new ArrayList<>();
        TestClient client = new TestClient(responseLog);

        Thread serverThread = new Thread(() -> server.start(61666));
        serverThread.start();
        Thread.sleep(500);

        startClientAndSendCommands(client, INSERT);
        serverThread.interrupt();
        Thread.sleep(500);

        Thread serverThread2 = new Thread(() -> server.start(61666));
        serverThread2.start();
        Thread.sleep(500);

        startClientAndSendCommands(client, SELECT);
        startClientAndSendCommands(client, INSERT);



    }

    private void startClientAndSendCommands(TestClient client, ETypeCommand command) throws IOException, ClassNotFoundException, InterruptedException {
        boolean wait = true;
        while (wait) {
            client.start();
            int i;
            for (i = 1; i <= 5; i++) {
                client.sendCommand(createCommand((long) i, new String[]{String.valueOf(i), String.valueOf(i)}, command));
            }
            Thread.sleep(6000);
            wait = false;
//            client.stop();
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

}
