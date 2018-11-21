package br.ufu.sd.work.server.chord;

import br.ufu.sd.work.ChordRequest;
import br.ufu.sd.work.ChordResponse;
import br.ufu.sd.work.ChordServiceGrpc;
import br.ufu.sd.work.request.ExecuteChord;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class ChordConnector {

    private String ip = "127.0.0.1";
    private int port = 51666;
    private int jumpNextPort = 10;
    private int numberOfNodes = 5;
    private int numberBitsId = 32;

    private long firstNode = (long) Math.pow(2,numberBitsId) - 1;
    private long nextNodeSub = (long) Math.pow(2, numberBitsId) / numberOfNodes;

    public ChordNode connect() throws ChordException {
        return tryConnectOnRing(port, firstNode);
    }

    public ChordNode tryConnectOnRing(int port, long candidateNode) throws ChordException{
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext().build();

        ChordServiceGrpc.ChordServiceBlockingStub  stub = ChordServiceGrpc.newBlockingStub(channel);
        ChordResponse response = null;

        try {
            response = stub.heyListen(ChordRequest.newBuilder().setId(candidateNode).build());
        } catch (StatusRuntimeException e) {}

        if (response == null) {
            System.out.println("eu sou o novo nó " + candidateNode);
            ChordNode node = new ChordNode();
            node.setMaxId(candidateNode);
            node.setPort(port);
            return node;
        } else {
            port = port + jumpNextPort;
            candidateNode = candidateNode - nextNodeSub;

            if (candidateNode > 0) {
                return tryConnectOnRing(port, candidateNode);
            } else {
               throw new ChordException("Chord Ring is full!!");
            }
        }
    }

    public static void main(String[] args) {
        new ChordConnector().calculateServersBand(8, 5);
    }

    private static long[] calculateServersBand(int numberOfBits, int numberOfServers) {
        long firstValue = (long) Math.pow(2,numberOfBits) - 1;
        long miniusValue = (long) Math.pow(2,numberOfBits) / numberOfServers;

        System.out.println("----" + miniusValue);

        long[] serversBand = new long[10];
        serversBand[numberOfServers - 1] = firstValue;
        int count = numberOfServers - 2;
        long nextValue = firstValue;

        for(;count >= 0; count --) {
            nextValue = nextValue - miniusValue;
            serversBand[count] = nextValue;
        }

        for(int i = 0;i < 10; i ++) {
            System.out.println(serversBand[i]);
        }

        return serversBand;
    }
}
