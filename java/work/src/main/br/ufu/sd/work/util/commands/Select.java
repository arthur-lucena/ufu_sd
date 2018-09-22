package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.util.commands.api.ICommand;

public class Select implements ICommand {

    @Override
    public void run(String[] args, Dictionary dictionary) {
        System.out.println("executando commando de select com os argumentos" + args);
    }
}
