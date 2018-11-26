package br.ufu.sd.work.server.service;

import br.ufu.sd.work.Response;
import br.ufu.sd.work.UpdateRequest;
import br.ufu.sd.work.UpdateServiceGrpc;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.commands.Update;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class ServiceUpdate extends UpdateServiceGrpc.UpdateServiceImplBase {

    private BlockingQueue<ResponseCommand> queueOne;

    public ServiceUpdate(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void update(UpdateRequest request, StreamObserver<Response> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Update(request)));
    }
}
