package com.simple_queue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class SimpleQueue {
    private static SimpleQueue simpleQueue;
    private Queue<Event> queue;
    private int maxSize;
    private int currentSize;
    private int[] arrivalInterval;
    private int[] dropoutInterval;
    private int lossNumber;

    SimpleQueue(int maxSize, int[] arrivalInterval, int[] dropoutInterval) {

        this.maxSize = maxSize;
        this.arrivalInterval = arrivalInterval;
        this.dropoutInterval = dropoutInterval;
        this.queue = new LinkedList<Event>();
        this.currentSize = 0;
        this.lossNumber = 0;
    }

    public boolean isFull() {
        return this.maxSize == this.currentSize;
    }

    public boolean addEvent(Event event) {
        if ((this.currentSize == maxSize) && (event.getEventType().eventName.equals(EventEnum.ARRIVAL.eventName))) {
            this.lossNumber++;
            return false;
        }
        if ((event.getEventType().eventName.equals(EventEnum.ARRIVAL.eventName))) {
            this.currentSize++;
        }
        if ((event.getEventType().eventName.equals(EventEnum.DROPOUT.eventName))) {
            this.currentSize--;
        }
        return this.queue.add(event);
    }

    public static SimpleQueue getInstance() {
        // Mock :)

        int maxSize = 4;
        int[] arrivalInternal = { 1, 2 };
        int[] dropoutInterval = { 2, 3 };

        // Mock :)

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
        return "SimpleQueue [arrivalInterval=" + Arrays.toString(arrivalInterval) + ", currentSize=" + currentSize
                + ", dropoutInterval=" + Arrays.toString(dropoutInterval) + ", lossNumber=" + lossNumber + ", maxSize="
                + maxSize + ", queue=" + queue + "]";
    }

    

}