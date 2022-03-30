package com.simple_queue.Event;

public class OrderEvent{
    public AbstractEvent event;
    public int order;
    public int orderCreated;

    public OrderEvent(AbstractEvent event) {
        this.event = event;
        this.order = -1;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.toString()).append("\t"+ this.order + "\t\t" + this.orderCreated);

        return stringBuilder.toString();
    }
}
