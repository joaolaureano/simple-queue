package com.simple_queue;
import java.util.LinkedList;
import java.util.Queue;

import com.simple_queue.Event.AbstractEvent;
import com.simple_queue.Event.ArrivalEvent;

public class SimpleQueue {
    private static SimpleQueue simpleQueue;
    private Queue<AbstractEvent> queue;
    private int maxSize;
    private int currentSize;
    private int[] arrivalInterval;
    private int[] dropoutInterval;
    private int lossNumber;

    SimpleQueue(int maxSize, int[] arrivalInterval, int[] dropoutInterval) {

        this.maxSize = maxSize;
        this.arrivalInterval = arrivalInterval;
        this.dropoutInterval = dropoutInterval;
        this.queue = new LinkedList<AbstractEvent>();
        this.currentSize = 0;
        this.lossNumber = 0;
    }

    public boolean isFull() {
        return this.maxSize == this.currentSize;
    }

    public boolean addEvent(AbstractEvent event) {
        if ((this.currentSize == maxSize) && (event instanceof ArrivalEvent)) {
            this.lossNumber++;
            return false;
        }
        return this.queue.add(event);
    }

    public static SimpleQueue getInstance() {

        Config config = Config.getInstance();
        int maxSize = config.getQueueSize();
        int[] arrivalInternal = config.getArrivalInterval();
        int[] dropoutInterval = config.getDropoutInterval();

        if (simpleQueue == null)
            simpleQueue = new SimpleQueue(maxSize, arrivalInternal, dropoutInterval);
        return simpleQueue;
    }

    public int getLossNumber() {
        return this.lossNumber;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int[] getArrivalInterval() {
        return arrivalInterval;
    }

    public void setArrivalInterval(int[] arrivalInterval) {
        this.arrivalInterval = arrivalInterval;
    }

    public int[] getDropoutInterval() {
        return dropoutInterval;
    }

    public void setDropoutInterval(int[] dropoutInterval) {
        this.dropoutInterval = dropoutInterval;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public void increaseQueue() {
        this.currentSize++;
    }

    public void decreaseQueue() {
        this.currentSize--;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nSimpleQueue information\n").append("Final Size = " + currentSize)
                .append("\nMaximum Size = " + maxSize ).append("\nInterval for arrival = " + arrivalInterval[0] + "," + arrivalInterval[1])
                .append("\nInterval for dropout = " + dropoutInterval[0] + "," + dropoutInterval[1]).append("\nLoss number = " + lossNumber);

        return stringBuilder.toString();
    }

}