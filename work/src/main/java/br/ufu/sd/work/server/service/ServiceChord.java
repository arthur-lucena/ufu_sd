package br.ufu.sd.work.server.service;

import br.ufu.sd.work.ChordNode;
import br.ufu.sd.work.ChordRequest;
import br.ufu.sd.work.ChordResponse;
import br.ufu.sd.work.ChordServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ServiceChord extends ChordServiceGrpc.ChordServiceImplBase {

    private ChordNode node;

    public ServiceChord(ChordNode node) {
        this.node = node;
    }

    @Override
    public void heyListen(ChordRequest request, StreamObserver<ChordResponse> responseObserver) {
        if (node.getPreviousNode() == null) {
            node = node.toBuilder().setPreviousNode(request.getNode()).build();
        }
        responseObserver.onNext(ChordResponse.newBuilder().setNode(node).build());
        responseObserver.onCompleted();
    }
}
