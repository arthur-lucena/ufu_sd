package br.ufu.sd.work.request;

import br.ufu.sd.work.Response;
import br.ufu.sd.work.UpdateRequest;
import br.ufu.sd.work.UpdateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteUpdate implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteUpdate.class.getName());

    private UpdateServiceGrpc.UpdateServiceBlockingStub stub;
    private ManagedChannel channel;
    private UpdateRequest request;
    private Response response;

    public ExecuteUpdate(UpdateRequest request, ManagedChannel channel, Response response) {
        this.request = request;
        this.channel = channel;
        this.response = response;
    }

    @Override
    public void run() {
        stub = UpdateServiceGrpc.newBlockingStub(channel);

        try {
            response = stub.update(request);
        } catch (StatusRuntimeException e) {
            logger.info(e.getMessage());
        }
    }
}
