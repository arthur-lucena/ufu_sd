package br.ufu.sd.work.server.commands.api;

import br.ufu.sd.work.Request;
import br.ufu.sd.work.Response;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.ETypeCommand;
import br.ufu.sd.work.server.log.LogManager;
import io.grpc.stub.StreamObserver;

public interface ICommand{
    void exec(StreamObserver<Response> so, Dictionary dictionary);

    void log(LogManager logManager);

    boolean isExecuted();

    long getIdRequest();

    Request getRequest();

    ETypeCommand getTypeCommand();
}
