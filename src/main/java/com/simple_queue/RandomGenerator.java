package com.simple_queue;


public class RandomGenerator {
    static int a = 25013; // multiplicador
    static int m = 13001; // modulo
    static int c = 358; // constante
    static long x0 = 100; // semente/x0
    // static long x0 = System.currentTimeMillis(); // semente/x0
    static double lastRandom = x0; // xi-1



    static double getNextRandom() {
        double nextRandom = ((a * lastRandom + c) % m) / m;
        lastRandom = nextRandom;
        return nextRandom;
    }
}
