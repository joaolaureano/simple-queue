package com.simple_queue;

public class App {
    public static void main(String[] args) {
        int indexBegin, indexEnd;
        indexBegin = 0;

        Escalonador escalonador = Escalonador.getInstance();

        escalonador.seeds = new Config().getSeeds();
        escalonador.firstSeed = new Config().getFirstSeed();
        indexEnd = escalonador.seeds.length - 1;

        Queue[] queues = new Config().getQueues();

        try {
            escalonador.initialize(queues);
            while (indexBegin < indexEnd) {
                escalonador.round();
                indexBegin++;
            }
        } catch (EndOfSeedsException ese) {
            System.out.println("End of seeds");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fileName = "result_.csv";
        new Output().save(fileName, (new Output()).format());
    }

}