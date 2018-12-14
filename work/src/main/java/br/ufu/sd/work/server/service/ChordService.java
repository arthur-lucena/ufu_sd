package br.ufu.sd.work.server.service;

import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordServiceGrpc;
import br.ufu.sd.work.server.chord.DataNode;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class ChordService extends ChordServiceGrpc.ChordServiceImplBase {

    private static volatile ChordNode node;

    public ChordService(ChordNode node) {
        this.node = node;
    }

    @Override
    public void heyListen(Empty request, StreamObserver<DataNode> responseObserver) {
        responseObserver.onNext(node.getDataNode());
        responseObserver.onCompleted();
    }

    @Override
    public void setPrevious(DataNode request, StreamObserver<Empty> responseObserver) {
        node.setPrevious(request);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void setLastNode(DataNode request, StreamObserver<Empty> responseObserver) {
        if (node.isFirstNode()) {

        }

        node.setPrevious(request);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
