package br.ufu.sd.work.server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ExecutionQueueConsumption implements Runnable {

    private BlockingQueue<OutputStreamCommand> executionQueue;
    private OutputStreamCommand osc;
    private volatile boolean running = true;

    public ExecutionQueueConsumption(BlockingQueue<OutputStreamCommand> executionQueue) {
        this.executionQueue = executionQueue;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (executionQueue.isEmpty()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            try {
                osc = executionQueue.take();
                osc.getMessageCommand().getCommand().run(osc.getMessageCommand().getArgs());
                osc.getMessageCommand().setExecuted(true);
                osc.getOutputClient().writeObject(osc.getMessageCommand());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
