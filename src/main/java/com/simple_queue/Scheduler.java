package com.simple_queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.simple_queue.Event.AbstractEvent;
import com.simple_queue.Event.FirstArrivalEvent;
import com.simple_queue.Event.OrderEvent;

public class Scheduler {

    private static Scheduler scheduler;
    public ArrayList<OrderEvent> queue = new ArrayList<OrderEvent>();
    private int indexOrder = 0;

    public static Scheduler getInstance() {
        if (scheduler == null)
            scheduler = new Scheduler();
        return scheduler;

    }

    public AbstractEvent start(double time) {
        this.queue = new ArrayList<OrderEvent>();

        AbstractEvent event = new FirstArrivalEvent(time);
        event.setTime(time);
        event.setFinished(true);
        SimpleQueue.getInstance().addEvent(event);
        addEvent(event);
        this.queue.get(0).order = indexOrder;
        this.queue.get(0).orderCreated = indexOrder;

        indexOrder++;
        event.execute(SimpleQueue.getInstance());

        return event;
    }

    public void addEvent(AbstractEvent event) {
        OrderEvent oEvent = new OrderEvent(event);
        oEvent.order = -1;
        oEvent.orderCreated = indexOrder;
        this.queue.add(oEvent);
    }

    public AbstractEvent next() {
        
        SimpleQueue singletonQueue = SimpleQueue.getInstance();
        AbstractEvent lastEvent = getLast();
        Collections.sort(queue, new Comparator<OrderEvent>() {
            @Override
            public int compare(OrderEvent event1, OrderEvent event2) {
                return (Double.valueOf(event1.event.getTime())).compareTo((Double.valueOf(event2.event.getTime())));
            }
        });
        
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).order != -1)
                continue;
            if(queue.get(i).event.getTime() < lastEvent.getTime() )
                continue;
            AbstractEvent event = queue.get(i).event;
            if (singletonQueue.addEvent(event)) {
                queue.get(i).order = indexOrder;
                indexOrder++;
                event.setFinished(true);
                event.execute(SimpleQueue.getInstance());
                return event;
            }
        }
        return null;
    }

    public AbstractEvent getLast() {
        Collections.sort(queue, new Comparator<OrderEvent>() {
            @Override
            public int compare(OrderEvent event1, OrderEvent event2) {
                return (Integer.valueOf(event2.order)).compareTo((Integer.valueOf(event1.order)));
            }
        });

        return queue.get(0).event;
    }

    public String toString() {
        Collections.sort(queue, new Comparator<OrderEvent>() {
            @Override
            public int compare(OrderEvent event1, OrderEvent event2) {
                return (Integer.valueOf(event1.order)).compareTo((Integer.valueOf(event2.order)));
            }
        });

        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("Scheduler information")
                .append("\nAll scheduled events. When ORDER TRIGGERED is -1, event was NOT triggered\n")
                .append("EVENT\t").append("TIME\t").append("ORDER TRIGGERED\t").append("ORDER SCHEDULED\n");
            
        for (OrderEvent oEvent : queue) {
            stringBuilder.append(oEvent);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}