package br.ufu.sd.work.server.service;

import br.ufu.sd.work.*;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.commands.Delete;
import br.ufu.sd.work.server.commands.Insert;
import br.ufu.sd.work.server.commands.Select;
import br.ufu.sd.work.server.commands.Update;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class CrudService extends CrudServiceGrpc.CrudServiceImplBase{

    private BlockingQueue<ResponseCommand> queueOne;

    public CrudService(BlockingQueue<ResponseCommand> queueOne) {
        this.queueOne = queueOne;
    }

    @Override
    public void select(SelectRequest request, StreamObserver<Response> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Select(request)));
    }

    @Override
    public void insert(InsertRequest request, StreamObserver<Response> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Insert(request)));
    }

    @Override
    public void update(UpdateRequest request, StreamObserver<Response> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Update(request)));
    }

    @Override
    public void delete(DeleteRequest request, StreamObserver<Response> responseObserver) {
        queueOne.add(new ResponseCommand(responseObserver, new Delete(request)));
    }
}
