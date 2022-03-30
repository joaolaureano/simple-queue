package com.simple_queue;


public class Interface {
    QueueSchedulerController qController = new QueueSchedulerController();
    
    public void init(boolean useRandom) {

        Config.init(useRandom);
        
        qController.run();
        System.out.println(SimpleQueue.getInstance());
        System.out.println();
        System.out.println(Scheduler.getInstance());
        System.out.println();
        System.out.println(Timer.getInstance());
        
    }
}
