package br.ufu.sd.work.request;

import br.ufu.sd.work.Response;
import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.SelectServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteSelect implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteSelect.class.getName());

    private SelectServiceGrpc.SelectServiceBlockingStub stub;
    private ManagedChannel channel;
    private SelectRequest request;
    private Response response;

    public ExecuteSelect(SelectRequest request, ManagedChannel channel, Response response) {
        this.request = request;
        this.channel = channel;
        this.response = response;
    }

    @Override
    public void run() {
        stub = SelectServiceGrpc.newBlockingStub(channel);

        try {
            response = stub.select(request);
        } catch (StatusRuntimeException e) {
            logger.info(e.getMessage());
        }
    }
}
