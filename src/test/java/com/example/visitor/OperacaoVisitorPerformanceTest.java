package com.example.visitor;

import com.example.dispacher.Confirmavel;
import com.example.dispacher.Operacao;
import com.example.dispacher.OperacaoDispatcher;
import com.example.dispacher.OperacaoService;
import com.example.dispacher.PreExecutavel;
import com.example.dispacher.TipoOperacao;
import com.example.dispacher.Executavel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class OperacaoVisitorPerformanceTest {

    private static final int TOTAL_OPERACOES = 1_000_000;
    private static final int WARMUP_ROUNDS = 10;
    private static final int MEASURE_ROUNDS = 15;

    @Test
    void shouldCompareVisitorPerformanceAgainstManualDispatcher() {
        OperacaoDispatcher dispatcher = new OperacaoDispatcher(List.of(new BenchmarkOperacaoXService(), new BenchmarkOperacaoYService()));
        OperacaoVisitorProcessor visitorProcessor = new OperacaoVisitorProcessor();

        Operacao operacaoX = new Operacao(com.example.dispacher.TipoOperacao.OPERACAO_X);
        Operacao operacaoY = new Operacao(com.example.dispacher.TipoOperacao.OPERACAO_Y);

        var visitorExecutar = benchmarkVisitor("Visitor - executar", () -> visitorProcessor.executar(new OperacaoX()));
        var dispatcherExecutar = benchmarkDispatcher("Dispatcher manual - executar", () -> dispatcher.executar(operacaoX));

        var visitorPreExecutar = benchmarkVisitor("Visitor - preExecutar", () -> visitorProcessor.preExecutar(new OperacaoX()));
        var dispatcherPreExecutar = benchmarkDispatcher("Dispatcher manual - preExecutar", () -> dispatcher.preExecutar(operacaoX));

        var visitorConfirmar = benchmarkVisitor("Visitor - confirmar", () -> visitorProcessor.confirmar(new OperacaoY()));
        var dispatcherConfirmar = benchmarkDispatcher("Dispatcher manual - confirmar", () -> dispatcher.confirmar(operacaoY));

        printComparison("executar", visitorExecutar, dispatcherExecutar);
        printComparison("preExecutar", visitorPreExecutar, dispatcherPreExecutar);
        printComparison("confirmar", visitorConfirmar, dispatcherConfirmar);

        assertThat(visitorExecutar).isGreaterThan(0L);
        assertThat(dispatcherExecutar).isGreaterThan(0L);
        assertThat(visitorPreExecutar).isGreaterThan(0L);
        assertThat(dispatcherPreExecutar).isGreaterThan(0L);
        assertThat(visitorConfirmar).isGreaterThan(0L);
        assertThat(dispatcherConfirmar).isGreaterThan(0L);
    }

    private static long benchmarkVisitor(String label, Runnable action) {
        Logger logger = Logger.getLogger("com.example.visitor");
        Level previousLevel = logger.getLevel();
        boolean previousUseParentHandlers = logger.getUseParentHandlers();
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        try {
            warmUp(action);
            long total = 0L;
            for (int i = 0; i < MEASURE_ROUNDS; i++) {
                long inicio = System.nanoTime();
                for (int j = 0; j < TOTAL_OPERACOES; j++) {
                    action.run();
                }
                total += System.nanoTime() - inicio;
            }

            long media = total / MEASURE_ROUNDS;
            System.out.println(label + ": " + media + " ns total médio | " + (media / TOTAL_OPERACOES) + " ns/op");
            return media;
        } finally {
            logger.setLevel(previousLevel);
            logger.setUseParentHandlers(previousUseParentHandlers);
        }
    }

    private static long benchmarkDispatcher(String label, Runnable action) {
        Logger logger = Logger.getLogger("com.example.dispacher");
        Level previousLevel = logger.getLevel();
        boolean previousUseParentHandlers = logger.getUseParentHandlers();
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        try {
            warmUp(action);
            long total = 0L;
            for (int i = 0; i < MEASURE_ROUNDS; i++) {
                long inicio = System.nanoTime();
                for (int j = 0; j < TOTAL_OPERACOES; j++) {
                    action.run();
                }
                total += System.nanoTime() - inicio;
            }

            long media = total / MEASURE_ROUNDS;
            System.out.println(label + ": " + media + " ns total médio | " + (media / TOTAL_OPERACOES) + " ns/op");
            return media;
        } finally {
            logger.setLevel(previousLevel);
            logger.setUseParentHandlers(previousUseParentHandlers);
        }
    }

    private static void warmUp(Runnable action) {
        for (int i = 0; i < WARMUP_ROUNDS; i++) {
            for (int j = 0; j < TOTAL_OPERACOES / 10; j++) {
                action.run();
            }
        }
    }

    private static void printComparison(String nomeOperacao, long visitorTempo, long dispatcherTempo) {
        String vencedor = visitorTempo <= dispatcherTempo ? "Visitor" : "DispatcherManual";
        double percentual = visitorTempo == 0 ? 0 : ((double) Math.abs(visitorTempo - dispatcherTempo) / visitorTempo) * 100;

        System.out.printf("Resumo %s: Visitor=%d ns | DispatcherManual=%d ns | vencedor=%s | ganho do vencedor=%.2f%%%n",
                nomeOperacao,
                visitorTempo,
                dispatcherTempo,
                vencedor,
                percentual);
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
