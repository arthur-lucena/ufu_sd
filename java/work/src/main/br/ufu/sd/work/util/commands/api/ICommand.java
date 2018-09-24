package br.ufu.sd.work.util.commands.api;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.server.OutputStreamCommand;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface ICommand extends Serializable {
    void run(OutputStreamCommand osc, Dictionary dictionary);
}
