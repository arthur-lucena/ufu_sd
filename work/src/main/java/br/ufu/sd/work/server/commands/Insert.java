package br.ufu.sd.work.server.commands;

import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.Response;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.commands.api.ICommand;
import br.ufu.sd.work.server.log.LogManager;
import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.apache.commons.lang3.SerializationUtils.serialize;

public class Insert implements ICommand<InsertRequest, Response> {
    private static final Logger logger = Logger.getLogger(Insert.class.getName());

    private InsertRequest request;
    private volatile boolean executed = false;
    private volatile boolean executedWithSucess = false;
    private volatile Metadata metadata;

    public Insert(InsertRequest request) {
        this.request = request;
    }

    @Override
    public void exec(StreamObserver<Response> so, Dictionary dictionary) {
        metadata = genMetadata(request);

        if (!dictionary.getData().containsKey(metadata.getId())) {
            dictionary.getData().put(metadata.getId(), serialize(metadata));
            logger.info("insert with " + metadata);
            so.onNext(Response.newBuilder().setResponse(new Gson().toJson(metadata)).build());
            executedWithSucess = true;
        } else {
            logger.info("object with Id: " + metadata.getId() + " not created");
            so.onNext(Response.newBuilder().setResponse("Id " + metadata.getId() + " existing").build());
        }

        so.onCompleted();
        executed = true;
    }

    @Override
    public void log(LogManager logManager) {
        if (executedWithSucess) {
            logger.info("logging INSERT with " + metadata);
            logManager.appendLog(metadata, getTypeCommand());
        }
    }

    private Metadata genMetadata(InsertRequest request) {
        return new Metadata(request.getId(), request.getValue(), request.getIdClient(), LocalDateTime.now(), request.getIdClient(), LocalDateTime.now());
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
    public InsertRequest getRequest() {
        return request;
    }

    @Override
    public ETypeCommand getTypeCommand() {
        return ETypeCommand.INSERT;
    }
}
