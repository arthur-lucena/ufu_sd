package br.ufu.sd.work.server.commands.api;

import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import io.grpc.stub.StreamObserver;

public interface ICommand<RQ, RE> {
    void exec(StreamObserver<RE> so, Dictionary dictionary);

    void log(LogManager logManager);

    boolean isExecuted();

    long getIdRequest();

    RQ getRequest();

    ETypeCommand getTypeCommand();
}
