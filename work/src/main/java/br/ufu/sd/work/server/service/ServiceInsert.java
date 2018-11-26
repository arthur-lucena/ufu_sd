package br.ufu.sd.work.server.service;

import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.InsertServiceGrpc;
import br.ufu.sd.work.Response;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.commands.Insert;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class ServiceInsert extends InsertServiceGrpc.InsertServiceImplBase {

    private BlockingQueue<ResponseCommand> queueOne;

    public ServiceInsert(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void insert(InsertRequest request, StreamObserver<Response> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Insert(request)));
    }
}
