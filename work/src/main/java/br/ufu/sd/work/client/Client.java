package br.ufu.sd.work.client;

import br.ufu.sd.work.*;
import br.ufu.sd.work.client.request.ExecuteDelete;
import br.ufu.sd.work.client.request.ExecuteSelect;
import br.ufu.sd.work.model.ETypeCommand;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 51666;

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

        StreamObserver<Response> so = new StreamObserver<Response>() {
            @Override
            public void onNext(Response value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };

        CrudServiceGrpc.CrudServiceStub stub = CrudServiceGrpc.newStub(channel);

        while (running) {
            try {
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

                        stub.insert(ir, so);

                        break;
                    case UPDATE:
                        UpdateRequest ur = UpdateRequest.newBuilder()
                                .setId(Long.valueOf(args[0]))
                                .setValue(args[1])
                                .setIdClient("1")
                                .build();

                        stub.update(ur, so);
                        break;
                    case DELETE:
                        DeleteRequest dr = DeleteRequest.newBuilder()
                                .setId(Long.valueOf(args[0]))
                                .setIdClient("1")
                                .build();

                        stub.delete(dr, so);

                        break;
                    case SELECT:
                        SelectRequest sr = SelectRequest.newBuilder()
                                .setId(Long.valueOf(args[0]))
                                .build();

                        stub.select(sr, so);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.info("exiting... =D");
    }
}