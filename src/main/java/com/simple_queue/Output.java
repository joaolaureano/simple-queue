package com.simple_queue;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class Output {
    

    public String format(App app) {
        StringBuilder sBuilder = new StringBuilder();
        NumberFormat nFormat = new DecimalFormat("#0.000");
        sBuilder.append("Clock information\n");
        sBuilder.append("STATE\t TIME\tPROBABILITY\n");
        double[] probabilities = app.getProbabilities();
        for (int i = 0; i < app.clock.length; i++)
            sBuilder.append(i + "\t" + nFormat.format(app.clock[i]) + "\t" + nFormat.format(probabilities[i]) + "\n");
        sBuilder.append("TOTAL TIME - " + nFormat.format(app.getTimeTotal()));

        sBuilder.append("\n\n").append("Queue information\n").append("Max size - " + app.maxSize)
                .append("\nArrival Interval - " + app.arrivalInterval[0] + "," + app.arrivalInterval[1])
                .append("\nDeparture Interval - " + app.departureInterval[0] + "," + app.departureInterval[1])
                .append("\nLoss Number - " + app.lossNumber).append("\nMode - " + app.mode);

        sBuilder.append("\n\n").append("Events information").append("EVENT\tTIME\tORDER\n");
        for (int i = 0; i < app.scheduledEvents.size(); i++) {
            Event e = app.scheduledEvents.get(i);
            sBuilder.append(e.type + "\t" + e.time + "\t" + e.order).append("\n");
        }

        return sBuilder.toString();
    }
    
    public void save(String fileName, String content) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
