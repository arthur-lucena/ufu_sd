package br.ufu.sd.work.server.service;

import br.ufu.sd.work.Response;
import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.SelectServiceGrpc;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.commands.Select;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class ServiceSelect extends SelectServiceGrpc.SelectServiceImplBase {

    private BlockingQueue<ResponseCommand> queueOne;

    public ServiceSelect(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void select(SelectRequest request, StreamObserver<Response> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Select(request)));
    }
}
