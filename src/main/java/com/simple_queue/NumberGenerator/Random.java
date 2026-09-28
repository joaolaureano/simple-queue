package com.simple_queue.NumberGenerator;

public class Random implements IGenerator {

    public double nextSeed() throws EndOfSeedsException {
        return RandomGenerator.getNextRandom();
    }

    public boolean hasSeed() {
        return true;
    }
}