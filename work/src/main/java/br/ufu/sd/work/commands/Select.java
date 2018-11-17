package br.ufu.sd.work.commands;

import br.ufu.sd.work.commands.api.ICommand;
import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import io.grpc.stub.StreamObserver;

public class Select implements ICommand {
    String response;
    Integer count = 0;

    @Override
    public void exec(StreamObserver so, Dictionary dictionary) {
//        System.out.println("executing select command for id: " + osc.getMessageCommand().getObjectId());
//
//        Integer found = 0;
//        Long objectId = osc.getMessageCommand().getObjectId();
//        for (Long key : dictionary.getData().keySet()) {
//            if (key.equals(objectId)) {
//
//                Metadata foundObject = (Metadata) deserialize(dictionary.getData().get(key));
//                found = found + 1;
//                System.out.println("object with Id: " + objectId + " found");
//                osc.getMessageCommand().setStreamObserver("object with Id: " + objectId + " found: " + foundObject.toString());
//            }
//        }
//
//        if (found == 0) {
//            System.out.println("object with Id: " + objectId + " not found");
//            osc.getMessageCommand().setStreamObserver("not found");
//        }
    }

    @Override
    public void log(LogManager logManager) {

    }
}
