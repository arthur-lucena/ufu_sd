package br.ufu.sd.work.client;

import br.ufu.sd.work.DeleteRequest;
import br.ufu.sd.work.InsertRequest;
import br.ufu.sd.work.SelectRequest;
import br.ufu.sd.work.UpdateRequest;
import br.ufu.sd.work.request.ExecuteDelete;
import br.ufu.sd.work.request.ExecuteInsert;
import br.ufu.sd.work.request.ExecuteSelect;
import br.ufu.sd.work.request.ExecuteUpdate;
import br.ufu.sd.work.model.ETypeCommand;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 54666;

    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private ManagedChannel channel;

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        channel = ManagedChannelBuilder.forAddress(IP, PORT)
                .usePlaintext().build();

        Scanner s = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("type a command: (insert <id:value> | update <id:value> | delete <id> | select <id>) - exit : to exit");
            String allCommand = s.nextLine();

            String[] allCommandArray = allCommand.split(" ");

            if (allCommandArray.length != 2) {
                logger.warning("invalid command");
                continue;
            }

            String stringCommand = allCommandArray[0];
            String[] args = allCommandArray[1].split(":");

            ETypeCommand command = ETypeCommand.fromString(stringCommand);

            if (command == null) {
                logger.warning("invalid command");
                continue;
            }

            switch (command) {
                case INSERT:
                    InsertRequest ir = InsertRequest.newBuilder()
                            .setId(Long.valueOf(args[0]))
                            .setValue(args[1])
                            .setIdClient("1")
                            .build();
                    ExecuteInsert ei = new ExecuteInsert(ir, channel, null);
                    Thread ti = new Thread(ei);
                    ti.start();
                    break;
                case UPDATE:
                    UpdateRequest ur = UpdateRequest.newBuilder()
                            .setId(Long.valueOf(args[0]))
                            .setValue(args[1])
                            .setIdClient("1")
                            .build();
                    ExecuteUpdate eu = new ExecuteUpdate(ur, channel, null);
                    Thread tu = new Thread(eu);
                    tu.start();
                    break;
                case DELETE:
                    DeleteRequest dr = DeleteRequest.newBuilder()
                            .setId(Long.valueOf(args[0]))
                            .setIdClient("1")
                            .build();
                    ExecuteDelete ed = new ExecuteDelete(dr, channel, null);
                    Thread td = new Thread(ed);
                    td.start();
                    break;
                case SELECT:
                    SelectRequest sr = SelectRequest.newBuilder()
                            .setId(Long.valueOf(args[0]))
                            .build();
                    ExecuteSelect es = new ExecuteSelect(sr, channel, null);
                    Thread ts = new Thread(es);
                    ts.start();
                    break;
                case EXIT:
                    running = false;

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    logger.warning("invalid command");
                    break;
            }
        }

        logger.info("exiting... =D");
    }
}