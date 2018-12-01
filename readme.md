 ### Terceira entrega

**Motivação**
Agora que seu banco de dados está gigante, cheio de dados importantes, você não pode mais permitir que falhas levem à indisponibilidade de dados. 
Como discutido nas últimas semanas, replicação de máquinas de estados é uma forma correta e "simples", mesmo que não muito eficiente de tornar um serviço tolerante a falhas. Vocês aplicarão esta técnica na terceira entrega.

**Difusão Totalmente Ordenada**
- [ ] Para Implementar a máquina de estados replicada, você precisa da implementação de um algoritmo de difusão totalmente ordenada. Dado que todos os trabalhos ou foram implementados em Python ou em Java, recomendo que usem o Atomix (visto em sala de aula, para Java) ou o OpenReplica (http://openreplica.org/, para Python). Ambos funcionam de forma muito parecida, então a dificuldade será a mesma em ambos os casos.

**O quê fazer?**
- [ ] Cada servidor do seu sistema é um ponto simples de falhas (SPOF) para os dados pelos quais é responsável. Para remover este SPOF, vocês usarão a implementação da difusão totalmente ordenada escolhida, replicando cada servidor em 3 vias.

**Topologia**
- [ ] Seja um anel como construído para a segunda entrega com N servidores. Na terceira entrega, haverão N*3 servidores, sendo que cada 3 servidores ocupam a mesma posição no anel.

**Comunicação**
- [ ] A comunicação entre cliente e servidores continua por gRPC. O cliente se conecta a qualquer servidor que conheça.
- [ ] Ao receber uma requisição, o servidor decide-se por executá-la ou por passá-la adiante para outro servidor. Se for passá-la adiante, usa gRPC.
- [ ] Se for executá-la, procede como antes para que seja logada. Contudo, em vez de fazer como antes para que seja executada, usa a difusão confiável para propagar a requisição para as 3 réplicas (inclusive si mesmo).
- [ ] Ao receber a requisição via difusão confiável, o servidor simplesmente a executa, tendo a certeza de que suas réplicas farão o mesmo.
- [ ] Operações de leitura também deverão ser feitas via o framework de difusão atômica. No caso do Atomix, por exemplo, usando uma query.

**Testes**

Os testes a serem executados são os mesmos das entregas anteriores, mais o teste do cenário em que réplicas de cada faixa de chaves são reiniciada repetidamente, sendo que no máximo uma réplica por faixa é reiniciada por vez, e enquanto operações de escrita e leitura são feitas nas faixas correspondentes.
 
 
 ### Segunda entrega
 
 
**Entrega 2** 
Em época de Big Data, um banco de dados com apenas um servidor é uma nulidade. Como armazenar meus milhões e milhões de registros? É necessário espalhar os dados por múltiplos servidores, e para tanto usaremos como modelo o funcionamento do Chord. 

**Roteamento** 
De acordo com a especificação da entrega anterior, cada requisição é colocada em uma fila F1, de onde é re-enfileirada nas filas F2 e F3. 

- [x] Para esta entrega, antes de re-enfileirar a mensagem, o servidor deverá analisar se é realmente responsabilidade deste servidor.
- [x] Caso o seja, a requisição é re-enfileirada em F2 e F3. Caso contrário, será colocada em uma fila F4. 
- [x] Um thread retira de F4 e invoca, consultando uma tabela de roteamento, o nó responsável pela requisição para que a processe ou que pelo menos esteja mais próximo que o mesmo. 
- [x] O servidor primeiro contactado pelo cliente é o responsável por enviar a resposta para o cliente. 

(verificar se o id é de responsabilidade do servidor, se sim seguir o fluxo normal, caso contrário jogar na fila de roteamento e repassar ao servidor adequado)

**Particionamento** 
O particionamento da responsabilidade sobre os dados seguirá o esquema de anel lógico definido pelo Chord. 
- [x] Seja n o número de nós a serem colocados no sistema na execução de testes. 
- [x] Cada servidor é identificado por um número de m bits. 
- [x] O primeiro nó a entrar no sistema recebe necessariamente o identificador 2m − 1
- [x] O nó seguinte recebe identificador menor 2m/n que o anterior. 
- [x] Seja uma sequência de nós com identificadores X < Y < Z. O nó Y é responsável pelos dados com chaves na faixa (X, Y].

**Comunicação** 
- [x] Toda comunicação deve ser agora feira usando gRPC. Cada operação é realizada via uma função diferente (i.e., há uma função para C, outra para R, ...). 
- [x] Servidores redirecionam requisições também usando gRPC, usando a mesma interface usada por clientes. 
- [x] Toda requisição é executada assincronamente do ponto de vista de quem invoca a requisição. 
- [ ] Uma requisição é redirecionada para o nó seguinte ou anterior, dependendo de qual o caminho mais curto até o nó responsável pelos dados. 
- [x] Múltiplos saltos podem ser necessários até que a requisição seja respondida.

**Tratamento de falhas** 
- [ ] Assuma que não haverão falhas permanentes ou envio de requisições enquanto algum nó estiver falho. 
- [ ] Nós podem ser reiniciados e, como na primeira entrega, devem ter seu estado recuperado pelo uso do log de operações e de snapshot do banco de dados.

**Log + Snapshot** 
- [x] Para evitar que o log se torne grande demais, frequentemente serão feitos snapshots do estado atual do banco de dados. 
- [x] Um snapshot do banco captura o estado atual do mesmo, em arquivo, e portanto torna desnecessário o arquivo de logs contendo as operações anteriores ao snapshot.

**Snapshoting** 
- [x] A cada U segundos, o estado atual do banco será gravado em um arquivo nomeado snap.X, onde X é um contador de logs. Isto é, o primeiro snapshot será gravado como snap.1, o segundo como snap.2 e assim por diante. 
- [x] As operações executadas antes de um snapshot X serão gravadas e um arquivo de log.(X-1). O primeiro arquivo de logs será então o log.0. 
- [x] Uma vez iniciald o snapshot que cria snap.X, nenhuma nova operação será escrita em log.(X-1). Novas operações são escritas em log.X. 
- [x] Serão mantidos pelo sistema os últimos 3 arquivos de log e de snapshot. Isto é, se o último snapshot executado foi o décimo, então há no sistema os logs log.8, log.9. log.10 (sendo escrito), e os snapshots snap.8, snap.9, e snap.10.

 -----------------------------------------------
 
 ### Primeira entrega
 
 ## Cliente

 - interface via linha de comando 
 - conectar no servidor
 - enviar comando ao servidor

***Leitura de comando***
 - [x] 1 thread em loop infinito apresentando menu de comandos e lendo comandos do teclado 
 - [x] uma vez digitado um comando, o mesmo é validado
 - [x] se válido comando é enviado ao servidor
 - [x] se inválido,  mensagem de erro é apresentada
 - [x] o comando “sair” termina a execução deste thread

***Apresentação de respostas***
 - [x] 1 thread em loop infinito recebendo mensagens do servidor
 - [x] uma vez recebida uma mensagem, a mesma é apresentada na tela
 - [x] uma vez terminado o thread de leitura de comandos, espera-se pelo menos 5 segundos por novas mensagens do servidor e então se termina este     thread

## Servidor

 - enfilera commandos enviados pelo cliente
 - aguarda conexões de cliente
 - retorna para o cliente uma msg de sucesso após o processamento do comando

***Processamento dos comandos***
 - [x] 1 ou mais threads recebendo comandos e colocando em uma fila F1
 - [x] 1 thread consumindo comandos de F1 e colocando cópias do comando em uma fila F2 e em outra fila F3
 - [x] 1 thread consumindo comandos de F2 e gravando-os em disco.
 - [x] 1 thread consumindo de F3 aplicando o comando no banco de dados.

***Gravação dos comandos em um arquivo de log***
 - [x] mantendo o arquivo aberto durante a execução do programa
 - [x] adicionando comandos sempre ao fim do arquivo
 - [x] somente se o comando altera a base de dados (Reads são descartados)

***Execução dos comando***
 - [x] contra o mapa(?)
 - [x] emitindo mensages de sucesso (create/update/delete)
 - [x] respondendo com informação solicitada (read)
 - [x] emitindo erros quando adequado (create/update/delete/read)
 - [x] na ordem em que os comandos foram enfileirados em F3

**Tolerância a falhas**
Como o mapa é mantido em memória, no caso de falhas, todo o banco apagado. Para recuperá-lo

 - [x] Na reinicialização do processo 
 - [x] abra o arquivo de log
 - [x] e processe-o na sequência em que foi escrito
 - [x] reexecutando todas as operações gravadas 
 - [x] antes de aceitar novas requisições de clientes.

**

## Observações

**
 - Testes devem ser escritos!!!!!!
 - Toda comunicação é feita via TCP. 
 - E o canal de comuniação com o cliente é mantido aberto enquanto o mesmo estiver executando. 
 - Todas as portas usadas na comunicação são especificadas via arquivos de
   configuração. 
