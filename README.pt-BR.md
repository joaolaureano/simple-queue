# Simple Queue

**[Read this in English / Leia em inglês](README.md)**

> Simulador de eventos discretos para redes de filas (M/M/c) — projeto de faculdade.

Este repositório faz parte de um arquivo pessoal de projetos desenvolvidos
durante a graduação. A entrega original está preservada na tag
[`V1`](../../releases/tag/V1); a branch padrão (`master`) corrige os bugs
encontrados nela — veja [Correções aplicadas](#correções-aplicadas) abaixo.

## Sobre este repositório

Este projeto foi desenvolvido durante a graduação, como parte de uma disciplina
sobre simulação / teoria das filas. Ele simula uma **rede de filas M/M/c**
(múltiplos servidores, com roteamento probabilístico entre filas) usando
**simulação de eventos discretos**, e calcula, ao final da execução, a
probabilidade de cada fila estar em cada estado (quantidade de clientes no
sistema), além do número de perdas (clientes rejeitados por falta de espaço
na fila).

Esta versão corrige bugs identificados em uma revisão de código feita bem
depois da entrega original (ver
[Correções aplicadas](#correções-aplicadas)).
A submissão original, sem essas correções, foi preservada na tag `V1`,
como registro histórico.

## Como funciona

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

## Modos de execução

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

## Como executar

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

## Correções aplicadas

Em relação à versão original (tag `V1`), esta versão corrige:

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
