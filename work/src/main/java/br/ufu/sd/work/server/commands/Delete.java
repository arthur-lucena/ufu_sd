package br.ufu.sd.work.server.commands;

import br.ufu.sd.work.*;
import br.ufu.sd.work.server.commands.api.ICommand;
import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class Delete implements ICommand<DeleteResponse> {

    private static final Logger logger = Logger.getLogger(Delete.class.getName());

    private DeleteRequest request;
    private volatile boolean executed = false;
    private volatile boolean executedWithSucess = false;

    public Delete(DeleteRequest request) {
        this.request = request;
    }

    @Override
    public void exec(StreamObserver<DeleteResponse> so, Dictionary dictionary) {
        if (dictionary.getData().containsKey(request.getId())) {
            dictionary.getData().remove(request.getId());
            logger.info("object with Id: " + request.getId() + " deleted");
            so.onNext(DeleteResponse.newBuilder().setResponse("objected with Id: " + request.getId() + " deleted").build());
            executedWithSucess = true;
        } else {
            logger.info("object with Id: " + request.getId() + " not found");
            so.onNext(DeleteResponse.newBuilder().setResponse("Object with id " + request.getId() + " not found").build());
        }

        so.onCompleted();
        executed = true;
    }

    @Override
    public void log(LogManager logManager) {
        if (executedWithSucess) {
            Metadata metadata = genMetadata(request);
            logger.info("logging DELETE with " + metadata);
            logManager.appendLog(metadata, ETypeCommand.DELETE);
        }
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    private Metadata genMetadata(DeleteRequest request) {
        return new Metadata(request.getId(), null, null, null, request.getIdClient(), LocalDateTime.now());
    }
}
