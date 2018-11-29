package br.ufu.sd.work.client.request;

import br.ufu.sd.work.CrudServiceGrpc;
import br.ufu.sd.work.DeleteRequest;
import io.grpc.ManagedChannel;

public class ExecuteDelete implements Runnable {

    private ManagedChannel channel;
    private DeleteRequest request;
    private String response;

    public ExecuteDelete(DeleteRequest request, ManagedChannel channel) {
        this.request = request;
        this.channel = channel;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public void run() {
        CrudServiceGrpc.CrudServiceBlockingStub stub = CrudServiceGrpc.newBlockingStub(channel);
        this.response = stub.delete(request).getResponse();
        System.out.println(this.response);
    }

}
