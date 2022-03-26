package com.simple_queue;

public class QueueSchedulerController {
    private Scheduler scheduler;
    private SimpleQueue queue;

    QueueSchedulerController() {

        this.scheduler = Scheduler.getInstance();
        this.queue = SimpleQueue.getInstance();
    }

    public void run() {

        scheduler.start(5);
        scheduler.start(2);
        scheduler.start(7);
        scheduler.start(1);
        scheduler.start(8);
        scheduler.start(9);
        scheduler.start(7);
        
        this.scheduler.next();
        this.scheduler.next();
        this.scheduler.next();
        this.scheduler.next();
        this.scheduler.next();
        this.scheduler.next();
        this.scheduler.next();


        System.out.println(this.queue);
    }

}