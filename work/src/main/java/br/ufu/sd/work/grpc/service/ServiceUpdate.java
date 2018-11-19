package br.ufu.sd.work.grpc.service;

import br.ufu.sd.work.UpdateRequest;
import br.ufu.sd.work.UpdateResponse;
import br.ufu.sd.work.UpdateServiceGrpc;
import br.ufu.sd.work.commands.Update;
import br.ufu.sd.work.server.queue.util.ResponseCommand;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class ServiceUpdate extends UpdateServiceGrpc.UpdateServiceImplBase {

    private BlockingQueue<ResponseCommand> queueOne;

    public ServiceUpdate(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void update(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Update(request)));
    }
}
