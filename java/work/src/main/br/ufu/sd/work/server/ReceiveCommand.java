package br.ufu.sd.work.server;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.util.MessageCommand;
import br.ufu.sd.work.util.commands.Delete;
import br.ufu.sd.work.util.commands.Insert;
import br.ufu.sd.work.util.commands.Select;
import br.ufu.sd.work.util.commands.Update;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class ReceiveCommand implements Runnable {
    private BlockingQueue<OutputStreamCommand> queue;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private volatile boolean running = true;

    public ReceiveCommand(ObjectInputStream inFromClient, ObjectOutputStream outToClient,
                          BlockingQueue<OutputStreamCommand> queue) {
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        this.queue = queue;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                MessageCommand messageCommand = (MessageCommand) inFromClient.readObject();

                switch (messageCommand.getTypeCommand()) {
                    case INSERT :
                        messageCommand.setCommand(new Insert());
                        break;
                    case UPDATE :
                        messageCommand.setCommand(new Update());
                        break;
                    case DELETE :
                        messageCommand.setCommand(new Delete());
                        break;
                    case SELECT :
                        messageCommand.setCommand(new Select());
                        break;
                    default:
                        // TODO tratar?
                        break;
                }

                queue.add(new OutputStreamCommand(outToClient, messageCommand));
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                running = false;
            }
        }

        return;
    }
}
