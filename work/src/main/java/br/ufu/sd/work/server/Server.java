package br.ufu.sd.work.server;

import br.ufu.sd.work.server.chord.ChordConnector;
import br.ufu.sd.work.server.chord.ChordException;
import br.ufu.sd.work.server.chord.ChordNode;
import br.ufu.sd.work.server.service.*;
import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.server.log.SnapshotScheduler;
import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.server.configuration.Configuration;
import br.ufu.sd.work.server.queue.QueueOneConsumption;
import br.ufu.sd.work.model.ResponseCommand;
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
    private Integer serverId;
    private Dictionary dictionary;
    private LogManager logManager;

    private io.grpc.Server server;
    private Integer serverPort;
    private Integer snapshotTaskInterval;

    private ChordNode node;

    public Server(String configurationFileName) throws ChordException{
        configure(configurationFileName);
        connectionChord();

        this.dictionary = new Dictionary(new ConcurrentHashMap<>());
        this.logManager = new LogManager(logFilePath, node.getNodeId());
    }

    public static void main(String[] args) throws IOException, InterruptedException, ChordException {
        Server server = new Server("configuration.properties");
        server.start();
        server.blockUntilShutdown();
    }

    private void connectionChord() throws ChordException {
        this.node = new ChordConnector().connect();
    }

    private void start() throws IOException {
        queueOne = new ArrayBlockingQueue<>(1000000);

        QueueOneConsumption queueOneConsumption = new QueueOneConsumption(queueOne, dictionary, logManager);

        createLogFileIfNeeded();
        recreateDictionaryIfNeeded();
        SnapshotScheduler scheduler = new SnapshotScheduler(logManager, (long) snapshotTaskInterval);
        scheduler.scheduleTask();

        Thread threadStarter = new Thread(queueOneConsumption);
        threadStarter.start();

        server = ServerBuilder.forPort(node.getPort())
                .addService(new ServiceInsert(queueOne))
                .addService(new ServiceSelect(queueOne))
                .addService(new ServiceDelete(queueOne))
                .addService(new ServiceUpdate(queueOne))
                .addService(new ServiceChord(node))
                .build()
                .start();

        logger.info("Server running on port :  " + node.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down the server when the JVM shut down");
            Server.this.stop();
            System.err.println("*** server shut down");
        }
        ));
    }

    private void blockUntilShutdown() throws InterruptedException {
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
        logManager.createLogFile();
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
        serverPort = Integer.valueOf(props.getProperty("server.port"));
        serverId = Integer.valueOf(props.getProperty("server.id"));
        snapshotTaskInterval = Integer.valueOf(props.getProperty("server.log.snapshot.interval"));
    }

}
