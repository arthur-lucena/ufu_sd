package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Insert implements ICommand {

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary) {
        String[] args = osc.getMessageCommand().getArgs();
        String formattedInsert = String.join(",", args);
        System.out.println("executando commando de insert com os argumentos" + args);
        dictionary.getData().put(osc.getMessageCommand().getObjectId(), formattedInsert.getBytes());
        System.out.println("inserção realizada" + args);
        osc.getMessageCommand().setResponse("Insert realizado!");

    }
}
