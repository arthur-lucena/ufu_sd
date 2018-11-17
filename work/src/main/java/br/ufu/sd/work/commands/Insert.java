package br.ufu.sd.work.commands;

import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.InsertResponse;
import br.ufu.sd.work.commands.api.ICommand;
import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.apache.commons.lang3.SerializationUtils.serialize;

public class Insert implements ICommand<InsertResponse> {
    private static final Logger logger = Logger.getLogger(Insert.class.getName());

    private InsertRequest request;
    private volatile boolean executed = false;
    private volatile boolean executedWithSucess = false;

    public Insert(InsertRequest request) {
        this.request = request;
    }

    @Override
    public void exec(StreamObserver<InsertResponse> so, Dictionary dictionary) {
        Metadata metadata = genMetadata(request);

        if (!dictionary.getData().containsKey(metadata.getId())) {
            dictionary.getData().put(metadata.getId(), serialize(metadata));
            logger.info("insert with " + metadata);
            so.onNext(InsertResponse.newBuilder().setResponse("insert success: " + metadata).build());
            executedWithSucess = true;
        } else {
            logger.info("object with Id: " + metadata.getId() + " not created");
            so.onNext(InsertResponse.newBuilder().setResponse("Id " + metadata.getId() + " existing").build());
        }

        so.onCompleted();
        executed = true;
    }

    @Override
    public void log(LogManager logManager) {
        if (executedWithSucess) {
            Metadata metadata = genMetadata(request);
            logger.info("logging with " + metadata);
            logManager.append(metadata, ETypeCommand.INSERT);
        }
    }

    private Metadata genMetadata(InsertRequest request) {
        return new Metadata(request.getId(), request.getValue(), request.getIdClient(), LocalDateTime.now(), request.getIdClient(), LocalDateTime.now());
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
}
