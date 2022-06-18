package com.simple_queue.NumberGenerator;

public class NumberGenerator {
    private IGenerator generator;
    static NumberGenerator instance;

    public static NumberGenerator getInstance() {
        return instance;
    }

    public static NumberGenerator instantiateSeedsGenerator(double[] seeds) {
        if (instance != null) {
            return instance;
        }
        instance = new NumberGenerator();
        instance.generator = new Seeds(seeds);
        return instance;
    }

    public static NumberGenerator instantiateRandomGenerator(int maxRound) {
        if (instance != null) {
            return instance;
        }
        instance = new NumberGenerator();
        instance.generator = new Random(maxRound);
        return instance;
    }

    public double getNextSeed() {
        return generator.nextSeed();
    }

    public boolean hasSeed() {
        return generator.hasSeed();
    }
}
