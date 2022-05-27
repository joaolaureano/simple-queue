package com.simple_queue;

public class Event {
    public Queue origin;
    public Queue destiny;
    public double time;
    public EventType type;
    public int order = -1;

    public Event(EventType type, double time, Queue origin, Queue destiny) {
        this.type = type;
        this.time = time;
        this.origin = origin;
        this.destiny = destiny;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Event [order=" + order + ", time=" + time + ", type=" + type + "]";
    }

}
