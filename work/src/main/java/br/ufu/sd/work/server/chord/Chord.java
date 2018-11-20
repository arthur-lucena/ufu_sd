package br.ufu.sd.work.server.chord;

import org.apache.commons.codec.digest.DigestUtils;

public class Chord {

    private String ip = "127.0.0.1";
    private int port = 51666;
    private int jumpNextPort = 10;
    private int numberOfNodes = 5;
    private int numberBitsId = 32;

    private long firstNode = (long) Math.pow(2,numberBitsId) - 1;
    private long nextNodeSub = (long) Math.pow(2, numberBitsId) / numberOfNodes;


    public void connect(String arg[]) {
        long id = tryConnectOnRing(port, firstNode);
    }

    public long tryConnectOnRing(int port, long candidateNode) {
        boolean connected = true;// tentar conectar

        if (!connected) {
            return candidateNode;
        } else {
            port = port + jumpNextPort;
            candidateNode = candidateNode - nextNodeSub;

            if (nextNodeSub > 0) {
                return tryConnectOnRing(port, candidateNode);
            } else {
                return -1;// EXCEPTION anel está cheio
            }
        }
    }
}
