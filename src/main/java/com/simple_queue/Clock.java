package com.simple_queue;

public class Clock {

    private static Clock single_instance = new Clock();

    public static Clock getInstance() {
        return single_instance;
    }

    public void calculateTime() {
        Escalonador escalonador = Escalonador.getInstance();
        for (int i = 0; i < escalonador.queues.length; i++) {
            Queue queue = escalonador.queues[i];
            int currentSize = queue.currentSize;
            double total;
            if (queue.probabilities.get(currentSize) == null) {
                total = 0;
            } else
                total = queue.probabilities.get(currentSize).doubleValue();
            total = total + (escalonador.getTime() - escalonador.getLastEventTime());
            queue.probabilities.put(currentSize, Double.valueOf(total));
        }
        escalonador.setLastEventTime(escalonador.getTime());
    }

    public double[][] getProbabilities() {
        Escalonador escalonador = Escalonador.getInstance();

        double[][] probabilities = new double[escalonador.queues.length][];
        for (int i = 0; i < escalonador.queues.length; i++) {
            Queue queue = escalonador.queues[i];
            double total = escalonador.getTime();
            probabilities[i] = new double[queue.probabilities.size()];
            for (int j = 0; j < queue.probabilities.size(); j++) {
                probabilities[i][j] = queue.probabilities.get(j) / total;
            }
        }

        return probabilities;
    }
}
