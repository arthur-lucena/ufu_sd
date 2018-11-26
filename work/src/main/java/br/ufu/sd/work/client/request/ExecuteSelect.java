package br.ufu.sd.work.client.request;

import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.SelectServiceGrpc;
import io.grpc.ManagedChannel;

public class ExecuteSelect implements Runnable {

    private ManagedChannel channel;
    private SelectRequest request;

    public ExecuteSelect(SelectRequest request, ManagedChannel channel) {
        this.request = request;
        this.channel = channel;
    }

    @Override
    public void run() {
        SelectServiceGrpc.SelectServiceBlockingStub stub = SelectServiceGrpc.newBlockingStub(channel);
        System.out.println(stub.select(request).getResponse());
    }
}
