package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Update implements ICommand {

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary) {
        System.out.println("executing update command for id: " + osc.getMessageCommand().getObjectId());

        String args = osc.getMessageCommand().getArgs()[0];
        Integer updated = 0;
        Long objectId = osc.getMessageCommand().getObjectId();
        for(Long key : dictionary.getData().keySet()){
            if(key.equals(objectId)) {
                dictionary.getData().put(key, args.getBytes());
                updated = updated + 1;
                System.out.println("object with Id: " + objectId + " updated");
            }
        }

        if(updated == 0){
            System.out.println("object with Id: " + objectId + " not found");
            osc.getMessageCommand().setResponse("not found");
        }
    }
}
