package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ResponseCommand;

import java.util.concurrent.BlockingQueue;

public class ExecutionQueueConsumption implements Runnable {

    private BlockingQueue<ResponseCommand> executionQueue;
    private ResponseCommand responseCommand;
    private volatile boolean running = true;
    private Dictionary dictionary;
    private int delayCommand;

    public ExecutionQueueConsumption(BlockingQueue<ResponseCommand> executionQueue, Dictionary dictionary, int delayCommand) {
        this.executionQueue = executionQueue;
        this.dictionary = dictionary;
        this.delayCommand = delayCommand;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (executionQueue.isEmpty()) {
                waitNewCommand();
                continue;
            }

            consumeCommand();
        }
    }

    private void consumeCommand() {
        try {
            responseCommand = executionQueue.take();
            responseCommand.getCommand().exec(responseCommand.getStreamObserver(), dictionary);
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
}
