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
    private long firstNode;
    private long offSetId;
    private long[] mapIdNode;

    public RedirectQueueConsumption(BlockingQueue<ResponseCommand> redirectQueue, ChordNode node, int delayCommand) {
        this.redirectQueue = redirectQueue;
        this.node = node;
        this.delayCommand = delayCommand;

        this.firstNode = node.getMaxId();
        this.offSetId = node.getOffSetId();

        this.mapIdNode = new long[node.getNumberOfNodes()+1];

        int count = node.getNumberOfNodes();

        while (count > 0) {
            mapIdNode[count] = firstNode;
            firstNode = firstNode - offSetId;
            count--;
        }

        count = node.getNumberOfNodes();
        while (count >= 0) {
            System.out.print(mapIdNode[count]);
            count--;

            if (count >= 0) {
                System.out.print(":");
            }
        }
        System.out.println();
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

            if (nextNode == null) {
                nextNode = new Client(node.getIpNext(), node.getPortNext());
            }

            if (previousNode == null) {
                if (node.getIpPrevious() != null) {
                    previousNode = new Client(node.getIpPrevious(), node.getPortPrevious());
                }
            }

            responseCommand = redirectQueue.take();

            ICommand command = responseCommand.getCommand();

            Boolean clockwise = getPossibleRedirection(node, command.getIdRequest());

            if (clockwise) {
                clientRedirect = nextNode;
            } else {
                clientRedirect = previousNode;
            }

            StreamObserver sos = new StreamObserverServer(responseCommand.getStreamObserver());

            clientRedirect.sendCommand(command.getRequest(), command.getTypeCommand(), sos);

            logger.info("Command " + command.getTypeCommand() + " redirect to " + (clockwise ? "next" : "previous") + " node - " + command.getRequest().toString());
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

    public boolean getPossibleRedirection(ChordNode node, long id) {
        return minDistanceOnRing(node.getNodeId(), getDestNode(id), node.getNumberOfNodes());
    }

    private int getDestNode(long id) {
        int curNode = node.getNumberOfNodes();
        int prevNode = curNode - 1;

        while (curNode >= 0) {
            if ((mapIdNode[prevNode] < id && id <= mapIdNode[curNode]) || mapIdNode[prevNode] == 0) {
                return curNode;
            } else {
                curNode--;
                prevNode--;
            }
        }

        return -1;
    }

    public static boolean minDistanceOnRing(int startNode, int destineNode, int ringCirc) {
        int distance = startNode - destineNode;
        boolean direction = distance >= 0;

        distance = Math.abs(distance);

        if (distance <= (ringCirc - distance)) {
            return !direction;
        } else {
            return direction;
        }
    }
}
