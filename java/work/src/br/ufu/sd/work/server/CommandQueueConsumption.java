package br.ufu.sd.work.server;

import br.ufu.sd.work.util.ClientSocketCommand;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class CommandQueueConsumption implements Runnable {

    private BlockingQueue<ClientSocketCommand> queue;
    private PrintWriter out;
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
                // TODO logar
                out = new PrintWriter(csc.getClientSocket().getOutputStream(), true);
                out.println("Commando " + csc.getCommand().getNome() + " executado!!!");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                    csc.getClientSocket().close();
                } catch (IOException e1) {

                }
            }
        }

    }
}
