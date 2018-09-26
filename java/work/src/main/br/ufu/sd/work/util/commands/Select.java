package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.apache.commons.lang3.SerializationUtils.deserialize;

public class Select implements ICommand {
    String response;
    Integer count = 0;

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary) {
        System.out.println("executing select command for id: " + osc.getMessageCommand().getObjectId());

        Integer found = 0;
        Long objectId = osc.getMessageCommand().getObjectId();
        for(Long key : dictionary.getData().keySet()){
            if(key.equals(objectId)) {

                Metadata foundObject = (Metadata) deserialize(dictionary.getData().get(key));
                found = found + 1;
                System.out.println("object with Id: " + objectId + " found");
                osc.getMessageCommand().setResponse("object with Id: " + objectId + " found: " + foundObject.toString());
            }
        }

        if(found == 0){
            System.out.println("object with Id: " + objectId + " not found");
            osc.getMessageCommand().setResponse("not found");
        }
    }
}
