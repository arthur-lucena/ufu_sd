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
  
