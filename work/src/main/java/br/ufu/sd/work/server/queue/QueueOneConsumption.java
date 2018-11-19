package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.commands.api.ICommand;
import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.queue.util.ResponseCommand;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class QueueOneConsumption implements Runnable {

    private static final Logger logger = Logger.getLogger(QueueOneConsumption.class.getName());

    private BlockingQueue<ResponseCommand> queueOne;
    private BlockingQueue<ICommand> logQueue;
    private BlockingQueue<ResponseCommand> executionQueue;
    private ResponseCommand responseCommand;
    private Dictionary dictionary;
    private LogManager logManager;

    private volatile boolean running = true;


    public QueueOneConsumption(BlockingQueue<ResponseCommand> queueOne, Dictionary dictionary,
                               LogManager logManager) {
        this.queueOne = queueOne;
        this.dictionary = dictionary;
        this.logManager = logManager;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        executionQueue = new ArrayBlockingQueue<>(1000000);
        logQueue = new ArrayBlockingQueue<>(1000000);

        ExecutionQueueConsumption executionQueueConsumption = new ExecutionQueueConsumption(executionQueue, dictionary);
        Thread threadStarter1 = new Thread(executionQueueConsumption);
        threadStarter1.start();

        LogQueueConsumption logQueueConsumption = new LogQueueConsumption(logQueue, logManager);
        Thread threadStarter2 = new Thread(logQueueConsumption);
        threadStarter2.start();

        int counterSleep = 0;
        int limitSleepLog = 5;

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

            // TODO here must be implemented redirection for F4, queue send to next server

            executionQueue.add(responseCommand);
            logQueue.add(responseCommand.getCommand());
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
