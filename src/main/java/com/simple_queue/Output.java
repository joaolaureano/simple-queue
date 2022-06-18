package com.simple_queue;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

public class Output {

    public String format() {
        StringBuilder sBuilder = new StringBuilder();
        NumberFormat nFormat = new DecimalFormat("#0.0000");
        sBuilder.append("Clock information\n");

        double[][] probabilities = Escalonador.getInstance().getProbabilities();
        // iterate over each probabilities for every queue array and iterate oveDr queue
        // array
        for (int i = 0; i < probabilities.length; i++) {
            sBuilder.append("QUEUE Nº " + i + "\n");
            sBuilder.append("STATE\t TIME\tPROBABILITY\n");
            for (int j = 0; j < probabilities[i].length; j++) {
                double timeAtState = Escalonador.getInstance().queues[i].probabilities.get(j);
                double probAtState = probabilities[i][j] * 100;
                String messageProb = j + "\t" + nFormat.format(timeAtState) + "\t" + nFormat.format(probAtState)
                        + "%\n";
                sBuilder.append(messageProb);
            }
            String messageLoss = "\nNumber of losses: " + Escalonador.getInstance().queues[i].lossNumber + "\n\n\n";
            sBuilder.append(messageLoss);
        }
        return sBuilder.toString();
    }

    public void save(String fileName, String content) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
