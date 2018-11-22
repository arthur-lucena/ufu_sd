package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordNodeUtils;
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
    private BlockingQueue<ResponseCommand> repassQueue;
    private ResponseCommand responseCommand;
    private Dictionary dictionary;
    private LogManager logManager;
    private ChordNode node;

    private volatile boolean running = true;


    public QueueOneConsumption(BlockingQueue<ResponseCommand> queueOne, Dictionary dictionary,
                               LogManager logManager, ChordNode node) {
        this.queueOne = queueOne;
        this.dictionary = dictionary;
        this.logManager = logManager;
        this.node = node;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        executionQueue = new ArrayBlockingQueue<>(1000000);
        logQueue = new ArrayBlockingQueue<>(1000000);
        repassQueue = new ArrayBlockingQueue<>(1000000);

        ExecutionQueueConsumption executionQueueConsumption = new ExecutionQueueConsumption(executionQueue, dictionary);
        Thread threadStarter1 = new Thread(executionQueueConsumption);
        threadStarter1.start();

        LogQueueConsumption logQueueConsumption = new LogQueueConsumption(logQueue, logManager);
        Thread threadStarter2 = new Thread(logQueueConsumption);
        threadStarter2.start();

        RepassQueueConsumption repassQueueConsumption = new RepassQueueConsumption(repassQueue, node);
        Thread threadStarter3 = new Thread(repassQueueConsumption);
        threadStarter3.start();

        int counterSleep = 0;
        int limitSleepLog = 15;

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

            if (ChordNodeUtils.isMyResponsibility(node, responseCommand.getCommand().getIdRequest())) {
                executionQueue.add(responseCommand);
                logQueue.add(responseCommand.getCommand());
            } else {
                repassQueue.add(responseCommand);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitNewCommand() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
