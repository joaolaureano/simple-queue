package com.simple_queue.NumberGenerator;

public class Random implements IGenerator {
    int maxRound = 0;

    public Random(int maxRound) {
        this.maxRound = maxRound;
    }

    public double nextSeed() throws EndOfSeedsException {
        maxRound--;
        return RandomGenerator.getNextRandom();
    }

    public boolean hasSeed() {
        return maxRound > 0;
    }
}