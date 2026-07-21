package com.example.dispacher;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class OperacaoDispatcherMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DispacherApplication.class, args);

        try {
            OperacaoDispatcher dispatcher = context.getBean(OperacaoDispatcher.class);

            Operacao operacaoX = new Operacao(TipoOperacao.OPERACAO_X);
            Operacao operacaoY = new Operacao(TipoOperacao.OPERACAO_Y);
            Operacao operacaoZ = new Operacao(TipoOperacao.OPERACAO_Z);

            System.out.println("=== Testando OperacaoX ===");
            dispatcher.executar(operacaoX);
            dispatcher.preExecutar(operacaoX);
            dispatcher.confirmar(operacaoX);

            System.out.println("\n=== Testando OperacaoY ===");
            dispatcher.executar(operacaoY);
            dispatcher.confirmar(operacaoY);

            System.out.println("\n=== Testando Operacao inexistente no registro ===");
            dispatcher.confirmar(operacaoZ);
        } finally {
            context.close();
        }
    }
}
