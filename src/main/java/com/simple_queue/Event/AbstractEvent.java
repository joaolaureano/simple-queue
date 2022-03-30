package com.simple_queue.Event;


import com.simple_queue.Scheduler;
import com.simple_queue.SimpleQueue;

public abstract class AbstractEvent {
    private boolean finished;
    private double time;

    public AbstractEvent(double time) {
        this.finished = false;
        this.time = time;
    }

    public abstract void execute(SimpleQueue queue);

    public static double calculateTime(int[] interval, double seed) {
        double result = (interval[1] - interval[0]) * seed + interval[0];
        double latestTimeScheduled = Scheduler.getInstance().getLast().time;
        result += latestTimeScheduled;
        return result;
    }
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}