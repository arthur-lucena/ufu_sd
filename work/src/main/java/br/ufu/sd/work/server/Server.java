package br.ufu.sd.work.server;

import br.ufu.sd.work.model.Dictionary;
import br.ufu.sd.work.model.Metadata;
import br.ufu.sd.work.model.ResponseCommand;
import br.ufu.sd.work.server.chord.ChordConnector;
import br.ufu.sd.work.server.chord.ChordException;
import br.ufu.sd.work.server.chord.ChordNodeWrapper;
import br.ufu.sd.work.server.configuration.Configuration;
import br.ufu.sd.work.server.log.LogManager;
import br.ufu.sd.work.server.log.SnapshotScheduler;
import br.ufu.sd.work.server.queue.QueueOneConsumption;
import br.ufu.sd.work.server.service.*;
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

    private Dictionary dictionary;
    private LogManager logManager;

    private io.grpc.Server server;

    private String logFilePath;
    private long snapshotTaskInterval;

    private String ip;
    private int firstPort;
    private int jumpNextPort;
    private int numberOfNodes;
    private int numberBitsId;

    private static volatile ChordNodeWrapper node;

    public Server(String configurationFileName) throws ChordException {
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
        ChordConnector chordConnector = new ChordConnector(this.ip, this.firstPort, this.jumpNextPort, this.numberOfNodes, this.numberBitsId);
        this.node = new ChordNodeWrapper();
        node.setByChordNode(chordConnector.connect());
    }

    private void start() throws IOException {
        BlockingQueue<ResponseCommand> queueOne = new ArrayBlockingQueue<>(1000000);

        QueueOneConsumption queueOneConsumption = new QueueOneConsumption(queueOne, dictionary, logManager, node);

        createLogFileIfNeeded();
        recreateDictionaryIfNeeded();
        SnapshotScheduler scheduler = new SnapshotScheduler(logManager, snapshotTaskInterval);
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
            if (!loggedData.isEmpty()) {
                loggedData.forEach((k, v) -> dictionary.getData().put(k, serialize(v)));
                List<Long> ids = new ArrayList<>(loggedData.keySet());
                Collections.reverse(ids);
            }
        }
    }

    private void configure(String configurationFileName) {
        Configuration configuration = new Configuration(configurationFileName);
        Properties props = configuration.getProp();
        this.ip = props.getProperty("server.chord-node.first-ip");
        this.firstPort = Integer.valueOf(props.getProperty("server.chord-node.first-port"));
        this.jumpNextPort = Integer.valueOf(props.getProperty("server.chord-node.jump-next-port"));
        this.numberOfNodes = Integer.valueOf(props.getProperty("server.chord-node.number-of-nodes"));
        this.numberBitsId = Integer.valueOf(props.getProperty("server.chord-node.number-of-bit-id"));

        this.logFilePath = props.getProperty("server.log.file.path");
        this.snapshotTaskInterval = Long.valueOf(props.getProperty("server.log.snapshot.interval"));
    }
}
