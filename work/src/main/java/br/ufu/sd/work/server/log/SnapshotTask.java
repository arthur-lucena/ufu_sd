package br.ufu.sd.work.server.log;

import br.ufu.sd.work.client.Client;

import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.logging.Logger;

public class SnapshotTask extends TimerTask {

    private LogManager logManager;
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    public SnapshotTask(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void run() {
        logger.info("TASK: creating new snapshot file at: " + LocalDateTime.now().toString());
        logManager.snapshot();
    }
}
