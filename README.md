# Simple Queue

**[Leia em português / Read this in Portuguese](README.pt-BR.md)**

> Discrete-event simulator for queueing networks (M/M/c) — a college project.

This repository is part of a personal archive of projects developed during
university (college). The original submission is preserved under the
[`V1`](../../releases/tag/V1) tag; the default branch (`master`) fixes the
bugs found in it — see [Fixes applied](#fixes-applied) below.

## About this repository

This project was built during undergraduate studies, as coursework for a
class on simulation / queueing theory. It simulates a **network of M/M/c
queues** (multiple servers per queue, with probabilistic routing between
queues) using **discrete-event simulation**, and at the end of the run
computes, for each queue, the probability of being in each state (number of
customers in the system), plus the number of losses (customers rejected due
to a full queue).

This version fixes bugs found during a code review done well after the
original submission (see
[Fixes applied](#fixes-applied)). The
original, uncorrected submission was preserved under the
`V1` tag as a historical record.

## How it works

The simulator is configured through an XML file
(`src/main/resources/model.xml`) describing:

- `queues` / `queue`: the list of queues in the system. Each queue has:
  - `arrivalInterval`: (min, max) interval for the uniform inter-arrival time.
  - `departureInterval`: (min, max) interval for the uniform service time.
  - `serverNumber`: number of servers for that queue.
  - `sizeQueue` (optional): the queue's maximum capacity. If omitted, the
    queue is treated as unbounded.
- `network` / `connection`: connections between queues, formatted as
  `origin,destination,probability`. A customer finishing service at
  `origin` is routed to `destination` with the given probability (the sum
  of a queue's outgoing probabilities can be less than 1 — the remainder is
  the probability of the customer leaving the network).
- `arrivals` / `arrival`: the time of the first external arrival at each
  queue, formatted as `queueIdx,time`.
- `seed`: a list of pseudo-random numbers (0 to 1) used in `SEED` mode.
- `roundNumber`: number of rounds (events) to run in `RANDOM` mode.
- `mode`: the run mode — `SEED`, `RANDOM`, or `PRINT_RANDOM` (see below).

Each queue gets an automatic index (0, 1, 2, ...) in the order it appears in
the XML — that index is what you use in `network` and `arrivals`.

## Run modes

- **SEED**: the simulation runs using exactly the numbers listed in the
  XML's `seed` tag, in order, until they run out. Useful for reproducing a
  specific result (e.g. to check hand-computed results from class).
- **RANDOM**: the simulation runs using a custom linear congruential
  generator (`RandomGenerator`), for `roundNumber` rounds (arrival/departure
  events processed).
- **PRINT_RANDOM**: just generates `roundNumber` pseudo-random numbers and
  writes them to `seeds.txt`, without running the simulation (useful for
  inspecting the generator's output).

The mode is set via the `<mode>` tag in `model.xml`.

## How to run

Requires Maven.

```sh
mvn exec:java -Dexec.mainClass="com.simple_queue.App"
```

The `Commands/run` script is also provided for convenience.

The result is written to `result.txt`, at the project root, structured as
follows, per queue:

```
QUEUE Nº <index>
STATE   TIME    PROBABILITY
<n>     <accumulated time in state n>    <probability in %>
...
Number of losses: <number of rejected customers>
```

- **STATE**: number of customers in the system (queue + in service).
- **TIME**: total time (in the simulation's time scale) the queue spent in
  that state.
- **PROBABILITY**: `TIME` divided by the total simulated time.

The models used in the original tests live under
`src/main/resources/simulador/` (`.yml` format, for reference only — they
are not read by the code, which only reads `model.xml`).

## Fixes applied

Compared to the original version (tag `V1`), this version fixes:

1. **Incorrect random number generator**
   (`NumberGenerator/RandomGenerator.java`): the LCG's state used to be fed
   back already divided by `m`, instead of being kept as an integer between
   calls — which broke the generator's statistical properties. The state
   (`lastX`) is now kept as an integer, and only divided by `m` in the
   returned value, as a standard LCG does.
2. **`roundNumber` now counts actual rounds**: in `RANDOM` mode, the
   stopping condition used to be "number of random numbers consumed" (which
   varied per round), and is now the number of rounds actually processed
   (`Escalonador.indexRound`) — which is what `roundNumber` in `model.xml`
   and the README always claimed it did.
3. **Duplicate index counter**, in `Event` and `Queue`: removed the
   redundant assignment in the constructor (the field is now initialized
   exactly once, at declaration), so IDs are sequential again.
4. **Hardcoded config path**: `Config.java` now loads `model.xml` from the
   classpath (`getResourceAsStream`) instead of a path relative to the
   working directory — it now works regardless of where the `.jar`/process
   is launched from.
5. **Unreachable condition** in `Queue.chegada()`: removed the dead
   `maxSize < 0` check, which was never true (an "unbounded" queue uses
   `Integer.MAX_VALUE`, not a negative number).
