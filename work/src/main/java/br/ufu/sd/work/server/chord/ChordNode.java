package br.ufu.sd.work.server.chord;

public class ChordNode {
    private long nodeId;
    private long offSetId;
    private long maxId;
    private int numberOfNodes;
    private boolean firstNode;
    private boolean lastNode;
    private String ip;
    private int port;
    private String ipNext;
    private int portNext;
    private String ipPrevious;
    private int portPrevious;

    public void setNext(DataNode channelNode) {
        this.ipNext = channelNode.getIp();
        this.portNext = channelNode.getPort();
    }

    public void setPrevious(DataNode channelNode) {
        this.ipPrevious = channelNode.getIp();
        this.portPrevious = channelNode.getPort();
    }

    public DataNode getDataNode() {
        return DataNode.newBuilder()
                .setIp(this.ip)
                .setPort(this.port)
                .build();
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public long getOffSetId() {
        return offSetId;
    }

    public void setOffSetId(long offSetId) {
        this.offSetId = offSetId;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
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

    @Override
    public String toString() {
        return "ChordNode{" +
                "\nnodeId=" + nodeId +
                "\n, offSetId=" + offSetId +
                "\n, maxId=" + maxId +
                "\n, numberOfNodes=" + numberOfNodes +
                "\n, ip='" + ip + '\'' +
                "\n, port=" + port +
                "\n, firstNode=" + firstNode +
                "\n, lastNode=" + lastNode +
                "\n, ipNext='" + ipNext + '\'' +
                "\n, portNext=" + portNext +
                "\n, ipPrevious='" + ipPrevious + '\'' +
                "\n, portPrevious=" + portPrevious +
                "\n}";
    }
}
