package br.ufu.sd.work.server.service;

import br.ufu.sd.work.server.chord.*;
import io.grpc.stub.StreamObserver;

public class ServiceChord extends ChordServiceGrpc.ChordServiceImplBase {

    private static volatile ChordNodeWrapper node;

    public ServiceChord(ChordNodeWrapper node) {
        this.node = node;
    }

    @Override
    public void heyListen(ChordNode request, StreamObserver<ChannelNode> responseObserver) {
        responseObserver.onNext(ChordNodeUtils.toChannelNode(node.getChordNode()));
        responseObserver.onCompleted();
    }

    @Override
    public void setPrevious(ChannelNode request, StreamObserver<ChannelNode> responseObserver) {
        node.setPrevious(request);

        responseObserver.onNext(ChordNodeUtils.toChannelNode(node.getChordNode()));
        responseObserver.onCompleted();
    }
}
