package com.simple_queue;

public class Event {

    private EventEnum eventType;
    private double time;
    private boolean finished;

    public Event(EventEnum eventType, double time) {
        this.eventType = eventType;
        this.finished = false;
        this.time = time;
    }

    public EventEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventEnum eventType) {
        this.eventType = eventType;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "Event [eventType=" + eventType + ", finished=" + finished + ", time=" + time + "]";
    }

    

}