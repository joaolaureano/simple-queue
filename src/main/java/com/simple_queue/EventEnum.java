package com.simple_queue;

public enum EventEnum {
    ARRIVAL("ARRIVAL"), DROPOUT("DROPOUT");

    public String eventName;

    EventEnum(String eventName) {
        this.eventName = eventName;
    }
}