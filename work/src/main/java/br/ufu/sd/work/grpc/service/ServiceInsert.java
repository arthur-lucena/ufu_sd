package br.ufu.sd.work.grpc.service;

import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.InsertResponse;
import br.ufu.sd.work.InsertServiceGrpc;
import br.ufu.sd.work.server.ResponseCommand;
import br.ufu.sd.work.commands.Insert;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class ServiceInsert extends InsertServiceGrpc.InsertServiceImplBase {

    private BlockingQueue<ResponseCommand> queueOne;

    public ServiceInsert(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void insert(InsertRequest request, StreamObserver<InsertResponse> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Insert(request)));
    }
}
