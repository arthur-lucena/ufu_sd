package br.ufu.sd.work.grpc.request;

import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.InsertResponse;
import br.ufu.sd.work.InsertServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteInsert implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteInsert.class.getName());

    private InsertServiceGrpc.InsertServiceBlockingStub stub;
    private ManagedChannel channel;
    private InsertRequest request;
    private String stringResponse;

    public ExecuteInsert(InsertRequest request, ManagedChannel channel, String stringResponse) {
        this.request = request;
        this.channel = channel;
        this.stringResponse = stringResponse;
    }

    @Override
    public void run() {
        stub = InsertServiceGrpc.newBlockingStub(channel);
        InsertResponse response;

        try {
            response = stub.insert(request);
        } catch (StatusRuntimeException e) {
            return;
        }

        if (stringResponse == null) {
            logger.info("response from insert request : " + response.getResponse());
        } else {
            stringResponse = response.getResponse();
        }
    }

}
