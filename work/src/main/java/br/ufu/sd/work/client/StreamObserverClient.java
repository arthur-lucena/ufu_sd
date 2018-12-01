package br.ufu.sd.work.client;

import br.ufu.sd.work.Response;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class StreamObserverClient implements StreamObserver<Response> {
    private static final Logger logger = Logger.getLogger(StreamObserverClient.class.getName());

    private Response response;

    @Override
    public void onNext(Response value) {
        response = value;
    }

    @Override
    public void onError(Throwable t) {
        logger.warning(t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println(response.getResponse());
    }
}
