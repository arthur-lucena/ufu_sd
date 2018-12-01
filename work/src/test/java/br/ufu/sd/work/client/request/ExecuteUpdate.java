package br.ufu.sd.work.client.request;

import br.ufu.sd.work.CrudServiceGrpc;
import br.ufu.sd.work.Request;
import io.grpc.ManagedChannel;

public class ExecuteUpdate implements Runnable {

    private ManagedChannel channel;
    private Request request;
    private String response;

    public String getResponse() {
        return response;
    }

    public ExecuteUpdate(Request request, ManagedChannel channel) {
        this.request = request;
        this.channel = channel;
    }

    @Override
    public void run() {
        CrudServiceGrpc.CrudServiceBlockingStub stub = CrudServiceGrpc.newBlockingStub(channel);
        this.response = stub.update(request).getResponse();
        System.out.println(this.response);
    }
}
