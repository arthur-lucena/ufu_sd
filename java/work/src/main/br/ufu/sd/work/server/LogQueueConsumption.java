package br.ufu.sd.work.server;

import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.util.MessageCommand;

import java.util.concurrent.BlockingQueue;

import static br.ufu.sd.work.model.ETypeCommand.DELETE;
import static br.ufu.sd.work.model.ETypeCommand.INSERT;
import static br.ufu.sd.work.model.ETypeCommand.UPDATE;

public class LogQueueConsumption implements Runnable {

    private BlockingQueue<MessageCommand> logQueue;
    private MessageCommand messageCommand;
    private LogManager logManager;
    private volatile boolean running = true;

    public LogQueueConsumption(BlockingQueue<MessageCommand> logQueue, LogManager logManager) {
        this.logQueue = logQueue;
        this.logManager = logManager;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (logQueue.isEmpty()) {
                awaitNewMessages();
                continue;
            }
            consume();
            if(isLoggable(messageCommand)) {
                System.out.println("writing new: " + messageCommand.getTypeCommand().name() + " entry on log with data: " + messageCommand.getArgs()[0]);
                logManager.append(Metadata.fromCommand(messageCommand), messageCommand.getTypeCommand());
            }
        }
    }

    private void consume() {
        try {
            messageCommand = logQueue.take();
            System.out.println("logando " + messageCommand.getTypeCommand().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void awaitNewMessages() {
        try {
            Thread.sleep(1000);
            System.out.println("nenhum comando de log enfileirado");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private boolean isLoggable(MessageCommand messageCommand) {
        return messageCommand != null && isInsertOrUpdateOrDeleteCommand(messageCommand);
    }

    private boolean isInsertOrUpdateOrDeleteCommand(MessageCommand messageCommand) {
        return INSERT.equals(messageCommand.getTypeCommand()) || UPDATE.equals(messageCommand.getTypeCommand()) ||
                DELETE.equals(messageCommand.getTypeCommand());
    }
}
