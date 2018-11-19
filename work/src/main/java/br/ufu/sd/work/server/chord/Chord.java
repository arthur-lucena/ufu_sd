package br.ufu.sd.work.server.chord;

import br.ufu.sd.work.server.Server;

import java.util.logging.Logger;

public class Chord {
    private static final Logger logger = Logger.getLogger(Chord.class.getName());


//    Particionamento O particionamento da responsabilidade sobre os dados seguirá o esquema de anel lógico definido pelo Chord.
//
//    Seja n o número de nós a serem colocados no sistema na execução de testes.
//    Cada servidor é identificado por um número de m bits.
//    O primeiro nó a entrar no sistema recebe necessariamente o identificador 2m − 1
//    O nó seguinte recebe identificador menor 2m/n que o anterior.
//    Seja uma sequência de nós com identificadores X < Y < Z. O nó Y é responsável pelos dados com chaves na faixa (X, Y].

    public void calcChord() {
        Integer numberOfNodes = 5; // n
        Long idBits = 64l; // m

        Long idFirstServerNode = (2 * idBits) - 1;
        Long nextServerNode = (2 * idBits) / numberOfNodes;

        logger.info(numberOfNodes.toString());
        logger.info(idBits.toString());
        logger.info(idFirstServerNode.toString());
        logger.info(nextServerNode.toString());
    }
}
