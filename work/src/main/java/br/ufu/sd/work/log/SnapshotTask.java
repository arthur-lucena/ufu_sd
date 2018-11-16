package br.ufu.sd.work.log;

import java.util.TimerTask;

public class SnapshotTask extends TimerTask {

    private LogManager logManager;

    public SnapshotTask(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void run() {
        logManager.createSnapshot();
    }
}
