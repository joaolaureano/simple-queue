package com.simple_queue;

import java.util.ArrayList;
import java.util.stream.DoubleStream;

public class Escalonador {

    private static Escalonador single_instance = new Escalonador();
    public ArrayList<Event>[] scheduledEvents;
    public int indexRound = 0;
    int indexSeed = 0;
    public double[] seeds;
    public double firstSeed;
    public Queue[] queues;

    public double lastEvt = 0.0D;
    public double currEvtTime = 0.0D;

    public static void destroy() {
        single_instance = null;
    }

    public static Escalonador getInstance() {
        if (single_instance == null)
            single_instance = new Escalonador();
        return single_instance;
    }

    public void calculateTime() {
        for (Queue queue : this.queues) {
            int currentSize = queue.currentSize;
            double subtotal = 0;
            if (queue.times.containsKey(currentSize))
                subtotal = queue.times.get(currentSize);
            subtotal += this.currEvtTime - this.lastEvt;
            queue.times.put(currentSize, subtotal);
        }
        this.lastEvt = this.currEvtTime;
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

        double nextTime = this.currEvtTime + calculate(queue.arrivalInterval, nextSeed);
        EventType type = EventType.ARRIVAL;
        Event event = new Event(type, nextTime, queue, queue.destiny);
        this.scheduledEvents[queue.index].add(event);
        return event;
    }

    public Event agendaSaida(Queue queue, double nextSeed) {
        double nextTime = this.currEvtTime + calculate(queue.departureInterval, nextSeed);
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
        this.lastEvt = this.currEvtTime;
        this.currEvtTime = selected.time;

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

    public double[][] getProbabilities() {
        double[][] prob = new double[this.queues.length][];

        for (int queueIdx = 0; queueIdx < this.queues.length; queueIdx++) {
            prob[queueIdx] = new double[this.queues[queueIdx].times.size()];
            double total = this.currEvtTime;
            for (int i = 0; i < prob[queueIdx].length; i++) {
                double subtotal = this.queues[queueIdx].times.get(i).doubleValue();

                prob[queueIdx][i] = (subtotal) / total * 100;
            }
        }

        return prob;
    }

    public double nextSeed() throws EndOfSeedsException {
        return seeds[indexSeed++];
    }

    public void initialize(Queue[] queues) throws Exception {
        this.queues = queues;
        this.scheduledEvents = new ArrayList[queues.length];
        for (int i = 0; i < queues.length; i++) {
            this.scheduledEvents[i] = new ArrayList<Event>();

        }
        this.agendaChegadaInicial(this.queues[0]);
    }
}
