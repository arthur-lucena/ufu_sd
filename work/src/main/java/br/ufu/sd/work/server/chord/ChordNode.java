package br.ufu.sd.work.server.chord;

public class ChordNode {
    private int nodeId;
    private int clusterId;
    private long offSetId;
    private long maxId;
    private int numberOfNodes;
    private boolean firstNode;
    private boolean lastNode;
    private Cluster cluster;
    private Cluster nextCluster;
    private Cluster previousCluster;

    public void addNodeToCluster(DataNode node) {
        cluster.toBuilder().addListDataNode(node);
    }

    public void addNodeToNextCluster(DataNode node) {
        nextCluster.toBuilder().addListDataNode(node);
    }

    public void addNodeToPreviousCluster(DataNode node) {
        previousCluster.toBuilder().addListDataNode(node);
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
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

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Cluster getNextCluster() {
        return nextCluster;
    }

    public void setNextCluster(Cluster nextCluster) {
        this.nextCluster = nextCluster;
    }

    public Cluster getPreviousCluster() {
        return previousCluster;
    }

    public void setPreviousCluster(Cluster previousCluster) {
        this.previousCluster = previousCluster;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ChordNode{");
        sb.append("\nnodeId=");
        sb.append(nodeId);
        sb.append("\noffSetId=");
        sb.append(offSetId);
        sb.append("\nmaxId=");
        sb.append(maxId);
        sb.append("\nnumberOfNodes=");
        sb.append(numberOfNodes);
        sb.append("\nfirstNode=");
        sb.append(firstNode);
        sb.append("\nlastNode=");
        sb.append(lastNode);
        sb.append("\ncluster=");
        sb.append(listDataNodeToString(cluster));
        sb.append("\nnextCluster=");
        sb.append(listDataNodeToString(nextCluster));
        sb.append("\npreviousCluster=");
        sb.append(listDataNodeToString(previousCluster));
        sb.append("\n}");
        return sb.toString();
    }

    private String listDataNodeToString(Cluster cl) {
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (DataNode dn : cl.getListDataNodeList()) {
            sb.append("{\nip=");
            sb.append(dn.getIp());
            sb.append("\nport=");
            sb.append(dn.getPort());
            sb.append("\nlocal=");
            sb.append(dn.getLocal());
            sb.append("\n}");
        }

        sb.append("]");

        return sb.toString();
    }
}
