package br.ufu.sd.work.server.chord;

import br.ufu.sd.work.ChordNode;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ChordNodeUtils {
    public boolean isMyResponsibility(ChordNode node, long id) {
        return node.getMinId() < id && id <= node.getMaxId();
    }

    public ManagedChannel getPossibleResponsibleChannel(ChordNode node, Long id) throws ChordException {
        if (id > node.getMaxId() || id <= node.getMinId()) {
            throw new ChordException("Invalid number, this number surpass MAX capacity");
        }

        ChordNode possibleNode;

        if (id <= node.getMinId()) {
            possibleNode = node.getPreviousNode();
        } else if (id > node.getMaxId()) {
            possibleNode = node.getNextNode();
        } else {
            possibleNode = node;
        }

        return ManagedChannelBuilder.forAddress(possibleNode.getIp(), possibleNode.getPort())
                .usePlaintext().build();
    }
}
