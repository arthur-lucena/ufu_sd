package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.OutputStreamCommand;
import br.ufu.sd.work.util.commands.api.ICommand;

import java.io.ObjectOutputStream;

public class Update implements ICommand {

    @Override
    public void run(OutputStreamCommand osc, Dictionary dictionary, Long insertID) {
        String[] args = osc.getMessageCommand().getArgs();
        System.out.println("executando commando de update com os argumentos" + args);
    }
}
