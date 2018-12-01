package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.server.commands.api.ICommand;
import br.ufu.sd.work.server.log.LogManager;

import java.util.concurrent.BlockingQueue;

public class LogQueueConsumption implements Runnable {

    private BlockingQueue<ICommand> logQueue;
    private LogManager logManager;
    private ICommand command;
    private int delayLog;
    private volatile boolean running = true;

    public LogQueueConsumption(BlockingQueue<ICommand> logQueue, LogManager logManager, int delayLog) {
        this.logQueue = logQueue;
        this.logManager = logManager;
        this.delayLog = delayLog;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (logQueue.isEmpty()) {
                waitNewCommand();
                continue;
            }

            consumeCommand();
        }
    }

    private void consumeCommand() {
        try {
            command = logQueue.take();

            if (command.isExecuted()) {
                command.log(logManager);
            } else {
                logQueue.add(command);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitNewCommand() {
        try {
            Thread.sleep(delayLog);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
