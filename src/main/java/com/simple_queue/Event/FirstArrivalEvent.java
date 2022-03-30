package com.simple_queue.Event;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.simple_queue.Config;
import com.simple_queue.Scheduler;
import com.simple_queue.SimpleQueue;
import com.simple_queue.Timer;

public class FirstArrivalEvent extends AbstractEvent {
    static int[] arrivalArray = Config.getInstance().getArrivalInterval();

    public FirstArrivalEvent(double time) {
        super(time);
    }

    @Override
    public void execute(SimpleQueue queue) {
        SimpleQueue singletonQueue = SimpleQueue.getInstance();
        int maxSize = singletonQueue.getMaxSize();
        int currentSize = singletonQueue.getCurrentSize();
        // Lua is everything for me <3 🥺

        if (currentSize < maxSize) {
            Timer.getInstance().accountTimer(this.getTime(), 1);
            queue.increaseQueue();
            if (currentSize <= 1) {
                AbstractEvent event = new DropoutEvent();
                Scheduler.getInstance().addEvent(event);
            }
        }
        AbstractEvent event = new ArrivalEvent();

        event.setFinished(false);
        Scheduler.getInstance().addEvent(event);

    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        NumberFormat nFormat = new DecimalFormat("#0.0000");

        stringBuilder.append("ARRIVAL").append("\t"+nFormat.format(this.getTime()));

        return stringBuilder.toString();
    }
}
