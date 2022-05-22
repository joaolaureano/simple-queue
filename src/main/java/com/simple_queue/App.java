package com.simple_queue;

import java.util.ArrayList;
import java.util.stream.*;

public class App {
    Config config = new Config();
    public int currentSize = 0;
    public int maxSize = config.getQueueSize();
    public int[] arrivalInterval = config.getArrivalInterval();
    public int[] departureInterval = config.getDepartureInterval();
    public double firstSeed = config.getFirstSeed();
    public double[] seeds = config.getSeeds();
    public String mode = config.getMode();
    public int numberOfRounds = config.getRoundNumber();
    public int serverNumber = config.getServerNumber();

    public int indexRound = 0;
    public int lossNumber = 0;
    public ArrayList<Event> scheduledEvents;
    public double[] clock = new double[maxSize + 1];
    public int indexSeed = 0;

    public static void main(String[] args) {
        App a = new App();
        a.initialize();

        int indexBegin, indexEnd;
        indexBegin = 0;
        if (a.mode.equals("RANDOM")) {
            indexEnd = a.numberOfRounds;
        } else if (a.mode.equals("SEED")) {
            indexEnd = a.seeds.length - 1;
        } else if (a.mode.equals("PRINT_SEEDS")) {
            RandomGenerator.printRandom(a.numberOfRounds);
            return;
        } else {
            return;
        }

        while (indexBegin < indexEnd) {
            a.nextRound();
            indexBegin++;
        }
        String fileName = "result-"+ a.mode +"-"+ a.serverNumber + ".txt";
        System.out.println(fileName);
        new Output().save(fileName, (new Output()).format(a));

    }

    public void initialize() {
        scheduledEvents = new ArrayList<Event>();

        schedule(EventType.FIRST_ARRIVAL);

    }

    public double calculate(int[] interval, double seed) {
        return (interval[1] - interval[0]) * seed + interval[0];
    }

    public void arrivalProcedure() {
        calculateTime(currentSize);
        if ((currentSize < maxSize) || (currentSize < 0)) {
            currentSize++;
            if (currentSize <= serverNumber) {
                schedule(EventType.DEPARTURE);
            }
        } else {
            this.lossNumber++;
        }
        schedule(EventType.ARRIVAL);
    }

    public void departureProcedure() {
        calculateTime(currentSize);
        currentSize--;
        if (currentSize >= serverNumber) {
            schedule(EventType.DEPARTURE);
        }
    }

    public void schedule(EventType eType) {

        try {
            double time;
            double nextSeed = 0;
            Event newEvent;
            if (eType == EventType.ARRIVAL) {
                nextSeed = nextSeed();
                time = lastTime() + calculate(arrivalInterval, nextSeed);
            } else if (eType == EventType.DEPARTURE) {
                nextSeed = nextSeed();
                time = lastTime() + calculate(departureInterval, nextSeed);
            } else {
                time = firstSeed;
            }
            newEvent = new Event(eType, time);
            this.scheduledEvents.add(newEvent);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void calculateTime(int index) {

        double nextTime = lastTime();
        double lastTime = previousTime();

        double delta = nextTime - lastTime;
        clock[index] += delta;
    }

    public double nextSeed() throws Exception {
        if (mode.equals("SEED"))
            return seeds[indexSeed++];
        else if (mode.equals("RANDOM"))
            return RandomGenerator.getNextRandom();
        else
            throw new Exception("MODE IS NOT AVAILABLE");
    }

    public void nextRound() {
        int maxIndex = -1;
        for (int i = 0; i < scheduledEvents.size(); i++) {
            if (scheduledEvents.get(i).order != -1)
                continue;
            if (maxIndex == -1)
                maxIndex = i;
            if (scheduledEvents.get(maxIndex).time > scheduledEvents.get(i).time)
                maxIndex = i;
        }

        scheduledEvents.get(maxIndex).order = indexRound++;
        switch (scheduledEvents.get(maxIndex).type) {
            case FIRST_ARRIVAL:
            case ARRIVAL:
                arrivalProcedure();
                break;
            case DEPARTURE:
                departureProcedure();
                break;
        }
    }

    public double lastTime() {
        for (int i = 0; i < scheduledEvents.size(); i++)
            if (scheduledEvents.get(i).order == indexRound - 1)
                return this.scheduledEvents.get(i).time;

        return 0;
    }

    public double previousTime() {
        for (int i = 0; i < scheduledEvents.size(); i++)
            if (scheduledEvents.get(i).order == indexRound - 2)
                return this.scheduledEvents.get(i).time;

        return 0;
    }

    public double getTimeTotal() {
        return DoubleStream.of(clock).sum();
    }

    public double[] getProbabilities() {
        double[] prob = new double[clock.length];
        double total = getTimeTotal();
        for (int i = 0; i < prob.length; i++)
            prob[i] = (clock[i] / total) * 100;

        return prob;
    }
}
