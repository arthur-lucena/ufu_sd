package br.ufu.sd.work.server.service;

import br.ufu.sd.work.server.chord.ChannelNode;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordNodeUtils;
import br.ufu.sd.work.server.chord.ChordServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ServiceChord extends ChordServiceGrpc.ChordServiceImplBase {

    private ChordNode node;

    public ServiceChord(ChordNode node) {
        this.node = node;
    }

    @Override
    public void heyListen(ChordNode request, StreamObserver<ChannelNode> responseObserver) {
        if (!node.hasPreviousNodeChannel()) {
            node = node.toBuilder().setPreviousNodeChannel(ChordNodeUtils.toChannelNode(request)).build();
            System.out.println(node);
        } else {
            // TODO fazer ping para verificar se o nó anterior está rodando, se estiver manter nó anterior, se não substituir
        }

        responseObserver.onNext(ChordNodeUtils.toChannelNode(node));
        responseObserver.onCompleted();
    }
}
