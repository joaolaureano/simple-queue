package com.simple_queue;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Timer {
    private static Timer instance;

    public double[] clock;
    public double currentTime;
    public int index;

    Timer() {
        clock = new double[Config.getInstance().getQueueSize() + 1];
        index = 0;
        currentTime = 0;
    }

    public static Timer getInstance() {
        if (instance == null)
            instance = new Timer();
        return instance;
    }
    

    public void accountTimer(double lastTime, int nextIndex) {
        double deltaTime = lastTime - currentTime;
        clock[index] += deltaTime;
        index += nextIndex;
        currentTime = lastTime;
    }

    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        NumberFormat nFormat = new DecimalFormat("#0.00");

        sBuilder.append("Timer final result\n");
        sBuilder.append("STATE\t").append("TIME\t").append("PROBABILITY\n");
        for (int i = 0; i < clock.length; i++) {
            double prob = clock[i] / currentTime;
            sBuilder.append(i + "\t" + nFormat.format(clock[i]) + "\t" + nFormat.format(prob * 100) +"%\n");
        }

        sBuilder.append("TOTAL TIME - " + currentTime + "\n");

        return sBuilder.toString();
    }
}
