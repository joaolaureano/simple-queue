package com.simple_queue.NumberGenerator;

public class Seeds implements IGenerator {
    private double[] seeds;
    private int indexSeed = 0;

    public Seeds(double[] seeds) {
        this.seeds = seeds;
    }

    public double nextSeed() throws EndOfSeedsException {
        if (indexSeed >= seeds.length) {
            throw new EndOfSeedsException("End of seeds");
        }
        return seeds[indexSeed++];
    }

    public boolean hasSeed() {
        return indexSeed < seeds.length;
    }
}