package br.ufu.sd.work.client.request;

import br.ufu.sd.work.CrudServiceGrpc;
import br.ufu.sd.work.SelectRequest;
import io.grpc.ManagedChannel;

public class ExecuteSelect implements Runnable {

    private ManagedChannel channel;
    private SelectRequest request;
    private String response;

    public String getResponse() {
        return response;
    }

    public ExecuteSelect(SelectRequest request, ManagedChannel channel) {
        this.request = request;
        this.channel = channel;
    }

    @Override
    public void run() {
        CrudServiceGrpc.CrudServiceBlockingStub stub = CrudServiceGrpc.newBlockingStub(channel);
        this.response = stub.select(request).getResponse();
        System.out.println(this.response);
    }
}
