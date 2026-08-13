# Comparação JMH: Dispatcher, Lambda e Visitor

## Objetivo

Este documento consolida a comparação de desempenho executada com JMH entre as implementações do projeto:

- `OperacaoDispatcher` — implementação manual otimizada sem lambda
- `OperacaoDispatcherLambda` — implementação original com lambda
- `OperacaoVisitorProcessor` — implementação baseada no padrão Visitor

A intenção é medir o custo do mecanismo de despacho em si, isolando a lógica de negócio real dos serviços usados no benchmark.

## Ambiente da execução

- JDK: 26.0.1
- JMH: 1.37
- JVM: HotSpot 64-bit Server VM
- Threads: 1
- Modo: Average time (`avgt`)
- Unidade: ns/op

## Como executar

A partir da raiz do projeto:

```powershell
cd D:\2026\WORK\WORKSPACE\Dispacher
.\mvnw -q -DskipTests test-compile
.\mvnw -q dependency:build-classpath -DincludeScope=test '-Dmdep.outputFile=cp.txt'

$cp = (Get-Content .\cp.txt) + ';target\classes;target\test-classes'
java -cp $cp com.example.dispacher.OperacaoDispatcherJmhBenchmark
java -cp $cp com.example.visitor.OperacaoVisitorJmhBenchmark
```

## Resultado atual — Dispatcher vs Lambda

| Benchmark | Resultado médio |
| --- | --- |
| `lambdaExecutar` | 3,404 ± 0,121 ns/op |
| `manualExecutar` | 1,036 ± 0,031 ns/op |
| `lambdaPreExecutar` | 3,437 ± 0,231 ns/op |
| `manualPreExecutar` | 1,037 ± 0,055 ns/op |
| `lambdaConfirmar` | 5,268 ± 0,376 ns/op |
| `manualConfirmar` | 1,011 ± 0,015 ns/op |

### Interpretação

A implementação manual foi mais rápida em todos os cenários avaliados:

- `manualExecutar` foi cerca de 3,3x mais rápido que `lambdaExecutar`
- `manualPreExecutar` foi cerca de 3,3x mais rápido que `lambdaPreExecutar`
- `manualConfirmar` foi cerca de 5,2x mais rápido que `lambdaConfirmar`

## Resultado atual — Visitor vs Dispatcher manual

| Benchmark | Resultado médio |
| --- | --- |
| `dispatcherExecutar` | 1,034 ± 0,051 ns/op |
| `visitorExecutar` | 1,119 ± 0,029 ns/op |
| `dispatcherPreExecutar` | 1,033 ± 0,028 ns/op |
| `visitorPreExecutar` | 1,131 ± 0,068 ns/op |
| `dispatcherConfirmar` | 1,024 ± 0,008 ns/op |
| `visitorConfirmar` | 1,108 ± 0,020 ns/op |

### Interpretação

No cenário atual, o `Dispatcher manual` manteve vantagem em todas as operações:

- executar: ~7,9% melhor
- preExecutar: ~8,6% melhor
- confirmar: ~7,6% melhor

Apesar de o padrão Visitor ser mais estruturado do ponto de vista de extensibilidade, no microbenchmark executado o custo do double dispatch e da estrutura do padrão foi ligeiramente maior do que o caminho direto do dispatcher manual.

## Conclusão

Para o cenário medido e este ambiente específico, a ordem prática de desempenho foi:

1. `OperacaoDispatcher` (mais eficiente)
2. `OperacaoVisitorProcessor` (próximo, mas mais lento)
3. `OperacaoDispatcherLambda` (mais lento)

Isso não significa que o Visitor seja pior em todos os casos. Em cenários com mais operações e regras de negócio mais complexas, o Visitor pode compensar em clareza de design e extensibilidade. Porém, para desempenho puro de despacho, o dispatcher manual mostrou melhor resultado no benchmark executado.

## Observações

- Os serviços usados no benchmark são `no-op` para reduzir ruído e medir predominantemente o custo do dispatch.
- Houve avisos do JMH sobre `sun.misc.Unsafe`, mas a execução foi concluída com sucesso.
- Os números devem ser interpretados como indicativos do ambiente local, e não como valores absolutos para todos os JDKs ou máquinas.
