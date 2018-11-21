package br.ufu.sd.work.server.service;

import br.ufu.sd.work.*;
import br.ufu.sd.work.server.commands.Select;
import br.ufu.sd.work.model.ResponseCommand;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class ServiceSelect extends SelectServiceGrpc.SelectServiceImplBase {

    private BlockingQueue<ResponseCommand> queueOne;

    public ServiceSelect(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void select(SelectRequest request, StreamObserver<SelectResponse> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Select(request)));
    }
}
