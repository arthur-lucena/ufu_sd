package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.chord.ChordException;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.commands.api.ICommand;
import br.ufu.sd.work.server.log.LogManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class QueueOneConsumption implements Runnable {

    private static final Logger logger = Logger.getLogger(QueueOneConsumption.class.getName());

    private BlockingQueue<ResponseCommand> queueOne;
    private BlockingQueue<ICommand> logQueue;
    private BlockingQueue<ResponseCommand> executionQueue;
    private BlockingQueue<ResponseCommand> redirectQueue;
    private ResponseCommand responseCommand;
    private Dictionary dictionary;
    private LogManager logManager;
    private volatile ChordNode node;
    private long maxNodeId;
    private long minNodeId;

    private volatile boolean running = true;

    private int delayCommand;
    private int delayLog;

    public QueueOneConsumption(BlockingQueue<ResponseCommand> queueOne, Dictionary dictionary,
                               LogManager logManager, ChordNode node, int delayCommand, int delayLog) {
        this.queueOne = queueOne;
        this.dictionary = dictionary;
        this.logManager = logManager;
        this.node = node;
        this.delayCommand = delayCommand;
        this.delayLog = delayLog;

        this.maxNodeId = node.getOffSetId() * node.getNodeId();
        this.minNodeId = node.getOffSetId() * (node.getNodeId() - 1);
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        executionQueue = new ArrayBlockingQueue<>(1000000);
        logQueue = new ArrayBlockingQueue<>(1000000);
        redirectQueue = new ArrayBlockingQueue<>(1000000);

        ExecutionQueueConsumption executionQueueConsumption = new ExecutionQueueConsumption(executionQueue, dictionary, delayCommand);
        Thread threadStarter1 = new Thread(executionQueueConsumption);
        threadStarter1.start();

        LogQueueConsumption logQueueConsumption = new LogQueueConsumption(logQueue, logManager, delayLog);
        Thread threadStarter2 = new Thread(logQueueConsumption);
        threadStarter2.start();

        RedirectQueueConsumption redirectQueueConsumption = new RedirectQueueConsumption(redirectQueue, node, delayCommand);
        Thread threadStarter3 = new Thread(redirectQueueConsumption);
        threadStarter3.start();

        int counterSleep = 0;
        int limitSleepLog = 50;

        while (running) {
            if (queueOne.isEmpty()) {
                waitNewCommand();

                counterSleep++;

                if (counterSleep >= limitSleepLog) {
                    logger.info("no command queued...");
                    counterSleep = 0;
                }

                continue;
            }

            consumeCommand();
        }
    }

    private void consumeCommand() {
        try {
            responseCommand = queueOne.take();

            if (isMyResponsibility(node, responseCommand.getCommand().getIdRequest())) {
                executionQueue.add(responseCommand);
                logQueue.add(responseCommand.getCommand());
            } else {
                redirectQueue.add(responseCommand);
            }
        } catch (ChordException e) {
            logger.warning(e.getMessage());
            responseCommand.getStreamObserver().onError(e);
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

    private boolean isMyResponsibility(ChordNode node, long id) throws ChordException {
        if (id > node.getMaxId()) {
            throw new ChordException("Invalid ID, this ID surpass MAX capacity.");
        }

        if (id < 0) {
            throw new ChordException("Invalid ID, can be below Zero.");
        }

        return (this.minNodeId < id && id <= this.maxNodeId) || (id == 0 && node.isLastNode());
    }
}
