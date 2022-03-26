package com.simple_queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Scheduler {

    private static Scheduler scheduler;
    private ArrayList<Event> queue = new ArrayList<Event>();

    public static Scheduler getInstance() {
        if (scheduler == null)
            scheduler = new Scheduler();
        return scheduler;

    }

    public Event start(double time) {
        this.queue = new ArrayList<Event>();

        EventEnum eventType = EventEnum.ARRIVAL;
        Event event = new Event(eventType, time);
        event.setFinished(false);
        this.queue.add(event);

        return event;
    }

    public double calculate(int[] interval, double randomNumber) {
        return interval[1] - interval[0] * randomNumber + interval[0];
    }

    public void arrival() {
        SimpleQueue singletonQueue = SimpleQueue.getInstance();
        int maxSize = singletonQueue.getMaxSize();
        int currentSize = singletonQueue.getCurrentSize();
        if (currentSize < maxSize) {
            singletonQueue.increaseQueue();
            if (currentSize <= 1) {
                // EventEnum eventType = EventEnum.DROPOUT;
                // Event event = new Event(eventType, time);
                // event.setFinished(false);
                // this.queue.add(event);
                // agendar saida
            }
        }
        // EventEnum eventType = EventEnum.ARRIVAL;
        // Event event = new Event(eventType, time);
        // event.setFinished(false);
        // this.queue.add(event);
        // agendar entrada
    }

    public void dropout() {
        SimpleQueue singletonQueue = SimpleQueue.getInstance();
        singletonQueue.decreaseQueue();
        int currentSize = singletonQueue.getCurrentSize();
        if (currentSize >= 1)
            // EventEnum eventType = EventEnum.DROPOUT;
            // Event event = new Event(eventType, time);
            // event.setFinished(false);
            // this.queue.add(event);
            // agendar saida
            return;

    }

    public void next() {
        SimpleQueue singletonQueue = SimpleQueue.getInstance();
        Collections.sort(queue, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                return (Double.valueOf(event1.getTime())).compareTo((Double.valueOf(event2.getTime())));
            }
        });
        for (Event event : queue) {
            if (event.getFinished())
                continue;

            event.setFinished(true);
            if (singletonQueue.addEvent(event)) {
                switch (event.getEventType()) {
                    case ARRIVAL:
                        System.out.println(event);
                        break;
                    case DROPOUT:
                        System.out.println(event);
                        break;
                }
                break;
            }
        }
    }
}