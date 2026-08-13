# Dispacher

Projeto de exemplo para comparação de duas implementações de dispatcher de operações.

## Objetivo

Este projeto demonstra e mede o custo de execução de:

- `OperacaoDispatcher` — implementação manual sem lambda
- `OperacaoDispatcherLambda` — implementação original com lambda

## Estrutura principal

- `src/main/java/com/example/dispacher/OperacaoDispatcher.java`
- `src/main/java/com/example/dispacher/OperacaoDispatcherLambda.java`
- `src/main/java/com/example/dispacher/TipoOperacao.java`
- `src/main/java/com/example/dispacher/Operacao.java`
- `src/jmh/java/com/example/dispacher/OperacaoDispatcherJmhBenchmark.java`

## Como executar os testes

```powershell
cd D:\2026\WORK\WORKSPACE\Dispacher
.\mvnw -q test
```

## Como executar o benchmark JMH

1. Compile o projeto:

```powershell
cd D:\2026\WORK\WORKSPACE\Dispacher
.\mvnw -q -DskipTests compile
```

2. Gere o classpath de dependências:

```powershell
.\mvnw org.apache.maven.plugins:maven-dependency-plugin:3.6.1:build-classpath -DincludeScope=test > target\cp.txt
```

3. Rode o benchmark:

```powershell
$cpLine = (Get-Content .\target\cp.txt | Select-String "C:\\Users\\.*\.jar" -AllMatches | ForEach-Object { $_.Matches.Value } | Select-Object -Last 1)
$cp = "target\classes;target\test-classes;" + $cpLine
java -cp "$cp" org.openjdk.jmh.Main com.example.dispacher.OperacaoDispatcherJmhBenchmark -wi 3 -i 5 -f 1 -bm avgt -tu ns
```

## Documentação de benchmark

- [Comparação JMH atual: Dispatcher vs Lambda vs Visitor](docs/benchmark-comparacao-jmh.md)

## Observações

- O benchmark foi executado com JMH em ambiente local.
- Os serviços usados no benchmark são `no-op` para isolar o custo do dispatch da lógica de negócio.
- O JMH pode exibir avisos de `sun.misc.Unsafe`, mas a execução concluída foi bem-sucedida.
