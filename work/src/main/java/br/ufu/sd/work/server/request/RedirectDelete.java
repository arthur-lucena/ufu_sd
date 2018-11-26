package br.ufu.sd.work.server.request;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.DeleteServiceGrpc;
import br.ufu.sd.work.Response;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordNodeUtils;
import io.grpc.ManagedChannel;

public class RedirectDelete implements Runnable {

    private ChordNode node;
    private ResponseCommand responseCommand;

    public RedirectDelete(ChordNode node, ResponseCommand responseCommand) {
        this.node = node;
        this.responseCommand = responseCommand;
    }

    @Override
    public void run() {
        ManagedChannel channel = ChordNodeUtils.getPossibleResponsibleChannel(node, responseCommand.getCommand().getIdRequest());
        DeleteServiceGrpc.DeleteServiceBlockingStub stub = DeleteServiceGrpc.newBlockingStub(channel);
        Response response = stub.delete((DeleteRequest) responseCommand.getCommand().getRequest());
        responseCommand.getStreamObserver().onNext(response);
        responseCommand.getStreamObserver().onCompleted();
    }

}
