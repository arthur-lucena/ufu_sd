package br.ufu.sd.work.util.commands;

import br.ufu.sd.work.util.commands.api.ICommand;

public class Delete implements ICommand {

    @Override
    public void run(String[] args) {
        System.out.println("executando commando de delete com os argumentos" + args);
    }
}