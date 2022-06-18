package com.simple_queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Escalonador {

    private static Escalonador single_instance = new Escalonador();

    public Clock clock = Clock.getInstance();

    public double[] seeds;
    int indexSeed = 0;

    public ArrayList<Event> scheduledEvents;

    public Map<Integer, Double> initials = new HashMap<>();

    public int indexRound = 0;

    public Queue[] queues;

    private double lastEventTime = 0;

    private double time = 0;

    public static Escalonador getInstance() {
        return single_instance;
    }

    public double getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(double lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    public double getTime() {
        return time;
    }

    public void initialize(Queue[] queues) throws Exception {
        this.queues = queues;
        this.scheduledEvents = new ArrayList<Event>();
        for (Entry<Integer, Double> entry : initials.entrySet()) {
            Queue queue = queues[entry.getKey()];
            Event event = new Event(EventType.ARRIVAL, entry.getValue(), queue, null);
            this.scheduledEvents.add(event);
        }
        Collections.sort(this.scheduledEvents);
    }

    public void round() throws Exception {

        Event nextEvent = this.extractLastEvent();

        this.lastEventTime = this.time;
        this.time = nextEvent.time;

        switch (nextEvent.type) {
            case ARRIVAL:
                nextEvent.origin.chegada(true);
                return;
            case DEPARTURE:
                nextEvent.origin.saida();
                if (nextEvent.destiny != null)
                    nextEvent.destiny.chegada(false);
                return;
        }
    }

    public double calculate(double[] interval, double seed) {
        return (interval[1] - interval[0]) * seed + interval[0];
    }

    public Event agendaChegada(Queue queue, double nextSeed) {
        double nextTime = this.time + calculate(queue.arrivalInterval, nextSeed);
        EventType type = EventType.ARRIVAL;
        Event event = new Event(type, nextTime, queue, null);
        this.scheduledEvents.add(event);
        Collections.sort(this.scheduledEvents);
        return event;
    }

    public Event agendaSaida(Queue queue, Queue destiny, double nextSeed) {
        double nextTime = this.time + calculate(queue.departureInterval, nextSeed);
        EventType type = EventType.DEPARTURE;
        Event event = new Event(type, nextTime, queue, destiny);
        this.scheduledEvents.add(event);
        Collections.sort(this.scheduledEvents);
        return event;
    }

    public void agendamentoInicial(int index, double nextSeed) {
        this.initials.put(index, Double.valueOf(nextSeed));
    }

    public double nextSeed() throws EndOfSeedsException {
        return seeds[indexSeed++];
    }

    public Event extractLastEvent() {
        return this.scheduledEvents.remove(0);
    }

}
