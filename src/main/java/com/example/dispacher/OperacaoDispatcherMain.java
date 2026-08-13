package com.example.dispacher;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.OutputStream;
import java.io.PrintStream;

public class OperacaoDispatcherMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DispacherApplication.class, args);

        try {
            OperacaoDispatcher dispatcher = context.getBean(OperacaoDispatcher.class);

            Operacao operacaoX = new Operacao(TipoOperacao.OPERACAO_X);
            Operacao operacaoY = new Operacao(TipoOperacao.OPERACAO_Y);
            Operacao operacaoZ = new Operacao(TipoOperacao.OPERACAO_Z);

            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(OutputStream.nullOutputStream()));
            try {
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
                System.setOut(originalOut);
            }
        } finally {
            context.close();
        }
    }
}
