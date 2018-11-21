package br.ufu.sd.work.server.chord;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ChordNode {

    private long minId;
    private long maxId; // is same nodeId
    private String ip;
    private int port;
    private boolean firstNode;
    private boolean lastNode;
    private long maxValue; // is same first Id

    private ChordNode nextNode;
    private ChordNode previousNode;

    public Long getNodeId() {
        return maxId;
    }

    public boolean isMyResponsibility(long id) {
        return minId < id && id <= maxId;
    }

    public ManagedChannel getPossibleResponsibleChannel(Long id) throws ChordException {
        if (isValidNumber(id)) {
            throw new ChordException("Invalid number, this number surpass MAX capacity");
        }

        ChordNode possibleNode;

        if (id <= minId) {
            possibleNode = previousNode;
        } else if (id > maxId) {
            possibleNode = nextNode;
        } else {
            possibleNode = this;
        }

        return ManagedChannelBuilder.forAddress(possibleNode.getIp(), possibleNode.getPort())
                .usePlaintext().build();
    }

    private boolean isValidNumber(long id) {
        return id > maxValue;
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

    public ChordNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(ChordNode nextNode) {
        this.nextNode = nextNode;
    }

    public ChordNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(ChordNode previousNode) {
        this.previousNode = previousNode;
    }
}
