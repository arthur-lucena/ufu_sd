package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.request.RedirectDelete;
import br.ufu.sd.work.server.request.RedirectInsert;
import br.ufu.sd.work.server.request.RedirectSelect;
import br.ufu.sd.work.server.request.RedirectUpdate;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class RedirectQueueConsumption implements Runnable {

    private static final Logger logger = Logger.getLogger(RedirectQueueConsumption.class.getName());

    private BlockingQueue<ResponseCommand> redirectQueue;
    private ResponseCommand responseCommand;
    private volatile ChordNode node;
    private volatile boolean running = true;

    public RedirectQueueConsumption(BlockingQueue<ResponseCommand> redirectQueue, ChordNode node) {
        this.redirectQueue = redirectQueue;
        this.node = node;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (redirectQueue.isEmpty()) {
                waitNewCommand();
                continue;
            }

            consumeCommand();
        }
    }

    private void consumeCommand() {
        try {
            responseCommand = redirectQueue.take();

            Thread thread = null;

            System.out.println(node);

            switch (responseCommand.getCommand().getTypeCommand()) {
                case INSERT:
                    thread = new Thread(new RedirectInsert(node, responseCommand));
                    break;
                case UPDATE:
                    thread = new Thread(new RedirectUpdate(node, responseCommand));
                    break;
                case DELETE:
                    thread = new Thread(new RedirectDelete(node, responseCommand));
                    break;
                case SELECT:
                    thread = new Thread(new RedirectSelect(node, responseCommand));
                    break;
                default:
                    break;
            }

            logger.info("Command " + responseCommand.getCommand().getTypeCommand() + " redirect : " + responseCommand.getCommand().getRequest().toString());

            thread.start();
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
