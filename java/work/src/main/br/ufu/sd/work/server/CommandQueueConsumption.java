package br.ufu.sd.work.server;

import br.ufu.sd.work.util.ClientSocketCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class CommandQueueConsumption implements Runnable {

    private BlockingQueue<ClientSocketCommand> queue;
    private ObjectOutputStream outToClient;
    private ClientSocketCommand csc;

    public CommandQueueConsumption(BlockingQueue<ClientSocketCommand> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
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
                csc = queue.take();
                // TODO criar fila de log
                // TODO criar fila de execuçao
                csc.getMessageCommand().setExecuted(true);

                csc.getOutToClient().writeObject(csc.getMessageCommand());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
