package br.ufu.sd.work.request;

import br.ufu.sd.work.UpdateRequest;
import br.ufu.sd.work.UpdateResponse;
import br.ufu.sd.work.UpdateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteUpdate implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteUpdate.class.getName());

    private UpdateServiceGrpc.UpdateServiceBlockingStub stub;
    private ManagedChannel channel;
    private UpdateRequest request;
    private String stringResponse;

    public ExecuteUpdate(UpdateRequest request, ManagedChannel channel, String stringResponse) {
        this.request = request;
        this.channel = channel;
        this.stringResponse = stringResponse;
    }

    @Override
    public void run() {
        stub = UpdateServiceGrpc.newBlockingStub(channel);
        UpdateResponse response;

        try {
            response = stub.update(request);
        } catch (StatusRuntimeException e) {
            return;
        }

        if (stringResponse == null) {
            logger.info("response from update request : " + response.getResponse());
        } else {
            stringResponse = response.getResponse();
        }
    }

}
