THIS IS A SIMULATION OF TANDEM QUEUE.

THE PARAMETERS NEED TO BE SET IN main/java/resources/model.xml.
THE PARAMETERS ARE
    queues -> The list of all queues presented in simulation
    queue -> All parameters for a queue. Parameters are below, idented
        arrivalInterval -> THE INTERVAL FOR ARRIVAL EVENTS. THE VALUES ARE SEPARATED BY A COMMA AND MUST BE TWO VALUES ONLY
        departureInterval -> THE INTERVAL FOR DROPOUT EVENTS. THE VALUES ARE SEPARATED BY A COMMA AND MUST BE TWO VALUES ONLY
        sizeQueue -> THE QUEUE SIZE
        serverNumber -> NUMBERS OF SERVER
    network -> The list of all queues connections
        connection -> Array that contains two number, the origin and the destiny of a given connection. 
            For example, if connection is [0,1], it means that the queue 1 will be the destiny of events after being processed by 0
    firstSeed -> THE FIRST EVENT TIME
    seed -> THE SEEDS TO BE USED, IF REQUIRED
    roundNumber -> NUMBERS OF ROUNDS TO BE RUNNED BY THE SCRIPT, WHEN USING THE RANDOM NUMBER GENERATOR
    mode -> WHAT THE SCRIPT WILL DO

ALL QUEUE WILL BE CREATED WITH AN AUTO-INCREMENT ID. YOU DO NOT NEED TO SET THIS.
IF YOU CREATE 3 QUEUES IN THE model.xml, THEY ILL HAVE THE ID 0,1,2 FOR EXAMPLE.
NOTE THAT YOU MUST USE THIS ID's TO CREATE THE CONNECTION AND NETWORK. USING PREVIOUS EXAMPLE, [0,1] WOULD BE A VALID EXAMPLE. [1,3] WOULD BE AN INVALID EXAMPLE.

THIS USES MAVEN, SO MUST BE INSTALLED

WHEN RUNNING THE CODE, THERE ARE THREE MODES
IT MUST BE SPECIFIED AN ARGUMENT FOR THE MODE
THE MODES AVAILABLE ARE "SEED" AND "RANDOM" AND "PRINT_RANDOM"
    FOR "SEED", THE SCRIPT WILL RUN USING THE SEED DEFINED. THE seed VALUE IN XML MUST BE SPECIFIED
    FOR "RANDOM", THE SCRIPT WILL RUN USING THE RANDOMGENERATOR. THE roundNumber VALUE IN XML MUST BE SPECIFIED .
    FOR "PRINT_RANDOM", IT WILL GENERATE THE SEEDS ACCORDING TO THE RANDOMGENERATOR SPECIFICATION. THE AMOUNT OF SEEDS DEPENDS ON roundNumber

IF REQUIRED, "App.java" IS THE MAIN CLASS
TO RUN THE CODE, YOU MUST USE THIS COMMAND
    mvn exec:java -Dexec.mainClass="com.simple_queue.App"
YOU CAN USE "run" FILE TO RUN 

THE RESULT OF SCENARIO WILL BE STORED IN "result.csv"

OUTPUT HAS FOLLOWING STRUCTURE
    CLOCK INFORMATION
        IT HAS 3 COLUMNS
        STATE, WHICH MEANS NUMBER OF PEOPLE
        TIME, WHICH MEANS HOW MUCH TIME THE QUEUE STORED THIS AMOUNT OF PEOPLE
        PROBABILITY, WHICH MEANS THE PROBABILITY OF THIS STATE HAPPEN
        IT ALSO HAS A FINAL ROW, WHICH IS THE TOTAL TIME


ALL RESULTS ARE STORED IN ./Resultados