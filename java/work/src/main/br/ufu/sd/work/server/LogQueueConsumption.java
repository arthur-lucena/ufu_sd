package br.ufu.sd.work.server;

import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class LogQueueConsumption implements Runnable {

    private BlockingQueue<MessageCommand> logQueue;
    private MessageCommand mc;
    private volatile boolean running = true;

    public LogQueueConsumption(BlockingQueue<MessageCommand> logQueue) {
        this.logQueue = logQueue;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (logQueue.isEmpty()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            try {
                mc = logQueue.take();
                System.out.println("logando " + mc.getTypeCommand().getCommandString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
