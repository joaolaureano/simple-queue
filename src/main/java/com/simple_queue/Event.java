package com.simple_queue;

public class Event implements Comparable<Event> {
    private static int _index = 0;

    private int index = _index++;

    public Queue origin;
    public Queue destiny;

    public double time;
    public EventType type;

    public int getId() {
        return index;
    }

    public Event(EventType type, double time, Queue origin, Queue destiny) {
        this.type = type;
        this.time = time;
        this.origin = origin;
        this.destiny = destiny;
        this.index = _index++;
    }

    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    @Override
    public String toString() {
        return "Event [order=" + index + ", time=" + time + ", type=" + type + "]";
    }

}
