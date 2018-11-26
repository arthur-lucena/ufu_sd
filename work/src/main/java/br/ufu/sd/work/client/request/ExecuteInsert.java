package br.ufu.sd.work.client.request;

import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.InsertServiceGrpc;
import io.grpc.ManagedChannel;

public class ExecuteInsert implements Runnable {

    private ManagedChannel channel;
    private InsertRequest request;

    public ExecuteInsert(InsertRequest request, ManagedChannel channel) {
        this.request = request;
        this.channel = channel;
    }

    @Override
    public void run() {
        InsertServiceGrpc.InsertServiceBlockingStub stub = InsertServiceGrpc.newBlockingStub(channel);
        System.out.println(stub.insert(request).getResponse());
    }
}
