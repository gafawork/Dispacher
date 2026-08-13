# Visitor Pattern - Exemplo com Operações

Este documento descreve a implementação do padrão Visitor no projeto, aplicada ao cenário de operações com comportamentos específicos como `executar`, `preExecutar` e `confirmar`.

## Estrutura do pacote

O exemplo está no pacote:

- `com.example.visitor`

Arquivos principais:

- `Operacao.java`
- `OperacaoX.java`
- `OperacaoY.java`
- `OperacaoZ.java`
- `OperacaoVisitor.java`
- `ExecutarVisitor.java`
- `PreExecutarVisitor.java`
- `ConfirmarVisitor.java`
- `OperacaoVisitorProcessor.java`
- `OperacaoVisitorMain.java`

## Ideia do padrão Visitor

No padrão Visitor, a lógica de processamento fica separada das classes de domínio.

A estrutura é:

- `Operacao` define a operação concreta e delega a execução para o visitor
- cada operação concreta implementa `aceitar(OperacaoVisitor visitor)`
- os visitantes implementam a lógica específica por tipo de operação

Isso permite adicionar novos comportamentos sem alterar as classes de operação.

## Como funciona no exemplo

A classe base:

```java
public abstract class Operacao {
    private final TipoOperacao tipo;

    protected Operacao(TipoOperacao tipo) {
        this.tipo = tipo;
    }

    public TipoOperacao getTipo() {
        return tipo;
    }

    public abstract void aceitar(OperacaoVisitor visitor);
}
```

Cada operação concreta faz o double-dispatch:

```java
public class OperacaoX extends Operacao {
    public OperacaoX() {
        super(TipoOperacao.OPERACAO_X);
    }

    @Override
    public void aceitar(OperacaoVisitor visitor) {
        visitor.visitar(this);
    }
}
```

O contrato do visitor:

```java
public interface OperacaoVisitor {
    void visitar(OperacaoX operacao);
    void visitar(OperacaoY operacao);
    void visitar(OperacaoZ operacao);
}
```

Os visitantes específicos:

```java
public class ExecutarVisitor implements OperacaoVisitor {
    @Override
    public void visitar(OperacaoX operacao) {
        log.info("Executando operação X");
    }

    @Override
    public void visitar(OperacaoY operacao) {
        log.info("Operação Y não possui a funcionalidade 'executar'");
    }
}
```

E o processador central:

```java
public class OperacaoVisitorProcessor {
    public void executar(Operacao operacao) {
        operacao.aceitar(new ExecutarVisitor());
    }

    public void preExecutar(Operacao operacao) {
        operacao.aceitar(new PreExecutarVisitor());
    }

    public void confirmar(Operacao operacao) {
        operacao.aceitar(new ConfirmarVisitor());
    }
}
```

## Exemplo de uso

```java
OperacaoVisitorProcessor processor = new OperacaoVisitorProcessor();

processor.executar(new OperacaoX());
processor.preExecutar(new OperacaoX());
processor.confirmar(new OperacaoY());
```

## Quando usar este padrão

O Visitor é útil quando:

- a estrutura de objetos é estável
- você precisa adicionar muitas operações diferentes sem mexer nas classes do domínio
- a lógica de negócio é específica e varia por tipo de objeto

## Ponto de atenção

O Visitor é mais apropriado quando o conjunto de tipos é relativamente fixo. Se for necessário adicionar novos tipos com frequência, a manutenção pode ficar mais complexa.

## Execução do exemplo

```powershell
cd D:\2026\WORK\WORKSPACE\Dispacher
.\mvnw -q spring-boot:run -Dspring-boot.run.main-class=com.example.visitor.OperacaoVisitorMain
```

## Comparação com Dispatcher

A versão Visitor tem a vantagem de deixar o processamento explícito e orientado ao tipo da operação, sem depender de `Map`, `instanceof` ou lambda em um único ponto central. Já o padrão `Dispatcher` tende a ser mais direto quando a variação é principalmente por operação e não por tipo concreto.
