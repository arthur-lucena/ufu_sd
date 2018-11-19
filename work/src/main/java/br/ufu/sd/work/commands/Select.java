package br.ufu.sd.work.commands;

import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.SelectResponse;
import br.ufu.sd.work.commands.api.ICommand;
import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.Metadata;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class Select implements ICommand<SelectResponse> {
    private static final Logger logger = Logger.getLogger(Select.class.getName());

    private SelectRequest request;

    public Select(SelectRequest request) {
        this.request = request;
    }

    @Override
    public void exec(StreamObserver<SelectResponse> so, Dictionary dictionary) {
        byte[] metadataBytes = dictionary.getData().get(request.getId());

        if (metadataBytes != null) {
            Metadata metadata = deserialize(metadataBytes);

            logger.info("object with Id: " + request.getId() + " found");
            so.onNext(SelectResponse.newBuilder().setResponse("object with Id: " + request.getId() + " found: " + metadata.toString()).build());
        } else {
            logger.info("object with Id: " + request.getId() + " not found");
            so.onNext(SelectResponse.newBuilder().setResponse("not found").build());
        }

        so.onCompleted();
    }

    @Override
    public void log(LogManager logManager) {
        logger.info("select dont log in file");
    }

    @Override
    public boolean isExecuted() {
        return true;
    }
}
