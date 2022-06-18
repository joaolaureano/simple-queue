package com.simple_queue.NumberGenerator;

public interface IGenerator {
    public double nextSeed() throws EndOfSeedsException;

    public boolean hasSeed();
}
