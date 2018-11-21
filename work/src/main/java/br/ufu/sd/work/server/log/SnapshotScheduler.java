package br.ufu.sd.work.server.log;

import java.util.Timer;
import java.util.TimerTask;

public class SnapshotScheduler {

    private LogManager logManager;
    private long snapShotTaskInterval;

    public SnapshotScheduler(LogManager logManager, long snapShotTaskInterval) {
        this.logManager = logManager;
        this.snapShotTaskInterval = snapShotTaskInterval;
    }

    public void scheduleTask() {
        TimerTask snapshotTask = new SnapshotTask(logManager);
        Timer timer = new Timer();
        timer.schedule(snapshotTask, 0, snapShotTaskInterval);
    }

}
