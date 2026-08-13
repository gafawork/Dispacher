package com.example.visitor;

public interface OperacaoVisitor {
    void visitar(OperacaoX operacao);

    void visitar(OperacaoY operacao);

    void visitar(OperacaoZ operacao);
}
