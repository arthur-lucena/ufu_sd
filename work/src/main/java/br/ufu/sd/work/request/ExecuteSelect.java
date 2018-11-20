package br.ufu.sd.work.request;

import br.ufu.sd.work.*;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteSelect implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteSelect.class.getName());

    private SelectServiceGrpc.SelectServiceBlockingStub stub;
    private ManagedChannel channel;
    private SelectRequest request;
    private String stringResponse;

    public ExecuteSelect(SelectRequest request, ManagedChannel channel, String stringResponse) {
        this.request = request;
        this.channel = channel;
        this.stringResponse = stringResponse;
    }

    @Override
    public void run() {
        stub = SelectServiceGrpc.newBlockingStub(channel);
        SelectResponse response;

        try {
            response = stub.select(request);
        } catch (StatusRuntimeException e) {
            return;
        }

        if (stringResponse == null) {
            logger.info("response from select request : " + response.getResponse());
        } else {
            stringResponse = response.getResponse();
        }
    }

}
