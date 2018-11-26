package br.ufu.sd.work.server.service;

import br.ufu.sd.work.server.Server;
import br.ufu.sd.work.server.chord.ChannelNode;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordNodeUtils;
import br.ufu.sd.work.server.chord.ChordServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ServiceChord extends ChordServiceGrpc.ChordServiceImplBase {

    private volatile ChordNode node;

    public ServiceChord(ChordNode node) {
        this.node = node;
    }

    @Override
    public void heyListen(ChordNode request, StreamObserver<ChannelNode> responseObserver) {
        responseObserver.onNext(ChordNodeUtils.toChannelNode(node));
        responseObserver.onCompleted();
    }

    @Override
    public void setPrevious(ChannelNode request, StreamObserver<ChannelNode> responseObserver) {
        node = ChordNode.getDefaultInstance().toBuilder().mergeFrom(node).setPreviousNodeChannel(request).build();

        System.out.println(node);

        responseObserver.onNext(ChordNodeUtils.toChannelNode(node));
        responseObserver.onCompleted();
    }
}
