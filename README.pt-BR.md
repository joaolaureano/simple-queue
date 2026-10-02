# Simple Queue — V1 (versão original)

**[Read this in English / Leia em inglês](README.md)**

Um **simulador de eventos discretos** para redes de **filas M/M/c** (vários
servidores por fila, roteamento probabilístico entre filas). Ao fim da
execução, ele informa, para cada fila, quanto tempo ela passou em cada estado
(número de clientes no sistema), a probabilidade de cada estado e o número de
perdas. Desenvolvido como trabalho de uma disciplina de simulação / teoria
das filas na faculdade.

> **Esta é a versão original arquivada**, exatamente como foi entregue. A
> única mudança sobre ela é este README, que substitui as anotações originais
> em maiúsculas com a mesma informação. A versão corrigida está na branch
> [`master`](../../tree/master).

## Configuração

Os parâmetros são lidos de `src/main/resources/model.xml`:

- `queues` / `queue`: as filas da simulação, cada uma com:
  - `arrivalInterval`: intervalo dos eventos de chegada — dois valores separados por vírgula.
  - `departureInterval`: intervalo dos eventos de saída — dois valores separados por vírgula.
  - `sizeQueue`: a capacidade da fila.
  - `serverNumber`: o número de servidores.
- `network` / `connection`: as conexões entre filas, como origem e destino.
  `0,1` significa que a fila 1 recebe os eventos depois que eles são
  processados pela fila 0.
- `seed`: as sementes a usar, quando necessário.
- `roundNumber`: o número de rodadas a executar ao usar o gerador de números
  aleatórios.
- `mode`: o que o programa faz (ver abaixo).
- `arrivals` / `arrival`: quando acontece o primeiro evento de cada fila.
  `<arrival>0,2.5</arrival>` significa que a fila 0 começa com um evento no
  instante 2,5.

As filas recebem um id autoincremental na ordem em que aparecem (0, 1, 2,
...), e são esses ids que se usam em `network` e `arrivals`: com três filas,
`0,1` é válido e `1,3` não é.

## Modos

- `SEED`: executa com as sementes listadas em `seed`.
- `RANDOM`: executa com o gerador de números aleatórios próprio, por
  `roundNumber` rodadas.
- `PRINT_RANDOM`: apenas gera `roundNumber` sementes com o gerador.

## Executando

Requer Maven. `App.java` é a classe principal.

```sh
mvn exec:java -Dexec.mainClass="com.simple_queue.App"
```

`Commands/run` executa o mesmo comando. O resultado vai para `result.txt`,
com três colunas por fila: **STATE** (número de clientes), **TIME** (quanto
tempo a fila ficou com essa quantidade de clientes) e **PROBABILITY** (a
probabilidade desse estado).

Os modelos testados estão em `src/main/resources/simulador`, com o mesmo nome
do modelo XML e outra extensão.

## Problemas conhecidos desta versão

Foram encontrados depois e estão corrigidos na `master`:

1. O gerador de números aleatórios realimenta o estado já dividido por `m`,
   o que quebra o LCG.
2. No modo `RANDOM`, `roundNumber` conta números aleatórios consumidos, e não
   rodadas.
3. `Event` e `Queue` atribuem o contador de índice duas vezes, então os ids
   pulam.
4. O `model.xml` é carregado por um caminho relativo ao diretório de
   execução.
5. `Queue.chegada()` tem uma checagem `maxSize < 0` inatingível.
