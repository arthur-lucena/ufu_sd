package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.util.commands.api.ICommand;

public class Update implements ICommand {

    @Override
    public void run(String[] args) {
        System.out.println("executando commando de update com os argumentos" + args);
    }
}