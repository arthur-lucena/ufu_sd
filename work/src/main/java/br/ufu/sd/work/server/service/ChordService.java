package br.ufu.sd.work.server.service;

import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.chord.ChordServiceGrpc;
import br.ufu.sd.work.server.chord.Cluster;
import br.ufu.sd.work.server.chord.DataNode;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ChordService extends ChordServiceGrpc.ChordServiceImplBase {

    private static volatile ChordNode node;

    public ChordService(ChordNode node) {
        this.node = node;
    }

    @Override
    public void heyListen(Empty request, StreamObserver<Cluster> responseObserver) {
        responseObserver.onNext(node.getCluster());
        responseObserver.onCompleted();
    }

    @Override
    public void setPrevious(DataNode request, StreamObserver<Empty> responseObserver) {
        node.setPrevious(request);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
        System.out.println(node);
    }

    @Override
    public void setFirstAndLastNode(DataNode request, StreamObserver<DataNode> responseObserver) {
        if (node.isFirstNode()) {
            node.setNext(request);

            DataNode firstNode = DataNode.newBuilder()
                    .setIp(node.getIp())
                    .setPort(node.getPort())
                    .build();

            responseObserver.onNext(firstNode);
            responseObserver.onCompleted();
            System.out.println(node);
        } else {
            ManagedChannel channelNext = ManagedChannelBuilder.forAddress(node.getIpNext(), node.getPortNext())
                    .usePlaintext().build();

            ChordServiceGrpc.ChordServiceBlockingStub stubNext = ChordServiceGrpc.newBlockingStub(channelNext);

            DataNode first = stubNext.setFirstAndLastNode(request);

            responseObserver.onNext(first);
            responseObserver.onCompleted();
        }
    }
}
