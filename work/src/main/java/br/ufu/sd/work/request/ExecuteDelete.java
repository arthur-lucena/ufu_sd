package br.ufu.sd.work.request;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.DeleteServiceGrpc;
import br.ufu.sd.work.Response;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteDelete implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteDelete.class.getName());

    private DeleteServiceGrpc.DeleteServiceBlockingStub stub;
    private ManagedChannel channel;
    private DeleteRequest request;
    private Response response;

    public ExecuteDelete(DeleteRequest request, ManagedChannel channel, Response response) {
        this.request = request;
        this.channel = channel;
        this.response = response;
    }

    @Override
    public void run() {
        stub = DeleteServiceGrpc.newBlockingStub(channel);

        try {
            response = stub.delete(request);
        } catch (StatusRuntimeException e) {
            logger.info(e.getMessage());
        }
    }

}
