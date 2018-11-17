package br.ufu.sd.work.commands;

import br.ufu.sd.work.commands.api.ICommand;
import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import io.grpc.stub.StreamObserver;

public class Update implements ICommand {

    @Override
    public void exec(StreamObserver so, Dictionary dictionary) {
//        System.out.println("executing update command for id: " + osc.getMessageCommand().getObjectId());
//
//        Metadata updateInformation = fromCommand(osc.getMessageCommand());
//
//        Integer updated = 0;
//        Long objectId = osc.getMessageCommand().getObjectId();
//        for (Long key : dictionary.getData().keySet()) {
//            if (key.equals(objectId)) {
//                dictionary.getData().put(key, serialize(updateInformation));
//                updated = updated + 1;
//                System.out.println("object with Id: " + objectId + " updated with information: " + updateInformation.toString());
//                osc.getMessageCommand().setStreamObserver("object with Id: " + objectId + " updated with information: " + updateInformation.toString());
//            }
//        }
//
//        if (updated == 0) {
//            System.out.println("object with Id: " + objectId + " not found");
//            osc.getMessageCommand().setStreamObserver("not found");
//        }
    }

    @Override
    public void log(LogManager logManager) {

    }
}
