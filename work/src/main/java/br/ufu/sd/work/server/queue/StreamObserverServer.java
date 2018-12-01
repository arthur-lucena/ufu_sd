package br.ufu.sd.work.server.queue;

import br.ufu.sd.work.Response;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class StreamObserverServer implements StreamObserver<Response> {
    private StreamObserver request;

    public StreamObserverServer(StreamObserver request) {
        this.request = request;
    }

    @Override
    public void onNext(Response value) {
        request.onNext(value);
    }

    @Override
    public void onError(Throwable t) {
        request.onError(t);
    }

    @Override
    public void onCompleted() {
        request.onCompleted();
    }


}
