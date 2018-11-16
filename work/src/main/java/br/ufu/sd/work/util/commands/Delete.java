package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

public class Delete implements ICommand {


    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary) {
        System.out.println("executing delete command for id: " + osc.getMessageCommand().getObjectId());

        Integer deleted = 0;
        Long objectId = osc.getMessageCommand().getObjectId();
        for(Long key : dictionary.getData().keySet()){
            if(key.equals(objectId)) {

                dictionary.getData().remove(key);
                deleted = deleted + 1;
                System.out.println("objected with Id: " + objectId + " deleted");
                osc.getMessageCommand().setResponse("objected with Id: " + objectId + " deleted");
            }
        }

        if(deleted == 0){
            System.out.println("objected with Id: " + objectId + " not found");
            osc.getMessageCommand().setResponse("not found");
        }
    }
}
