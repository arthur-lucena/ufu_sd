package br.ufu.sd.work.client.request;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.DeleteServiceGrpc;
import io.grpc.ManagedChannel;

public class ExecuteDelete implements Runnable {

    private ManagedChannel channel;
    private DeleteRequest request;

    public ExecuteDelete(DeleteRequest request, ManagedChannel channel) {
        this.request = request;
        this.channel = channel;
    }

    @Override
    public void run() {
        DeleteServiceGrpc.DeleteServiceBlockingStub stub = DeleteServiceGrpc.newBlockingStub(channel);
        System.out.println(stub.delete(request).getResponse());
    }

}
