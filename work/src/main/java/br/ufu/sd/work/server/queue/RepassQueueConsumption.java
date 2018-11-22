package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.UpdateRequest;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.request.ExecuteDelete;
import br.ufu.sd.work.request.ExecuteInsert;
import br.ufu.sd.work.request.ExecuteSelect;
import br.ufu.sd.work.request.ExecuteUpdate;
import br.ufu.sd.work.server.chord.ChordException;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordNodeUtils;
import io.grpc.ManagedChannel;

import java.util.concurrent.BlockingQueue;

public class RepassQueueConsumption implements Runnable {

    private BlockingQueue<ResponseCommand> repassQueue;
    private ChordNode node;
    private ResponseCommand responseCommand;
    private volatile boolean running = true;
    private Dictionary dictionary;

    public RepassQueueConsumption(BlockingQueue<ResponseCommand> repassQueue, ChordNode node) {
        this.repassQueue = repassQueue;
        this.node = node;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (repassQueue.isEmpty()) {
                waitNewCommand();
                continue;
            }

            consumeCommand();
        }
    }

    private void consumeCommand() {
        try {
            responseCommand = repassQueue.take();
            ManagedChannel channel;

            try {
                channel = ChordNodeUtils.getPossibleResponsibleChannel(node, responseCommand.getCommand().getIdRequest());
            } catch (ChordException e) {
                responseCommand.getStreamObserver().onNext(e.getMessage());
                responseCommand.getStreamObserver().onCompleted();
                return;
            }

            Runnable runnableCommand = null;
            String response = null;

            switch (responseCommand.getCommand().getTypeCommand()) {
                case INSERT:
                    runnableCommand = new ExecuteInsert((InsertRequest) responseCommand.getCommand().getRequest(), channel, response);
                    break;
                case UPDATE:
                    runnableCommand = new ExecuteUpdate((UpdateRequest) responseCommand.getCommand().getRequest(), channel, response);
                    break;
                case DELETE:
                    runnableCommand = new ExecuteDelete((DeleteRequest) responseCommand.getCommand().getRequest(), channel, response);
                    break;
                case SELECT:
                    runnableCommand = new ExecuteSelect((SelectRequest) responseCommand.getCommand().getRequest(), channel, response);
                    break;
                default:
                    break;
            }

            Thread thread = new Thread(runnableCommand);
            thread.start();
            thread.join();

            responseCommand.getStreamObserver().onNext(response);
            responseCommand.getStreamObserver().onCompleted();
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
