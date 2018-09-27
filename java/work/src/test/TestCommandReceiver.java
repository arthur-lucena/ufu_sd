import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class TestCommandReceiver implements Runnable {

    private ObjectInputStream inFromServer;
    private List<String> responses;

    public TestCommandReceiver(ObjectInputStream inFromServer, List<String> responses) {
        this.inFromServer = inFromServer;
        this.responses = responses;
    }

    private volatile boolean running = true;

    public void terminate() {
        running = false;
        try {
            inFromServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object objectReceived = inFromServer.readObject();

                if (objectReceived != null) {
                    MessageCommand messageCommand = (MessageCommand) objectReceived;

                    if (messageCommand.getCommand() == null) {
                        System.out.println("Conectado!");
                    } else {
                        if (messageCommand.getResponse() != null) {
                            System.out.println(messageCommand.getResponse());
                            responses.add(messageCommand.getResponse());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }

    public List<String> getResponses() {
        return responses;
    }
}
