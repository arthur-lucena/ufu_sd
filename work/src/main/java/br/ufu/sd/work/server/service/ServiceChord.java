package br.ufu.sd.work.server.service;

import br.ufu.sd.work.ChordRequest;
import br.ufu.sd.work.ChordResponse;
import br.ufu.sd.work.ChordServiceGrpc;
import br.ufu.sd.work.server.chord.ChordNode;
import io.grpc.stub.StreamObserver;

public class ServiceChord extends ChordServiceGrpc.ChordServiceImplBase {

    private ChordNode node;

    public ServiceChord(ChordNode node) {
        this.node = node;
    }

    @Override
    public void heyListen(ChordRequest request, StreamObserver<ChordResponse> responseObserver) {
        System.out.println("alguem tentou entrar no anel " + request.getId());
        responseObserver.onNext(ChordResponse.newBuilder().setId(node.getNodeId()).build());
        responseObserver.onCompleted();
    }
}
