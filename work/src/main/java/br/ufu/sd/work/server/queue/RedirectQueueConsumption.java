package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.client.Client;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.commands.api.ICommand;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class RedirectQueueConsumption implements Runnable {

    private static final Logger logger = Logger.getLogger(RedirectQueueConsumption.class.getName());

    private BlockingQueue<ResponseCommand> redirectQueue;
    private ResponseCommand responseCommand;
    private volatile ChordNode node;
    private volatile boolean running = true;
    private Client nextNode;
    private Client previousNode;
    private int delayCommand;

    public RedirectQueueConsumption(BlockingQueue<ResponseCommand> redirectQueue, ChordNode node, int delayCommand) {
        this.redirectQueue = redirectQueue;
        this.node = node;
        this.delayCommand = delayCommand;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (redirectQueue.isEmpty()) {
                waitNewCommand();
                continue;
            }

            consumeCommand();
        }
    }

    private void consumeCommand() {
        try {
            Client clientRedirect;

            if (nextNode == null && !node.isFirstNode()) {
                nextNode = new Client(node.getIpNext(), node.getPortNext());
            }

            if (previousNode == null && !node.isLastNode()) {
                if (node.getIpPrevious() != null) {
                    previousNode = new Client(node.getIpPrevious(), node.getPortPrevious());
                }
            }

            responseCommand = redirectQueue.take();

            ICommand command = responseCommand.getCommand();

            int direction =  getPossibleRedirection(node, command.getIdRequest());

            if (direction > 0) {
                clientRedirect = nextNode;
            } else if (direction < 0) {
                clientRedirect = previousNode;
            } else {
                logger.warning("HOW U CAME HERE!??!?!");
                return;
            }

            StreamObserver sos = new StreamObserverServer(responseCommand.getStreamObserver());

            clientRedirect.sendCommand(command.getRequest(), command.getTypeCommand(), sos);

            logger.info("Command " + command.getTypeCommand() + " redirect : " + command.getRequest().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitNewCommand() {
        try {
            Thread.sleep(delayCommand);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getPossibleRedirection(ChordNode node, Long id) {
        if (id <= node.getMinId() && !node.isLastNode()) {
            return -1;
        } else if (id > node.getMaxId()) {
            return 1;
        } else {
            return 0;
        }
    }
}
