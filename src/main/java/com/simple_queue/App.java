package com.simple_queue;

import com.simple_queue.NumberGenerator.NumberGenerator;

public class App {

    public static void main(String[] args) {

        Queue[] queues = new Config().getQueues();
        new Config().initializeGenerator();
        Escalonador escalonador = Escalonador.getInstance();

        try {
            escalonador.initialize(queues);
            while (NumberGenerator.getInstance().hasSeed()) {
                escalonador.round();
            }
        } catch (Exception e) {
            System.out.println("End of seeds");
        }
        String fileName = "result.txt";
        System.out.println(fileName);
        new Output().save(fileName, (new Output()).format());
    }
}