package br.ufu.sd.work.commands;

import br.ufu.sd.work.commands.api.ICommand;
import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import io.grpc.stub.StreamObserver;

public class Delete implements ICommand {


    @Override
    public void exec(StreamObserver so, Dictionary dictionary) {
//        System.out.println("executing delete command for id: " + osc.getMessageCommand().getObjectId());
//
//        Integer deleted = 0;
//        Long objectId = osc.getMessageCommand().getObjectId();
//        for (Long key : dictionary.getData().keySet()) {
//            if (key.equals(objectId)) {
//
//                dictionary.getData().remove(key);
//                deleted = deleted + 1;
//                System.out.println("objected with Id: " + objectId + " deleted");
//                osc.getMessageCommand().setStreamObserver("objected with Id: " + objectId + " deleted");
//            }
//        }
//
//        if (deleted == 0) {
//            System.out.println("objected with Id: " + objectId + " not found");
//            osc.getMessageCommand().setStreamObserver("not found");
//        }
    }

    @Override
    public void log(LogManager logManager) {

    }
}
