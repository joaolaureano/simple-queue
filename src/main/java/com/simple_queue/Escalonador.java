package com.simple_queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Escalonador {

    private static Escalonador single_instance = new Escalonador();

    public double[] seeds;
    public double firstSeed;
    int indexSeed = 0;

    public ArrayList<Event> scheduledEvents;

    public int indexRound = 0;

    public Queue[] queues;

    private double lastEventTime = 0;
    private double time = 0;

    public static Escalonador getInstance() {
        return single_instance;
    }

    public void initialize(Queue[] queues) throws Exception {
        this.queues = queues;
        this.scheduledEvents = new ArrayList<Event>();
        this.agendamentoInicial(this.queues[0], firstSeed);
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

    public void calculateTime() {
        for (int i = 0; i < this.queues.length; i++) {
            Queue queue = this.queues[i];
            int currentSize = queue.currentSize;
            double total;
            if (queue.probabilities.get(currentSize) == null) {
                total = 0;
            } else
                total = queue.probabilities.get(currentSize).doubleValue();
            total = total + (this.time - this.lastEventTime);
            queue.probabilities.put(currentSize, Double.valueOf(total));
        }
        this.lastEventTime = this.time;
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

    public Event agendamentoInicial(Queue queue, double nextSeed) {
        EventType type = EventType.ARRIVAL;
        Event event = new Event(type, nextSeed, queue, null);
        this.scheduledEvents.add(event);
        Collections.sort(this.scheduledEvents);
        return event;
    }

    public double nextSeed() throws EndOfSeedsException {
        return seeds[indexSeed++];
    }

    public Event extractLastEvent() {
        return this.scheduledEvents.remove(0);
    }

    public double getTotalTime(Double[] times) {
        double total = 0;
        for (int i = 0; i < times.length; i++) {
            total += times[i];
        }
        return total;
    }

    public double[][] getProbabilities() {
        double[][] probabilities = new double[this.queues.length][];
        for (int i = 0; i < this.queues.length; i++) {
            Queue queue = this.queues[i];
            double total = this.time;
            probabilities[i] = new double[queue.probabilities.size()];
            for (int j = 0; j < queue.probabilities.size(); j++) {
                probabilities[i][j] = queue.probabilities.get(j) / total;
            }
        }

        return probabilities;
    }
}
