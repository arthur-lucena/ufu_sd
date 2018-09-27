import br.ufu.sd.work.client.CommandReceiver;
import br.ufu.sd.work.client.CommandSender;
import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Created by ismaley on 25/09/18.
 */
public class TestClient {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 61666;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private CommandSender sender;
    private TestCommandReceiver receiver;
    private List<String> responseLog;

    public TestClient(List<String> responseLog) {
        this.responseLog = responseLog;
    }

    public void start() {
        createConnection();

        sender = new CommandSender(outToServer);
        receiver = new TestCommandReceiver(inFromServer, responseLog);
        Thread commandReceiverThread = new Thread(receiver);
        commandReceiverThread.start();
    }

    public void sendCommand(MessageCommand command) throws IOException, ClassNotFoundException {
        sender.send(command.getTypeCommand().getName(), command.getArgs());
    }

    public void stop() {
        receiver.terminate();
        try {
            inFromServer.close();
            outToServer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConnection() {
        try {
            clientSocket = new Socket(IP, PORT);
            outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
