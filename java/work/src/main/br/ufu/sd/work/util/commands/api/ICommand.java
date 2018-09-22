package br.ufu.sd.work.util.commands.api;

import java.io.Serializable;

public interface ICommand extends Serializable {
    void run(String[] args);
}
