package com.simple_queue;

public class Event {
    public double time;
    public EventType type;
    public int order = -1;

    public Event(EventType type, double time) {
        this.type = type;
        this.time = time;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Event [order=" + order + ", time=" + time + ", type=" + type + "]";
    }

}
