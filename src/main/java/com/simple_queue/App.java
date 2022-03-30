package com.simple_queue;

/**
 * Hello world!
 *
 */
public class App {
    // below code is good :)
    // mvn exec:java -Dexec.mainClass=com.simple_queue.App -Dexec.args="RANDOM"
    public static void main(String[] args) {
        // if (args[0].equals("RANDOM"))
            new Interface().init(true);
        // else if (args[0].equals("SEED"))
            // new Interface().init(false);
        // else {
        //     System.out.println("YOUR ARGUMENT SHOULD BE 'RANDOM' OR 'SEED' ");
        // }
    }
}
