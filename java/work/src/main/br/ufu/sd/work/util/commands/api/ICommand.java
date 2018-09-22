package br.ufu.sd.work.util.commands.api;

import br.ufu.sd.work.model.Dictionary;

import java.io.Serializable;

public interface ICommand extends Serializable {
    void run(String[] args, Dictionary dictionary);
}
