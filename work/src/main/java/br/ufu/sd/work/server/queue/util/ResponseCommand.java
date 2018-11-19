package br.ufu.sd.work.server.queue.util;

import br.ufu.sd.work.commands.api.ICommand;
import io.grpc.stub.StreamObserver;

public class ResponseCommand {
    public StreamObserver streamObserver;
    public ICommand command;

    public ResponseCommand(StreamObserver streamObserver, ICommand command) {
        this.streamObserver = streamObserver;
        this.command = command;
    }

    public StreamObserver getStreamObserver() {
        return streamObserver;
    }

    public void setStreamObserver(StreamObserver streamObserver) {
        this.streamObserver = streamObserver;
    }

    public ICommand getCommand() {
        return command;
    }

    public void setCommand(ICommand command) {
        this.command = command;
    }
}
