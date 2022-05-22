package com.simple_queue;

import java.io.FileWriter;
import java.io.IOException;

public class RandomGenerator {
    static int a = 47057; // multiplicador
    static int m = 12613; // modulo
    static int c = 358; // constante
    static long x0 = 46819; // semente/x0
    // static long x0 = System.currentTimeMillis(); // semente/x0
    static double lastRandom = x0; // xi-1

    static double getNextRandom() {
        double nextRandom = ((a * lastRandom + c) % m) / m;
        lastRandom = nextRandom;
        return nextRandom;
    }

    static void printRandom(int number) {
        String allNumbers = "rndnumbers:\n";
        while (number > 0) {
            allNumbers += "- " + getNextRandom();
            number--;
            allNumbers += "\n";
        }

        try {
            FileWriter fw = new FileWriter("seeds.txt");
            fw.write(allNumbers);
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
