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
        ChannelNode next = ChannelNode.newBuilder()
                .setIp(ipNext)
                .setPort(portNext)
                .build();

        ChannelNode previous = ChannelNode.newBuilder()
                .setIp(ipPrevious)
                .setPort(portPrevious)
                .build();

        return ChordNode.newBuilder()
                .setNodeId(nodeId)
                .setMinId(minId)
                .setMaxId(maxId)
                .setMaxChordId(maxChordId)
                .setIp(ip)
                .setPort(port)
                .setFirstNode(firstNode)
                .setLastNode(lastNode)
                .setNextNodeChannel(next)
                .setPreviousNodeChannel(previous)
                .build();
    }

    public void setByChordNode(ChordNode node) {
        this.nodeId = node.getNodeId();
        this.minId = node.getMinId();
        this.maxId = node.getMaxId();
        this.maxChordId = node.getMaxChordId();
        this.ip = node.getIp();
        this.port = node.getPort();
        this.firstNode = node.getFirstNode();
        this.lastNode = node.getLastNode();
        this.ipNext = node.getNextNodeChannel().getIp();
        this.portNext = node.getNextNodeChannel().getPort();
        this.ipPrevious = node.getPreviousNodeChannel().getIp();
        this.portPrevious = node.getPreviousNodeChannel().getPort();
    }

    public void setNext(ChannelNode channelNode) {
        this.ipNext = channelNode.getIp();
        this.portNext = channelNode.getPort();
    }

    public void setPrevious(ChannelNode channelNode) {
        this.ipPrevious = channelNode.getIp();
        this.portPrevious = channelNode.getPort();
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public long getMinId() {
        return minId;
    }

    public void setMinId(long minId) {
        this.minId = minId;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public long getMaxChordId() {
        return maxChordId;
    }

    public void setMaxChordId(long maxChordId) {
        this.maxChordId = maxChordId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isFirstNode() {
        return firstNode;
    }

    public void setFirstNode(boolean firstNode) {
        this.firstNode = firstNode;
    }

    public boolean isLastNode() {
        return lastNode;
    }

    public void setLastNode(boolean lastNode) {
        this.lastNode = lastNode;
    }

    public String getIpNext() {
        return ipNext;
    }

    public void setIpNext(String ipNext) {
        this.ipNext = ipNext;
    }

    public int getPortNext() {
        return portNext;
    }

    public void setPortNext(int portNext) {
        this.portNext = portNext;
    }

    public String getIpPrevious() {
        return ipPrevious;
    }

    public void setIpPrevious(String ipPrevious) {
        this.ipPrevious = ipPrevious;
    }

    public int getPortPrevious() {
        return portPrevious;
    }

    public void setPortPrevious(int portPrevious) {
        this.portPrevious = portPrevious;
    }
}
