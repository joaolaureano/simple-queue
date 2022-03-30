package com.simple_queue.Event;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.simple_queue.Config;
import com.simple_queue.Scheduler;
import com.simple_queue.SimpleQueue;
import com.simple_queue.Timer;

public class ArrivalEvent extends AbstractEvent {
    static int[] arrivalArray = Config.getInstance().getArrivalInterval();

    public ArrivalEvent() {
        super(calculateTime(arrivalArray, Config.getInstance().nextSeed()));
    }

    @Override
    public void execute(SimpleQueue queue) {

        int maxSize = queue.getMaxSize();
        int currentSize = queue.getCurrentSize();
        if (currentSize < maxSize) {
            Timer.getInstance().accountTimer(this.getTime(), 1);
            queue.increaseQueue();
            currentSize++;
            if (currentSize <= 1) {
                if (Config.getInstance().hasNewSeed()) {
                    AbstractEvent event = new DropoutEvent();
                    Scheduler.getInstance().addEvent(event);
                }
            }
        }

        if (Config.getInstance().hasNewSeed()) {

            AbstractEvent event = new ArrivalEvent();

            Scheduler.getInstance().addEvent(event);
        }

    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        NumberFormat nFormat = new DecimalFormat("#0.0000");

        stringBuilder.append("ARRIVAL").append("\t" + nFormat.format(this.getTime()));

        return stringBuilder.toString();
    }
}
