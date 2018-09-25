package br.ufu.sd.work.server;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.util.commands.Insert;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ExecutionQueueConsumption implements Runnable {

    private BlockingQueue<OutputStreamCommand> executionQueue;
    private OutputStreamCommand osc;
    private volatile boolean running = true;
    private Dictionary dictionary;

    public ExecutionQueueConsumption(BlockingQueue<OutputStreamCommand> executionQueue, Dictionary dictionary) {
        this.executionQueue = executionQueue;
        this.dictionary = dictionary;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (executionQueue.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            try {
                osc = executionQueue.take();
                osc.getMessageCommand().getCommand().run(this.osc, this.dictionary);
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
