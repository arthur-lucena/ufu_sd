package br.ufu.sd.work.server.request;

import br.ufu.sd.work.Response;
import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.SelectServiceGrpc;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordNodeUtils;
import io.grpc.ManagedChannel;

public class RedirectSelect implements Runnable {

    private ChordNode node;
    private ResponseCommand responseCommand;

    public RedirectSelect(ChordNode node, ResponseCommand responseCommand) {
        this.node = node;
        this.responseCommand = responseCommand;
    }

    @Override
    public void run() {
        ManagedChannel channel = ChordNodeUtils.getPossibleResponsibleChannel(node, responseCommand.getCommand().getIdRequest());
        SelectServiceGrpc.SelectServiceBlockingStub stub = SelectServiceGrpc.newBlockingStub(channel);
        Response response = stub.select((SelectRequest) responseCommand.getCommand().getRequest());
        responseCommand.getStreamObserver().onNext(response);
        responseCommand.getStreamObserver().onCompleted();
    }
}
