package com.simple_queue;

import com.simple_queue.NumberGenerator.NumberGenerator;

public class App {

    public static void main(String[] args) {

        Config config = new Config();
        Queue[] queues = config.getQueues();
        config.initializeGenerator();
        Escalonador escalonador = Escalonador.getInstance();

        try {
            escalonador.initialize(queues);
            if (config.getMode().equals("RANDOM")) {
                int roundNumber = config.getRoundNumber();
                while (escalonador.indexRound < roundNumber) {
                    escalonador.round();
                }
            } else {
                while (NumberGenerator.getInstance().hasSeed()) {
                    escalonador.round();
                }
            }
        } catch (Exception e) {
            System.out.println("End of seeds");
        }
        String fileName = "result.txt";
        new Output().save(fileName, (new Output()).format());
    }
}