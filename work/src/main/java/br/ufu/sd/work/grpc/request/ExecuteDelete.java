package br.ufu.sd.work.grpc.request;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.DeleteResponse;
import br.ufu.sd.work.DeleteServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteDelete implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteDelete.class.getName());

    private DeleteServiceGrpc.DeleteServiceBlockingStub stub;
    private ManagedChannel channel;
    private DeleteRequest request;
    private String stringResponse;

    public ExecuteDelete(DeleteRequest request, ManagedChannel channel, String stringResponse) {
        this.request = request;
        this.channel = channel;
        this.stringResponse = stringResponse;
    }

    @Override
    public void run() {
        stub = DeleteServiceGrpc.newBlockingStub(channel);
        DeleteResponse response;

        try {
            response = stub.delete(request);
        } catch (StatusRuntimeException e) {
            return;
        }

        if (stringResponse == null) {
            logger.info("response from delete request : " + response.getResponse());
        } else {
            stringResponse = response.getResponse();
        }
    }

}
