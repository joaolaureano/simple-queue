package com.simple_queue.Event;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.simple_queue.Config;
import com.simple_queue.Scheduler;
import com.simple_queue.SimpleQueue;
import com.simple_queue.Timer;

public class DropoutEvent extends AbstractEvent {
    static int[] dropoutArray = Config.getInstance().getDropoutInterval();

    public DropoutEvent() {
            super(calculateTime(dropoutArray, Config.getInstance().nextSeed()));
    }

    @Override
    public void execute(SimpleQueue queue) {
        if (queue.getCurrentSize() != 0) {
            SimpleQueue singletonQueue = SimpleQueue.getInstance();
            int currentSize = singletonQueue.getCurrentSize();
            Timer.getInstance().accountTimer(this.getTime(), -1);
            queue.decreaseQueue();
            if (currentSize >= 1) {
                if (Config.getInstance().hasNewSeed()){
                    AbstractEvent event = new DropoutEvent();
                    
                    Scheduler.getInstance().addEvent(event);
                }
                return;
            }
        }
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        NumberFormat nFormat = new DecimalFormat("#0.0000");

        stringBuilder.append("DROPOUT").append("\t"+nFormat.format(this.getTime()));

        return stringBuilder.toString();
    }
}
