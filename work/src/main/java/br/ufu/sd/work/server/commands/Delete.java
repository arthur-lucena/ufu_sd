package br.ufu.sd.work.server.commands;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.Response;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.commands.api.ICommand;
import br.ufu.sd.work.server.log.LogManager;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Delete implements ICommand<DeleteRequest, Response> {

    private static final Logger logger = Logger.getLogger(Delete.class.getName());

    private DeleteRequest request;
    private volatile boolean executed = false;
    private volatile boolean executedWithSucess = false;

    public Delete(DeleteRequest request) {
        this.request = request;
    }

    @Override
    public void exec(StreamObserver<Response> so, Dictionary dictionary) {
        if (dictionary.getData().containsKey(request.getId())) {
            dictionary.getData().remove(request.getId());
            logger.info("object with Id: " + request.getId() + " deleted");
            so.onNext(Response.newBuilder().setResponse("objected with Id: " + request.getId() + " deleted").build());
            executedWithSucess = true;
        } else {
            logger.info("object with Id: " + request.getId() + " not found");
            so.onNext(Response.newBuilder().setResponse("Object with id " + request.getId() + " not found").build());
        }

        so.onCompleted();
        executed = true;
    }

    @Override
    public void log(LogManager logManager) {
        if (executedWithSucess) {
            Metadata metadata = genMetadata(request);
            logger.info("logging DELETE with " + metadata);
            logManager.appendLog(metadata, getTypeCommand());
        }
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public long getIdRequest() {
        return request.getId();
    }

    @Override
    public DeleteRequest getRequest() {
        return request;
    }

    @Override
    public ETypeCommand getTypeCommand() {
        return ETypeCommand.DELETE;
    }

    private Metadata genMetadata(DeleteRequest request) {
        return new Metadata(request.getId(), null, null, null, request.getIdClient(), LocalDateTime.now());
    }
}
