package br.ufu.sd.work.commands;

import br.ufu.sd.work.DeleteResponse;
import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.UpdateRequest;
import br.ufu.sd.work.UpdateResponse;
import br.ufu.sd.work.commands.api.ICommand;
import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class Update implements ICommand<UpdateResponse> {
    private static final Logger logger = Logger.getLogger(Update.class.getName());

    private UpdateRequest request;
    private volatile boolean executed = false;
    private volatile boolean executedWithSucess = false;
    private volatile Metadata metadata;

    public Update(UpdateRequest request) {
        this.request = request;
    }

    @Override
    public void exec(StreamObserver<UpdateResponse> so, Dictionary dictionary) {
        byte[] metadataBytes = dictionary.getData().get(request.getId());

        if (metadataBytes != null) {
            metadata = deserialize(metadataBytes);

            metadata.setMessage(request.getValue());
            metadata.setUpdatedBy(request.getIdClient());
            metadata.setUpdatedAt(LocalDateTime.now());

            dictionary.getData().put(metadata.getId(), serialize(metadata));

            logger.info("object with Id: " + metadata.getId() + " update: " + metadata.toString());
            so.onNext(UpdateResponse.newBuilder().setResponse("objected with Id: " + metadata.getId() + " updated").build());
            executedWithSucess = true;
        } else {
            logger.info("object with Id: " + request.getId() + " not found");
            so.onNext(UpdateResponse.newBuilder().setResponse("Object with id " + metadata.getId() + " not found").build());
        }

        so.onCompleted();
        executed = true;
    }

    @Override
    public void log(LogManager logManager) {
        if (executedWithSucess) {
            logger.info("logging UPDATE with " + metadata);
            logManager.appendLog(metadata, ETypeCommand.UPDATE);
        }
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
}
