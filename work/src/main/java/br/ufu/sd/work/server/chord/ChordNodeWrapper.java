package br.ufu.sd.work.server.chord;

public class ChordNodeWrapper {

    private long nodeId;
    private long minId;
    private long maxId;
    private long maxChordId;
    private String ip;
    private int port;
    private boolean firstNode;
    private boolean lastNode;
    private String ipNext;
    private int portNext;
    private String ipPrevious;
    private int portPrevious;

    public ChordNode getChordNode() {
        ChannelNode.newBuilder()

        ChordNode.newBuilder()
                .setIp(ip)
                .setPort(port)
                .setNodeId(nodeId)
                .setMaxId(firstNode)
                .setMaxChordId(firstNode)
                .setMinId(minId)
                .setFirstNode(firstNode)
                .setLastNode(lastNode)
                .build();

    }

    public void setByChordNode(ChordNode node) {

    }
}
