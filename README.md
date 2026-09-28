# Simple Queue

> Simulador de eventos discretos para redes de filas (M/M/c) — projeto de faculdade.
> Discrete-event simulator for queueing networks (M/M/c) — a college project.

This repository is part of a personal archive of projects developed during
university (college). The original submission is preserved under the
[`original`](../../releases/tag/original) tag; this version (tag `bugfix`,
and the default branch) fixes the bugs found in it — see the
[changelog](#correções-aplicadas--fixes-applied) below.

---

## 🇧🇷 Português

### Sobre este repositório

Este projeto foi desenvolvido durante a graduação, como parte de uma disciplina
sobre simulação / teoria das filas. Ele simula uma **rede de filas M/M/c**
(múltiplos servidores, com roteamento probabilístico entre filas) usando
**simulação de eventos discretos**, e calcula, ao final da execução, a
probabilidade de cada fila estar em cada estado (quantidade de clientes no
sistema), além do número de perdas (clientes rejeitados por falta de espaço
na fila).

Esta versão corrige bugs identificados em uma revisão de código feita bem
depois da entrega original (ver [changelog](#correções-aplicadas--fixes-applied)).
A submissão original, sem essas correções, foi preservada na tag `original`,
como registro histórico.

### Como funciona

O simulador é configurado por um arquivo XML (`src/main/resources/model.xml`)
que descreve:

- `queues` / `queue`: a lista de filas do sistema. Cada fila tem:
  - `arrivalInterval`: intervalo (mín, máx) do tempo entre chegadas (uniforme).
  - `departureInterval`: intervalo (mín, máx) do tempo de atendimento (uniforme).
  - `serverNumber`: número de servidores (atendentes) da fila.
  - `sizeQueue` (opcional): capacidade máxima da fila. Se omitido, a fila é
    considerada infinita.
- `network` / `connection`: as conexões entre filas, no formato
  `origem,destino,probabilidade`. Um cliente que termina o atendimento na
  fila `origem` é roteado para a fila `destino` com a probabilidade indicada
  (a soma das probabilidades de saída de uma fila pode ser menor que 1 — a
  diferença é a probabilidade do cliente sair da rede).
- `arrivals` / `arrival`: o instante da primeira chegada externa em cada
  fila, no formato `idxFila,tempo`.
- `seed`: lista de números pseudoaleatórios (0 a 1) a serem usados no modo
  `SEED`.
- `roundNumber`: quantidade de rounds (eventos) a executar no modo `RANDOM`.
- `mode`: o modo de execução — `SEED`, `RANDOM` ou `PRINT_RANDOM` (ver abaixo).

Cada fila recebe um índice automático (0, 1, 2, ...) na ordem em que aparece
no XML — é esse índice que deve ser usado em `network` e `arrivals`.

### Modos de execução

- **SEED**: a simulação roda usando exatamente os números da lista `seed` do
  XML, na ordem, até esgotá-los. Útil para reproduzir um resultado específico
  (ex.: para verificar contas feitas manualmente em sala de aula).
- **RANDOM**: a simulação roda usando um gerador congruencial linear (LCG)
  próprio (`RandomGenerator`), por `roundNumber` rounds (eventos de chegada
  ou saída processados).
- **PRINT_RANDOM**: apenas gera `roundNumber` números pseudoaleatórios e
  grava em `seeds.txt`, sem rodar a simulação (útil para inspecionar a
  qualidade do gerador).

O modo é definido pela tag `<mode>` no `model.xml`.

### Como executar

Requer Maven instalado.

```sh
mvn exec:java -Dexec.mainClass="com.simple_queue.App"
```

Também existe o script `Commands/run` para conveniência.

O resultado é salvo em `result.txt`, na raiz do projeto, com a seguinte
estrutura, por fila:

```
QUEUE Nº <índice>
STATE   TIME    PROBABILITY
<n>     <tempo acumulado no estado n>    <probabilidade em %>
...
Number of losses: <quantidade de clientes rejeitados>
```

- **STATE**: número de clientes no sistema (fila + em atendimento).
- **TIME**: tempo total (na escala de tempo da simulação) em que a fila
  permaneceu naquele estado.
- **PROBABILITY**: `TIME` dividido pelo tempo total simulado.

Os modelos usados nos testes originais estão em
`src/main/resources/simulador/` (formato `.yml`, apenas para referência —
não são lidos pelo código, que lê somente `model.xml`).

### Correções aplicadas / Fixes applied

Em relação à versão original (tag `original`), esta versão corrige:

1. **Gerador de números aleatórios incorreto**
   (`NumberGenerator/RandomGenerator.java`): o estado do LCG era realimentado
   já dividido por `m`, em vez de manter o estado como inteiro entre
   chamadas — o que quebrava as propriedades estatísticas do gerador. Agora
   o estado (`lastX`) é mantido como inteiro, e só é dividido por `m` no
   valor retornado, como um LCG padrão.
2. **`roundNumber` agora conta rounds de fato**: no modo `RANDOM`, o
   critério de parada deixou de ser "número de sementes consumidas" (que
   variava por round) e passou a ser o número de rounds efetivamente
   processados (`Escalonador.indexRound`), que é o que o `roundNumber` do
   `model.xml` e o `README` sempre disseram fazer.
3. **Contador de índice duplicado**, em `Event` e `Queue`: removida a
   atribuição redundante no construtor (o campo já é inicializado uma única
   vez, na declaração), então os IDs voltam a ser sequenciais.
4. **Caminho de configuração fixo**: `Config.java` agora carrega
   `model.xml` via classpath (`getResourceAsStream`) em vez de um caminho
   relativo ao diretório de execução — funciona independente de onde o
   `.jar`/execução for disparado.
5. **Condição inatingível** em `Queue.chegada()`: removida a checagem morta
   `maxSize < 0`, que nunca era verdadeira (fila "infinita" usa
   `Integer.MAX_VALUE`, não um valor negativo).

---

## 🇺🇸 English

### About this repository

This project was built during undergraduate studies, as coursework for a
class on simulation / queueing theory. It simulates a **network of M/M/c
queues** (multiple servers per queue, with probabilistic routing between
queues) using **discrete-event simulation**, and at the end of the run
computes, for each queue, the probability of being in each state (number of
customers in the system), plus the number of losses (customers rejected due
to a full queue).

This version fixes bugs found during a code review done well after the
original submission (see the [changelog](#correções-aplicadas--fixes-applied)
above). The original, uncorrected submission was preserved under the
`original` tag as a historical record.

### How it works

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

### Run modes

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

### How to run

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

### Fixes applied

Compared to the original version (tag `original`), this version fixes:

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
