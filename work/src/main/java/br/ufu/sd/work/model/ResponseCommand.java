package br.ufu.sd.work.model;

import br.ufu.sd.work.server.commands.api.ICommand;
import io.grpc.stub.StreamObserver;

public class ResponseCommand {
    private StreamObserver streamObserver;
    private ICommand command;

    public ResponseCommand(StreamObserver streamObserver, ICommand command) {
        this.streamObserver = streamObserver;
        this.command = command;
    }

    public StreamObserver getStreamObserver() {
        return streamObserver;
    }

    public ICommand getCommand() {
        return command;
    }
}
