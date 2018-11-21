package br.ufu.sd.work.server.service;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.DeleteResponse;
import br.ufu.sd.work.DeleteServiceGrpc;
import br.ufu.sd.work.server.commands.Delete;
import br.ufu.sd.work.model.ResponseCommand;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class ServiceDelete extends DeleteServiceGrpc.DeleteServiceImplBase {

    private BlockingQueue<ResponseCommand> queueOne;

    public ServiceDelete(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void delete(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Delete(request)));
    }
}
