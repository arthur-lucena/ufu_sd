package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import static br.ufu.sd.work.model.Metadata.fromCommand;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class Update implements ICommand {

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary) {
        System.out.println("executing update command for id: " + osc.getMessageCommand().getObjectId());

        Metadata updateInformation = fromCommand(osc.getMessageCommand());

        Integer updated = 0;
        Long objectId = osc.getMessageCommand().getObjectId();
        for(Long key : dictionary.getData().keySet()){
            if(key.equals(objectId)) {
                dictionary.getData().put(key, serialize(updateInformation));
                updated = updated + 1;
                System.out.println("object with Id: " + objectId + " updated with information: " + updateInformation.toString());
                osc.getMessageCommand().setResponse("object with Id: " + objectId + " updated with information: " + updateInformation.toString());
            }
        }

        if(updated == 0){
            System.out.println("object with Id: " + objectId + " not found");
            osc.getMessageCommand().setResponse("not found");
        }
    }
}
