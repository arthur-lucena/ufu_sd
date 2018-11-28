package br.ufu.sd.work.client.request;

import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.SelectServiceGrpc;
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
        SelectServiceGrpc.SelectServiceBlockingStub stub = SelectServiceGrpc.newBlockingStub(channel);
        this.response = stub.select(request).getResponse();
        System.out.println(this.response);
    }
}
