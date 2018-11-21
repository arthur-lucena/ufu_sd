package br.ufu.sd.work.request;

import br.ufu.sd.work.ChordRequest;
import br.ufu.sd.work.ChordResponse;
import br.ufu.sd.work.ChordServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class ExecuteChord implements Runnable {

    private static final Logger logger = Logger.getLogger(ExecuteChord.class.getName());

    private ChordServiceGrpc.ChordServiceBlockingStub stub;
    private ManagedChannel channel;
    private ChordRequest request;
    private long longResponse;

    public ExecuteChord(ChordRequest request, ManagedChannel channel, long longResponse) {
        this.request = request;
        this.channel = channel;
        this.longResponse = longResponse;
    }

    @Override
    public void run() {
        stub = ChordServiceGrpc.newBlockingStub(channel);
        ChordResponse response;

        try {
            response = stub.heyListen(request);
        } catch (StatusRuntimeException e) {
            return;
        }

        if (longResponse == 0) {
            logger.info("response from delete request : " + response.getId());
        } else {
            longResponse = response.getId();
        }
    }

}
