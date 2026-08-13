package com.example.visitor;

import com.example.dispacher.Confirmavel;
import com.example.dispacher.Executavel;
import com.example.dispacher.Operacao;
import com.example.dispacher.OperacaoDispatcher;
import com.example.dispacher.OperacaoService;
import com.example.dispacher.PreExecutavel;
import com.example.dispacher.TipoOperacao;
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
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
@State(Scope.Thread)
public class OperacaoVisitorJmhBenchmark {

    private OperacaoDispatcher dispatcher;
    private OperacaoVisitorProcessor visitorProcessor;
    private Operacao operacaoX;
    private Operacao operacaoY;
    private com.example.visitor.Operacao operacaoVisitorX;
    private com.example.visitor.Operacao operacaoVisitorY;
    private java.util.logging.Level previousVisitorLevel;
    private boolean previousVisitorUseParentHandlers;
    private java.util.logging.Level previousDispatcherLevel;
    private boolean previousDispatcherUseParentHandlers;

    @Setup(Level.Trial)
    public void setup() {
        Logger visitorLogger = Logger.getLogger("com.example.visitor");
        Logger dispatcherLogger = Logger.getLogger("com.example.dispacher");

        previousVisitorLevel = visitorLogger.getLevel();
        previousVisitorUseParentHandlers = visitorLogger.getUseParentHandlers();
        previousDispatcherLevel = dispatcherLogger.getLevel();
        previousDispatcherUseParentHandlers = dispatcherLogger.getUseParentHandlers();

        visitorLogger.setLevel(java.util.logging.Level.OFF);
        visitorLogger.setUseParentHandlers(false);
        dispatcherLogger.setLevel(java.util.logging.Level.OFF);
        dispatcherLogger.setUseParentHandlers(false);

        OperacaoService operacaoXService = new BenchmarkOperacaoXService();
        OperacaoService operacaoYService = new BenchmarkOperacaoYService();

        dispatcher = new OperacaoDispatcher(List.of(operacaoXService, operacaoYService));
        visitorProcessor = new OperacaoVisitorProcessor();

        operacaoX = new Operacao(TipoOperacao.OPERACAO_X);
        operacaoY = new Operacao(TipoOperacao.OPERACAO_Y);
        operacaoVisitorX = new OperacaoX();
        operacaoVisitorY = new OperacaoY();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        Logger visitorLogger = Logger.getLogger("com.example.visitor");
        Logger dispatcherLogger = Logger.getLogger("com.example.dispacher");

        visitorLogger.setLevel(previousVisitorLevel);
        visitorLogger.setUseParentHandlers(previousVisitorUseParentHandlers);
        dispatcherLogger.setLevel(previousDispatcherLevel);
        dispatcherLogger.setUseParentHandlers(previousDispatcherUseParentHandlers);
    }

    @Benchmark
    public void visitorExecutar() {
        visitorProcessor.executar(operacaoVisitorX);
    }

    @Benchmark
    public void dispatcherExecutar() {
        dispatcher.executar(operacaoX);
    }

    @Benchmark
    public void visitorPreExecutar() {
        visitorProcessor.preExecutar(operacaoVisitorX);
    }

    @Benchmark
    public void dispatcherPreExecutar() {
        dispatcher.preExecutar(operacaoX);
    }

    @Benchmark
    public void visitorConfirmar() {
        visitorProcessor.confirmar(operacaoVisitorY);
    }

    @Benchmark
    public void dispatcherConfirmar() {
        dispatcher.confirmar(operacaoY);
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(OperacaoVisitorJmhBenchmark.class.getSimpleName())
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
            // no-op
        }

        @Override
        public void preExecutar(Operacao operacao) {
            // no-op
        }

        @Override
        public void confirmar(Operacao operacao) {
            // no-op
        }
    }

    private static class BenchmarkOperacaoYService implements OperacaoService, Confirmavel {
        @Override
        public TipoOperacao getTipo() {
            return TipoOperacao.OPERACAO_Y;
        }

        @Override
        public void confirmar(Operacao operacao) {
            // no-op
        }
    }
}
