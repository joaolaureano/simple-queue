package com.simple_queue;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Output {

    public String format() {
        StringBuilder sBuilder = new StringBuilder();
        NumberFormat nFormat = new DecimalFormat("#0.000");
        sBuilder.append("Clock information\n");
        double[][] probabilities = Escalonador.getInstance().getProbabilities();
        // iterate over each probabilities for every queue array and iterate over queue
        // array
        for (int i = 0; i < probabilities.length; i++) {
            sBuilder.append("QUEUE Nº " + i + "\n");
            sBuilder.append("STATE\t TIME\tPROBABILITY\n");
            for (int j = 0; j < probabilities[i].length; j++) {
                sBuilder.append(i + "\t" + j + "\t" + nFormat.format(probabilities[i][j]) + "\n");
            }
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
