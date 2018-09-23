package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Delete implements ICommand {
    Integer count = 0;

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary, Long insertID) {
        String[] args = osc.getMessageCommand().getArgs();
        System.out.println("executando commando de delete com os argumentos" + args);
        String formattedInsert = String.join(",",args);

        for(Long key  : dictionary.getData().keySet()){
            if(Arrays.equals(formattedInsert.getBytes(),dictionary.getData().get(key))) {

                dictionary.getData().remove(key);
                count = count + 1;
                System.out.println("delete realizado");

            }
        }

        if(count == 0){
            osc.getMessageCommand().setResponse("Dado não foi encontrado.");
        }
    }
}
