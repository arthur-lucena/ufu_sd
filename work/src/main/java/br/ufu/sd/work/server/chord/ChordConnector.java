package br.ufu.sd.work.server.chord;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
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
        ChannelNode response = null;

        try {
            response = stub.heyListen(candidateNode);
        } catch (StatusRuntimeException e) {
            // TODO diferenciar de nenhum serviço rodando na porta, de uma porta já ocupada
            if (e.getStatus().getCode().equals(Status.Code.UNAVAILABLE)) {
                System.out.println("não tem ngm");
            }

            if (e.getStatus().getCode().equals(Status.Code.ALREADY_EXISTS) || e.getStatus().getCode().equals(Status.Code.UNIMPLEMENTED)) {
                System.out.println("ocupada outro serviço ou outro servidor grpc");
            }

        }

        if (response == null) {
            System.out.println(candidateNode.toString());
            return candidateNode;
        } else {
            ChordNode newCandidateNode = ChordNode.newBuilder()
                    .setIp(ip)
                    .setPort(candidateNode.getPort() + jumpNextPort) // TODO diferenciar de nenhum serviço rodando na porta, de uma porta já ocupada, se porta estiver ocupada só incrementar a porta
                    .setNodeId(candidateNode.getNodeId() - nextNodeSub)
                    .setMaxId(candidateNode.getMaxId() - nextNodeSub)
                    .setMinId(candidateNode.getMinId() - nextNodeSub)
                    .setNextNodeChannel(response)
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
