package com.example.dispacher;

import org.junit.jupiter.api.Test;

import java.util.List;

class OperacaoDispatcherPerformanceTest {

    private static final int TOTAL_OPERACOES = 1_000_000;
    private static final int WARMUP_ROUNDS = 10;
    private static final int MEASURE_ROUNDS = 20;

    @Test
    void shouldMeasureSequentialPerformanceOfBothDispatchers() {
        OperacaoService operacaoXService = new BenchmarkOperacaoXService();
        OperacaoService operacaoYService = new BenchmarkOperacaoYService();

        OperacaoDispatcherLambda lambdaDispatcher = new OperacaoDispatcherLambda(List.of(operacaoXService, operacaoYService));
        OperacaoDispatcher dispatcher = new OperacaoDispatcher(List.of(operacaoXService, operacaoYService));

        Operacao operacaoX = new Operacao(TipoOperacao.OPERACAO_X);
        Operacao operacaoY = new Operacao(TipoOperacao.OPERACAO_Y);

        warmUp(() -> lambdaDispatcher.executar(operacaoX));
        warmUp(() -> dispatcher.executar(operacaoX));
        warmUp(() -> lambdaDispatcher.preExecutar(operacaoX));
        warmUp(() -> dispatcher.preExecutar(operacaoX));
        warmUp(() -> lambdaDispatcher.confirmar(operacaoY));
        warmUp(() -> dispatcher.confirmar(operacaoY));

        Result lambdaExecutar = measureAverage("Lambda - executar", () -> lambdaDispatcher.executar(operacaoX));
        Result optimizedExecutar = measureAverage("Manual - executar", () -> dispatcher.executar(operacaoX));

        Result lambdaPreExecutar = measureAverage("Lambda - preExecutar", () -> lambdaDispatcher.preExecutar(operacaoX));
        Result optimizedPreExecutar = measureAverage("Manual - preExecutar", () -> dispatcher.preExecutar(operacaoX));

        Result lambdaConfirmar = measureAverage("Lambda - confirmar", () -> lambdaDispatcher.confirmar(operacaoY));
        Result optimizedConfirmar = measureAverage("Manual - confirmar", () -> dispatcher.confirmar(operacaoY));

        System.out.println("\n=== Benchmark OperacaoDispatcher vs OperacaoDispatcherLambda ===");
        printResult(lambdaExecutar, optimizedExecutar, "executar");
        printResult(lambdaPreExecutar, optimizedPreExecutar, "preExecutar");
        printResult(lambdaConfirmar, optimizedConfirmar, "confirmar");
    }

    private static void warmUp(Runnable action) {
        for (int i = 0; i < WARMUP_ROUNDS; i++) {
            for (int j = 0; j < TOTAL_OPERACOES / 10; j++) {
                action.run();
            }
        }
    }

    private static Result measureAverage(String label, Runnable action) {
        long total = 0L;

        for (int i = 0; i < MEASURE_ROUNDS; i++) {
            long inicio = System.nanoTime();
            for (int j = 0; j < TOTAL_OPERACOES; j++) {
                action.run();
            }
            total += System.nanoTime() - inicio;
        }

        long tempoMedio = total / MEASURE_ROUNDS;
        long mediaPorOperacao = tempoMedio / TOTAL_OPERACOES;
        System.out.println(label + ": " + tempoMedio + " ns total médio | " + mediaPorOperacao + " ns/op");
        return new Result(label, tempoMedio, mediaPorOperacao);
    }

    private static void printResult(Result lambda, Result optimized, String nomeOperacao) {
        String vencedor = lambda.tempoTotal() <= optimized.tempoTotal() ? "Lambda" : "Manual";
        double percentual = lambda.tempoTotal() == 0 ? 0 : ((double) Math.abs(lambda.tempoTotal() - optimized.tempoTotal()) / lambda.tempoTotal()) * 100;

        System.out.printf("Resumo %s: Lambda=%d ns | Manual=%d ns | vencedor=%s | ganho do Manual=%.2f%%%n",
                nomeOperacao,
                lambda.tempoTotal(),
                optimized.tempoTotal(),
                vencedor,
                percentual);
    }

    private record Result(String label, long tempoTotal, long mediaPorOperacao) {
    }

    private static class BenchmarkOperacaoXService implements OperacaoService, Executavel, PreExecutavel, Confirmavel {
        @Override
        public TipoOperacao getTipo() {
            return TipoOperacao.OPERACAO_X;
        }

        @Override
        public void executar(Operacao operacao) {
            // sem-op para reduzir overhead do benchmark
        }

        @Override
        public void preExecutar(Operacao operacao) {
            // sem-op para reduzir overhead do benchmark
        }

        @Override
        public void confirmar(Operacao operacao) {
            // sem-op para reduzir overhead do benchmark
        }
    }

    private static class BenchmarkOperacaoYService implements OperacaoService, Confirmavel {
        @Override
        public TipoOperacao getTipo() {
            return TipoOperacao.OPERACAO_Y;
        }

        @Override
        public void confirmar(Operacao operacao) {
            // sem-op para reduzir overhead do benchmark
        }
    }
}
