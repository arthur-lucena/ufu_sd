package br.ufu.sd.work.server;

import br.ufu.sd.work.util.MessageCommand;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CommandQueueConsumption implements Runnable {

    private BlockingQueue<OutputStreamCommand> queue;
    private BlockingQueue<MessageCommand> logQueue;
    private BlockingQueue<OutputStreamCommand> executionQueue;
    private OutputStreamCommand osc;
    private volatile boolean running = true;

    public CommandQueueConsumption(BlockingQueue<OutputStreamCommand> queue) {
        this.queue = queue;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        executionQueue = new ArrayBlockingQueue<>(1000000);
        logQueue = new ArrayBlockingQueue<>(1000000);

        ExecutionQueueConsumption runnable1 = new ExecutionQueueConsumption(executionQueue);
        Thread t1 = new Thread(runnable1);
        t1.start();

        LogQueueConsumption runnable2 = new LogQueueConsumption(logQueue);
        Thread t2 = new Thread(runnable2);
        t2.start();

        while (running) {
            if (queue.isEmpty()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("nenhum comando enfileirado");
                continue;
            }

            try {
                osc = queue.take();
                executionQueue.add(osc);
                logQueue.add(new MessageCommand(osc.getMessageCommand()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
