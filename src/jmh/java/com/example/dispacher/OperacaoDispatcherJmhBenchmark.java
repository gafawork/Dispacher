package com.example.dispacher;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
@State(Scope.Thread)
public class OperacaoDispatcherJmhBenchmark {

    private OperacaoDispatcher dispatcher;
    private OperacaoDispatcherLambda lambdaDispatcher;
    private Operacao operacaoX;
    private Operacao operacaoY;

    @Setup(Level.Trial)
    public void setup() {
        OperacaoService operacaoXService = new BenchmarkOperacaoXService();
        OperacaoService operacaoYService = new BenchmarkOperacaoYService();

        dispatcher = new OperacaoDispatcher(List.of(operacaoXService, operacaoYService));
        lambdaDispatcher = new OperacaoDispatcherLambda(List.of(operacaoXService, operacaoYService));

        operacaoX = new Operacao(TipoOperacao.OPERACAO_X);
        operacaoY = new Operacao(TipoOperacao.OPERACAO_Y);
    }

    @Benchmark
    public void lambdaExecutar() {
        lambdaDispatcher.executar(operacaoX);
    }

    @Benchmark
    public void manualExecutar() {
        dispatcher.executar(operacaoX);
    }

    @Benchmark
    public void lambdaPreExecutar() {
        lambdaDispatcher.preExecutar(operacaoX);
    }

    @Benchmark
    public void manualPreExecutar() {
        dispatcher.preExecutar(operacaoX);
    }

    @Benchmark
    public void lambdaConfirmar() {
        lambdaDispatcher.confirmar(operacaoY);
    }

    @Benchmark
    public void manualConfirmar() {
        dispatcher.confirmar(operacaoY);
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(OperacaoDispatcherJmhBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(options).run();
    }

    private static class BenchmarkOperacaoXService implements OperacaoService, Executavel, PreExecutavel, Confirmavel {
        @Override
        public TipoOperacao getTipo() {
            return TipoOperacao.OPERACAO_X;
        }

        @Override
        public void executar(Operacao operacao) {
            // sem-op para reduzir ruído no benchmark
        }

        @Override
        public void preExecutar(Operacao operacao) {
            // sem-op para reduzir ruído no benchmark
        }

        @Override
        public void confirmar(Operacao operacao) {
            // sem-op para reduzir ruído no benchmark
        }
    }

    private static class BenchmarkOperacaoYService implements OperacaoService, Confirmavel {
        @Override
        public TipoOperacao getTipo() {
            return TipoOperacao.OPERACAO_Y;
        }

        @Override
        public void confirmar(Operacao operacao) {
            // sem-op para reduzir ruído no benchmark
        }
    }
}
