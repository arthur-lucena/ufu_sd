package br.ufu.sd.work.grpc.service;

import br.ufu.sd.work.*;
import br.ufu.sd.work.commands.Insert;
import br.ufu.sd.work.commands.Select;
import br.ufu.sd.work.server.ResponseCommand;
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
