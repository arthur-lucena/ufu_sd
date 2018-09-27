package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;
import static br.ufu.sd.work.model.Metadata.fromCommand;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class Insert implements ICommand {

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary) {
        Integer duplicate = 0;
        String[] args = osc.getMessageCommand().getArgs();
        Long objectId = osc.getMessageCommand().getObjectId();
        for(Long key : dictionary.getData().keySet()){
            if(key.equals(objectId)) {
                duplicate = duplicate + 1;
            }
        }

        if(duplicate == 0) {
            System.out.println("executando commando de insert com os argumentos" + args);
            Metadata metadata = fromCommand(osc.getMessageCommand());
            dictionary.getData().put(osc.getMessageCommand().getObjectId(), serialize(metadata));
            System.out.println("inserção realizada" + metadata);
            osc.getMessageCommand().setResponse("inserção realizada: " + metadata);
        } else {
            System.out.println("object with Id: " + objectId + " not created");
            osc.getMessageCommand().setResponse("Id existing");
        }
    }
}
