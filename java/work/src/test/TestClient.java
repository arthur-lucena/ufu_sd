import br.ufu.sd.work.client.Client;
import br.ufu.sd.work.client.CommandReceiver;
import br.ufu.sd.work.client.CommandSender;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ismaley on 25/09/18.
 */
public class TestClient {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 61666;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private MessageCommand currentCommand;

    public static void main(String[] args) {
        new TestClient().start();
    }

    public void start() {
        createConnection();
        boolean running = true;

        CommandSender cs = new CommandSender(outToServer);
        CommandReceiver runnable = new CommandReceiver(inFromServer);
        Thread commandReceiverThread = new Thread(runnable);
        commandReceiverThread.start();

        while (running) {

            if (ETypeCommand.INSERT.equals(currentCommand.getTypeCommand()) ||
                    ETypeCommand.UPDATE.equals(currentCommand.getTypeCommand()) ||
                    ETypeCommand.DELETE.equals(currentCommand.getTypeCommand()) ||
                    ETypeCommand.SELECT.equals(currentCommand.getTypeCommand())) {
                cs.send(currentCommand.getTypeCommand().getName(), currentCommand.getArgs());
            } else if (ETypeCommand.EXIT.equals(currentCommand.getTypeCommand())) {
                running = false;

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runnable.terminate();

                try {
                    commandReceiverThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    inFromServer.close();
                    outToServer.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                System.out.println("commando invalido");
            }
        }

        System.out.println("saindo... =D");
    }

    public void sendCommand(MessageCommand command) {
        currentCommand = command;
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
