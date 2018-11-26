package br.ufu.sd.work.request;

import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.InsertServiceGrpc;
import br.ufu.sd.work.Response;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteInsert implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteInsert.class.getName());

    private InsertServiceGrpc.InsertServiceBlockingStub stub;
    private ManagedChannel channel;
    private InsertRequest request;
    private Response response;

    public ExecuteInsert(InsertRequest request, ManagedChannel channel, Response response) {
        this.request = request;
        this.channel = channel;
        this.response = response;
    }

    @Override
    public void run() {
        stub = InsertServiceGrpc.newBlockingStub(channel);

        try {
            response = stub.insert(request);
        } catch (StatusRuntimeException e) {
            logger.info(e.getMessage());
        }
    }
}
