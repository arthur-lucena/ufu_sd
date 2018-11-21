package br.ufu.sd.work.server.chord;

import br.ufu.sd.work.ChordNode;
import br.ufu.sd.work.ChordRequest;
import br.ufu.sd.work.ChordResponse;
import br.ufu.sd.work.ChordServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class ChordConnector {

    private String ip;
    private int port;
    private int jumpNextPort;
    private long firstNode;
    private long nextNodeSub;

    public ChordConnector(String ip, int port, int jumpNextPort, int numberOfNodes, int numberBitsId) {
        this.ip = ip;
        this.port = port;
        this.jumpNextPort = jumpNextPort;

        this.firstNode = (long) Math.pow(2,numberBitsId) - 1;
        this.nextNodeSub = (long) Math.pow(2, numberBitsId) / numberOfNodes;
    }

    public ChordNode connect() throws ChordException {
        ChordNode node = ChordNode.newBuilder()
                .setIp(ip)
                .setPort(port)
                .setNodeId(firstNode)
                .setMaxId(firstNode)
                .setMinId(firstNode - nextNodeSub)
                .setFirstNode(true)
                .build();

        return tryConnectOnRing(node);
    }

    public ChordNode tryConnectOnRing(ChordNode candidateNode) throws ChordException{
        ManagedChannel channel = ManagedChannelBuilder.forAddress(candidateNode.getIp(), candidateNode.getPort())
                .usePlaintext().build();

        ChordServiceGrpc.ChordServiceBlockingStub stub = ChordServiceGrpc.newBlockingStub(channel);
        ChordResponse response = null;

        try {
            response = stub.heyListen(ChordRequest.newBuilder().setNode(candidateNode).build());
        } catch (StatusRuntimeException e) {}

        if (response == null) {
            System.out.println(candidateNode.toString());
            return candidateNode;
        } else {
            ChordNode newCandidateNode = ChordNode.newBuilder()
                    .setIp(ip)
                    .setPort(candidateNode.getPort() + jumpNextPort)
                    .setNodeId(candidateNode.getNodeId() - nextNodeSub)
                    .setMaxId(candidateNode.getMaxId() - nextNodeSub)
                    .setMinId(candidateNode.getMinId() - nextNodeSub)
                    .setNextNode(candidateNode)
                    .setFirstNode(false)
                    .setLastNode(candidateNode.getNodeId() - nextNodeSub - nextNodeSub == 0)
                    .build();

            if (newCandidateNode.getNodeId() > 0) {
                return tryConnectOnRing(newCandidateNode);
            } else {
               throw new ChordException("Chord Ring is full!!");
            }
        }
    }
}
