package com.example.visitor;

public class OperacaoVisitorMain {

    public static void main(String[] args) {
        OperacaoVisitorProcessor dispatcher = new OperacaoVisitorProcessor();

        Operacao operacaoX = new OperacaoX();
        Operacao operacaoY = new OperacaoY();
        Operacao operacaoZ = new OperacaoZ();

        System.out.println("=== Testando OperacaoX ===");
        dispatcher.executar(operacaoX);
        dispatcher.preExecutar(operacaoX);
        dispatcher.confirmar(operacaoX);

        System.out.println("\n=== Testando OperacaoY ===");
        dispatcher.executar(operacaoY);
        dispatcher.confirmar(operacaoY);

        System.out.println("\n=== Testando Operacao inexistente ===");
        dispatcher.confirmar(operacaoZ);
    }
}
