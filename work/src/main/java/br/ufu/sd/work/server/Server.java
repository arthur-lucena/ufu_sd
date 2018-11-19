package br.ufu.sd.work.server;

import br.ufu.sd.work.grpc.service.ServiceDelete;
import br.ufu.sd.work.grpc.service.ServiceInsert;
import br.ufu.sd.work.grpc.service.ServiceSelect;
import br.ufu.sd.work.grpc.service.ServiceUpdate;
import br.ufu.sd.work.log.LogManager;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.Metadata;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.apache.commons.lang3.SerializationUtils.serialize;


public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private BlockingQueue<ResponseCommand> queueOne;

    private String logFilePath;
    private String snapshotFilePath;
    private Integer serverId;
    private String startKeyRange;
    private String endKeyRange;
    private Dictionary dictionary = new Dictionary(new ConcurrentHashMap<>());
    private LogManager logManager;

    private io.grpc.Server server;
    private Integer serverPort;


    public Server(String configurationFileName) {
        configure(configurationFileName);
        this.logManager = new LogManager(logFilePath, snapshotFilePath, serverId);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server("configuration.properties");
        server.start();
        server.blockUntilShutdown();
    }

    public void start() throws IOException {
        queueOne = new ArrayBlockingQueue<>(1000000);

        QueueOneConsumption queueOneConsumption = new QueueOneConsumption(queueOne, dictionary, logManager);

        createLogFileIfNeeded();
        recreateDictionaryIfNeeded();

        Thread threadStarter = new Thread(queueOneConsumption);
        threadStarter.start();

        server = ServerBuilder.forPort(serverPort)
                .addService(new ServiceInsert(queueOne))
                .addService(new ServiceSelect(queueOne))
                .addService(new ServiceDelete(queueOne))
                .addService(new ServiceUpdate(queueOne))
                .build()
                .start();

        logger.info("Server running on port :  " + serverPort);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down the server when the JVM shut down");
            Server.this.stop();
            System.err.println("*** server shut down");
        }
        ));
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void createLogFileIfNeeded() {
        logManager.createFile();
    }

    private void recreateDictionaryIfNeeded() {
        if (dictionary.getData().isEmpty()) {
            LinkedHashMap<Long, Metadata> loggedData = logManager.recoverInformation();
            if(!loggedData.isEmpty()) {
                loggedData.forEach((k, v) -> dictionary.getData().put(k, serialize(v)));
                List<Long> ids = new ArrayList<>(loggedData.keySet());
                Collections.reverse(ids);
            }
        }
    }

    private void configure(String configurationFileName) {
        Configuration configuration = new Configuration(configurationFileName);
        Properties props = configuration.getProp();
        logFilePath = props.getProperty("server.log.file.path");
        logFilePath = props.getProperty("server.log.snapshot.file.path");
        startKeyRange = props.getProperty("server.key.range").split("-")[0];
        endKeyRange =  props.getProperty("server.key.range").split("-")[1];
        serverPort = Integer.valueOf(props.getProperty("server.port"));
        serverId = Integer.valueOf(props.getProperty("server.id"));
    }

}
