# Simple Queue

> Simulador de eventos discretos para redes de filas (M/M/c) — projeto de faculdade.
> Discrete-event simulator for queueing networks (M/M/c) — a college project.

This repository is part of a personal archive of projects developed during
university (college). It is kept close to its original form on purpose — see
the notes below.

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

Esta tag/versão (`original`) preserva o código **exatamente como foi entregue
na época**, incluindo bugs conhecidos, para servir de registro histórico.
Uma versão revisada, com os bugs corrigidos, está disponível na tag
`bugfix` (ou na branch principal, se você estiver lendo isso depois da
correção).

> **Aviso:** esta versão contém bugs conhecidos que afetam a corretude dos
> resultados, listados na seção [Bugs conhecidos](#bugs-conhecidos-nesta-versão)
> abaixo. Não utilize esta versão para gerar resultados que você pretenda usar
> de fato — use a versão corrigida.

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
- `roundNumber`: quantidade de "rounds" a executar no modo `RANDOM`.
- `mode`: o modo de execução — `SEED`, `RANDOM` ou `PRINT_RANDOM` (ver abaixo).

Cada fila recebe um índice automático (0, 1, 2, ...) na ordem em que aparece
no XML — é esse índice que deve ser usado em `network` e `arrivals`.

### Modos de execução

- **SEED**: a simulação roda usando exatamente os números da lista `seed` do
  XML, na ordem. Útil para reproduzir um resultado específico (ex.: para
  verificar contas feitas manualmente em sala de aula).
- **RANDOM**: a simulação roda usando um gerador congruencial linear (LCG)
  próprio (`RandomGenerator`), por `roundNumber` chamadas.
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

### Bugs conhecidos nesta versão

Identificados em revisão posterior do código (mantidos aqui para registro
histórico; corrigidos na tag `bugfix`):

1. **Gerador de números aleatórios incorreto**
   (`NumberGenerator/RandomGenerator.java`): o estado do gerador congruencial
   linear é realimentado já dividido por `m`, em vez de manter o estado como
   inteiro. Isso quebra as propriedades estatísticas do gerador — afeta
   diretamente o modo `RANDOM`, que é o modo configurado por padrão no
   `model.xml` deste repositório.
2. **`roundNumber` não corresponde a "rounds" da simulação**: no modo
   `RANDOM`, o contador é decrementado a cada número aleatório consumido, e
   cada round pode consumir de 1 a 4 números — então o número real de
   eventos simulados é menor (e variável) em relação ao configurado.
3. **Contador de índice duplicado** em `Event` e `Queue`: o índice interno é
   incrementado duas vezes por objeto criado (inicializador de campo +
   atribuição no construtor), fazendo os IDs saltarem de 2 em 2. Não afeta a
   corretude da simulação, só os IDs internos.
4. **Caminho de configuração fixo**: `Config.java` lê
   `./src/main/resources/model.xml` como caminho relativo ao diretório de
   execução, em vez de carregar via classpath — só funciona se executado a
   partir da raiz do projeto.
5. **Condição inatingível** em `Queue.chegada()`: a checagem `maxSize < 0`
   nunca é verdadeira, pois o valor padrão de fila "infinita" é
   `Integer.MAX_VALUE`, não um valor negativo.

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

This tag/version (`original`) preserves the code **exactly as it was
submitted at the time**, including known bugs, to serve as a historical
record. A revised version with the bugs fixed is available under the
`bugfix` tag (or on the main branch, if you're reading this after the fix
was applied).

> **Warning:** this version has known bugs that affect the correctness of
> its results, listed in [Known bugs](#known-bugs-in-this-version) below.
> Don't rely on this version to produce results you intend to actually use —
> use the fixed version instead.

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
- `roundNumber`: number of "rounds" to run in `RANDOM` mode.
- `mode`: the run mode — `SEED`, `RANDOM`, or `PRINT_RANDOM` (see below).

Each queue gets an automatic index (0, 1, 2, ...) in the order it appears in
the XML — that index is what you use in `network` and `arrivals`.

### Run modes

- **SEED**: the simulation runs using exactly the numbers listed in the
  XML's `seed` tag, in order. Useful for reproducing a specific result
  (e.g. to check hand-computed results from class).
- **RANDOM**: the simulation runs using a custom linear congruential
  generator (`RandomGenerator`), for `roundNumber` draws.
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

### Known bugs in this version

Found during a later code review (kept here for the historical record; fixed
in the `bugfix` tag):

1. **Incorrect random number generator**
   (`NumberGenerator/RandomGenerator.java`): the linear congruential
   generator's state is fed back already divided by `m`, instead of being
   kept as an integer. This breaks the generator's statistical properties —
   it directly affects `RANDOM` mode, which is the mode configured by
   default in this repository's `model.xml`.
2. **`roundNumber` doesn't map to simulation "rounds"**: in `RANDOM` mode,
   the counter is decremented every time a random number is drawn, and each
   round can draw anywhere from 1 to 4 numbers — so the actual number of
   simulated events ends up smaller (and variable) relative to what was
   configured.
3. **Duplicate index counter** in `Event` and `Queue`: the internal index is
   incremented twice per created object (once in the field initializer, once
   in the constructor body), so IDs skip by 2. Doesn't affect simulation
   correctness, only the internal IDs.
4. **Hardcoded config path**: `Config.java` reads
   `./src/main/resources/model.xml` as a path relative to the working
   directory, instead of loading it from the classpath — it only works when
   run from the project root.
5. **Unreachable condition** in `Queue.chegada()`: the `maxSize < 0` check is
   never true, since the default value for an "unbounded" queue is
   `Integer.MAX_VALUE`, not a negative number.
