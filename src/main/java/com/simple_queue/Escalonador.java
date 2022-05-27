package com.simple_queue;

import java.util.ArrayList;
import java.util.stream.DoubleStream;

public class Escalonador {

    private static Escalonador single_instance = new Escalonador();
    public ArrayList<Event>[] scheduledEvents;
    public int indexRound = 0;
    public double[][] clock;
    int indexSeed = 0;
    public double[] seeds;
    public double firstSeed;
    private Queue[] queues;

    public static Escalonador getInstance() {
        return single_instance;
    }

    public void calculateTime(int queueIndex, int queueSize) {

        double nextTime = nextTime();
        double lastTime = lastTime();

        double delta = nextTime - lastTime;
        clock[queueIndex][queueSize] += delta;
    }

    public double nextTime() {
        for (int queueIndex = 0; queueIndex < queues.length; queueIndex++)
            for (int i = 0; i < scheduledEvents[queueIndex].size(); i++)
                if (scheduledEvents[queueIndex].get(i).order == indexRound - 1)
                    return this.scheduledEvents[queueIndex].get(i).time;
        return 0;
    }

    public double lastTime() {
        for (int queueIndex = 0; queueIndex < queues.length; queueIndex++)
            for (int i = 0; i < scheduledEvents[queueIndex].size(); i++)
                if (scheduledEvents[queueIndex].get(i).order == indexRound - 2)
                    return this.scheduledEvents[queueIndex].get(i).time;
        return 0;
    }

    public double calculate(int[] interval, double seed) {
        return (interval[1] - interval[0]) * seed + interval[0];
    }

    public void agendaChegada(Queue queue) throws Exception {
        agendaChegada(queue, nextSeed());
    }

    public void agendaSaida(Queue queue) throws Exception {
        agendaSaida(queue, nextSeed());
    }

    public Event agendaChegada(Queue queue, double nextSeed) {

        double nextTime = nextTime() + calculate(queue.arrivalInterval, nextSeed);
        EventType type = EventType.ARRIVAL;
        Event event = new Event(type, nextTime, queue, queue.destiny);
        this.scheduledEvents[queue.index].add(event);
        return event;
    }

    public Event agendaSaida(Queue queue, double nextSeed) {
        double nextTime = nextTime() + calculate(queue.departureInterval, nextSeed);
        EventType type = EventType.DEPARTURE;
        Event event = new Event(type, nextTime, queue, queue.destiny);
        this.scheduledEvents[queue.index].add(event);
        return event;
    }

    public void agendaChegadaInicial(Queue queue) throws Exception {
        agendaChegadaInicial(queue, 0);
    }

    public Event agendaChegadaInicial(Queue queue, double nextSeed) {

        double nextTime = firstSeed;
        EventType type = EventType.FIRST_ARRIVAL;
        Event event = new Event(type, nextTime, queue, queue.destiny);
        this.scheduledEvents[queue.index].add(event);
        return event;
    }

    public void round() throws Exception {
        int queue = 0;
        int[] indexes = { 0, -1 };
        for (; queue < this.queues.length; queue++) {
            for (int i = 0; i < this.scheduledEvents[queue].size(); i++) {
                if (scheduledEvents[queue].get(i).order != -1)
                    continue;
                if (indexes[1] == -1) {
                    indexes[0] = queue;
                    indexes[1] = i;

                }
                if (scheduledEvents[indexes[0]].get(indexes[1]).time > scheduledEvents[queue].get(i).time) {
                    indexes[0] = queue;
                    indexes[1] = i;
                }
            }
        }
        scheduledEvents[indexes[0]].get(indexes[1]).order = indexRound++;

        Event selected = scheduledEvents[indexes[0]].get(indexes[1]);

        switch (selected.type) {
            case FIRST_ARRIVAL:
            case ARRIVAL:
                selected.origin.chegada(true);
                break;
            case DEPARTURE:
                selected.origin.saida();
                if (selected.destiny != null)
                    selected.destiny.chegada(false);
                break;
        }
    }

    public double getTimeTotal(int queueIndex) {
        return DoubleStream.of(clock[queueIndex]).sum();
    }

    public double[][] getProbabilities() {
        double[][] prob = new double[clock.length][];
        for (int i = 0; i < clock.length; i++)
            prob[i] = new double[clock[i].length];
        for (int queue = 0; queue < clock.length; queue++) {
            double total = getTimeTotal(queue);
            for (int i = 0; i < prob[queue].length; i++)
                prob[queue][i] = (clock[queue][i] / total) * 100;
        }

        return prob;
    }

    public double nextSeed() throws EndOfSeedsException {
        return seeds[indexSeed++];
    }

    public void initialize(Queue[] queues) throws Exception {
        this.queues = queues;
        this.scheduledEvents = new ArrayList[queues.length];
        this.clock = new double[queues.length][];
        for (int i = 0; i < queues.length; i++) {
            this.scheduledEvents[i] = new ArrayList<Event>();
            this.clock[i] = new double[queues[i].maxSize + 1];

        }
        this.agendaChegadaInicial(this.queues[0]);
    }
}
