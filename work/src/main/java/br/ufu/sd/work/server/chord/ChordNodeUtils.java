package br.ufu.sd.work.server.chord;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public abstract class ChordNodeUtils {
    public static boolean isMyResponsibility(ChordNode node, long id) throws ChordException {
        if (id > node.getMaxChordId()) {
            throw new ChordException("Invalid ID, this ID surpass MAX capacity.");
        }

        if (id < 0) {
            throw new ChordException("Invalid ID, can be below Zero.");
        }

        return (node.getMinId() < id && id <= node.getMaxId()) || (id == 0 && node.getLastNode());
    }

    public static ChannelNode toChannelNode(ChordNode node) {
        return ChannelNode.newBuilder().setIp(node.getIp()).setPort(node.getPort()).build();
    }

    public static ManagedChannel getPossibleResponsibleChannel(ChordNode node, Long id) {
        ChannelNode possibleChannelNode;

        if (id <= node.getMinId() && !node.getLastNode()) {
            possibleChannelNode = node.getPreviousNodeChannel();
        } else if (id > node.getMaxId()) {
            possibleChannelNode = node.getNextNodeChannel();
        } else {
            possibleChannelNode = toChannelNode(node);
        }

        return ManagedChannelBuilder.forAddress(possibleChannelNode.getIp(), possibleChannelNode.getPort())
                .usePlaintext().build();
    }
}
