package com.simple_queue;

public class QueueSchedulerController {
    private Scheduler scheduler;

    QueueSchedulerController() {

        this.scheduler = Scheduler.getInstance();
    }

    public void run() {
        int MAX_NUMBER = 10000;
        scheduler.start(Config.getInstance().firstSeed());
        for (int i = 0; i < MAX_NUMBER; i++) {
            this.scheduler.next();
        }
    }
}