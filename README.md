# Simple Queue — V1 (original version)

**[Leia em português / Read this in Portuguese](README.pt-BR.md)**

A **discrete-event simulator** for networks of **M/M/c queues** (several
servers per queue, probabilistic routing between queues). At the end of a
run it reports, for each queue, how long it spent in each state (number of
customers in the system), the probability of each state and the number of
losses. Built as coursework for a college class on simulation / queueing
theory.

> **This is the archived original version**, exactly as it was submitted.
> The only change on top of it is this README, which replaces the original
> all-caps notes with the same information. The fixed version lives on the
> [`master`](../../tree/master) branch.

## Configuration

The parameters are read from `src/main/resources/model.xml`:

- `queues` / `queue`: the queues in the simulation, each with:
  - `arrivalInterval`: interval for arrival events — two comma-separated values.
  - `departureInterval`: interval for departure events — two comma-separated values.
  - `sizeQueue`: the queue's capacity.
  - `serverNumber`: the number of servers.
- `network` / `connection`: the connections between queues, as origin and
  destination. `0,1` means queue 1 receives the events after they are
  processed by queue 0.
- `seed`: the seeds to use, when required.
- `roundNumber`: the number of rounds to run when using the random number
  generator.
- `mode`: what the program does (see below).
- `arrivals` / `arrival`: when the first event happens in each queue.
  `<arrival>0,2.5</arrival>` means queue 0 starts with an event at time 2.5.

Queues get an auto-increment id in the order they appear (0, 1, 2, ...), and
those ids are the ones used in `network` and `arrivals`: with three queues,
`0,1` is valid and `1,3` is not.

## Modes

- `SEED`: runs with the seeds listed in `seed`.
- `RANDOM`: runs with the built-in random number generator, for
  `roundNumber` rounds.
- `PRINT_RANDOM`: only generates `roundNumber` seeds with the generator.

## Running

Requires Maven. `App.java` is the main class.

```sh
mvn exec:java -Dexec.mainClass="com.simple_queue.App"
```

`Commands/run` runs the same command. The result goes to `result.txt`, with
three columns per queue: **STATE** (number of customers), **TIME** (how long
the queue held that many customers) and **PROBABILITY** (the probability of
that state).

The tested models are in `src/main/resources/simulador`, with the same name
as the XML model and a different extension.

## Known issues in this version

These were found later and are fixed on `master`:

1. The random number generator feeds back its state already divided by `m`,
   which breaks the LCG.
2. In `RANDOM` mode, `roundNumber` counts consumed random numbers, not
   rounds.
3. `Event` and `Queue` assign their index counter twice, so ids skip.
4. `model.xml` is loaded from a path relative to the working directory.
5. `Queue.chegada()` has an unreachable `maxSize < 0` check.
