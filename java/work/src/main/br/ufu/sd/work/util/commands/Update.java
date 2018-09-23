package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Update implements ICommand {
    Integer count = 0;

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary, Long insertID) {
        String[] args = osc.getMessageCommand().getArgs();
        String oldValue = args[0];
        String newValue = args[1];

        for(Long key  : dictionary.getData().keySet()){
            if(Arrays.equals(oldValue.getBytes(),dictionary.getData().get(key))) {

                dictionary.getData().replace(key, newValue.getBytes());
                count = count + 1;
                System.out.println("update realizado");

            }
        }

        if(count == 0){
            osc.getMessageCommand().setResponse("Dado não foi encontrado.");
        }
    }
}
