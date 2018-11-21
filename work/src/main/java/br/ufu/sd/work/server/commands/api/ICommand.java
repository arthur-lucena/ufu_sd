package br.ufu.sd.work.server.commands.api;

import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import io.grpc.stub.StreamObserver;

public interface ICommand<T> {
    void exec(StreamObserver<T> so, Dictionary dictionary);

    void log(LogManager logManager);

    boolean isExecuted();
}
