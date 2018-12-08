package br.ufu.sd.work.client;

import br.ufu.sd.work.*;
import br.ufu.sd.work.model.ETypeCommand;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.logging.Logger;

public class Client {

    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private CrudServiceGrpc.CrudServiceStub stub;

    public static void main(String[] args) {
        new Client( "127.0.0.1", 51666).runOnTerminal();
    }

    public Client(String ip, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext().build();
        stub = CrudServiceGrpc.newStub(channel);
    }

    public void runOnTerminal() {
        Scanner s = new Scanner(System.in);
        boolean running = true;
        Request request = Request.getDefaultInstance();
        StreamObserverClient soc = new StreamObserverClient();

        while (running) {
            try {
                String allCommand = readCommand(s);
                ETypeCommand command = ETypeCommand.fromString(allCommand);
                String[] args;

                if (command == null) {
                    String[] allCommandArray = allCommand.split(" ");

                    if (allCommandArray.length != 2) {
                        logger.warning("invalid command");
                        continue;
                    }

                    String stringCommand = allCommandArray[0];
                    args = allCommandArray[1].split(":");

                    command = ETypeCommand.fromString(stringCommand);

                    if (command == null) {
                        logger.warning("invalid command");
                        continue;
                    }

                    if (args.length == 1) {
                        request = request.toBuilder()
                                .setId(Long.valueOf(args[0]))
                                .setValue("")
                                .setClient("terminal")
                                .build();
                    } else {
                        request = request.toBuilder()
                                .setId(Long.valueOf(args[0]))
                                .setValue(args[1])
                                .setClient("terminal")
                                .build();
                    }


                }

                running = sendCommand(request, command, soc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.info("exiting... =D");
    }

    public boolean sendCommand(Request request, ETypeCommand command, StreamObserver soc) {
        boolean running = true;

        switch (command) {
            case INSERT:
                stub.insert(request, soc);
                break;
            case UPDATE:
                stub.update(request, soc);
                break;
            case DELETE:
                stub.delete(request, soc);
                break;
            case SELECT:
                stub.select(request, soc);
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

        return running;
    }

    private String readCommand(Scanner s) {
        System.out.println("type a command: (insert <id:value> | update <id:value> | delete <id> | select <id>) - exit : to exit");
        return s.nextLine();
    }
}