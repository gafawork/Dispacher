# Benchmark JMH do OperacaoDispatcher

## Objetivo

Comparar o custo de execução entre:

- `OperacaoDispatcher` (implementação manual sem lambda)
- `OperacaoDispatcherLambda` (implementação original com lambda)

A classe de benchmark está em:

- `src/jmh/java/com/example/dispacher/OperacaoDispatcherJmhBenchmark.java`

## Como executar

1. Compile o projeto:

```powershell
cd D:\2026\WORK\WORKSPACE\Dispacher
.\mvnw -q -DskipTests compile
```

2. Gere o classpath de dependências do projeto:

```powershell
.\mvnw org.apache.maven.plugins:maven-dependency-plugin:3.6.1:build-classpath -DincludeScope=test > target\cp.txt
```

3. Extraia a linha de classpath gerada pelo plugin e rode o benchmark JMH:

```powershell
$cpLine = (Get-Content .\target\cp.txt | Select-String "C:\\Users\\.*\.jar" -AllMatches | ForEach-Object { $_.Matches.Value } | Select-Object -Last 1)
$cp = "target\classes;target\test-classes;" + $cpLine
java -cp "$cp" org.openjdk.jmh.Main com.example.dispacher.OperacaoDispatcherJmhBenchmark -wi 3 -i 5 -f 1 -bm avgt -tu ns
```

Observação: o plugin `maven-dependency-plugin` imprime mensagens no console antes do classpath. Por isso, a extração por `Select-String` evita incluir linhas de log na variável `$cp`.

## Resultado obtido

Execução realizada em ambiente local com JDK 26.0.1, usando JMH.

### Média por operação

| Benchmark | Resultado |
| --- | --- |
| `lambdaConfirmar` | 3,355 ± 0,091 ns/op |
| `lambdaExecutar` | 3,361 ± 0,028 ns/op |
| `lambdaPreExecutar` | 3,422 ± 0,092 ns/op |
| `manualConfirmar` | 1,005 ± 0,019 ns/op |
| `manualExecutar` | 1,013 ± 0,016 ns/op |
| `manualPreExecutar` | 1,010 ± 0,011 ns/op |

### Interpretação

Na execução validada:

- `manualExecutar` foi ~3,3x mais rápido que `lambdaExecutar`
- `manualPreExecutar` foi ~3,4x mais rápido que `lambdaPreExecutar`
- `manualConfirmar` foi ~3,3x mais rápido que `lambdaConfirmar`

Em termos gerais, a implementação manual se mostrou mais eficiente no cenário medido.

## Observações

- O benchmark foi configurado com warmup e medição controlada pelo JMH.
- A classe de benchmark usa serviços sem operação real (`no-op`) para reduzir ruído e medir principalmente o custo do dispatch.
- O JMH gerou avisos relacionados a `sun.misc.Unsafe`, mas a execução foi concluída com sucesso e os dados apresentados acima são válidos para este ambiente.
