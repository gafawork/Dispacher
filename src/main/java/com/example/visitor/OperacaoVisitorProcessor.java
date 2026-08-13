package com.example.visitor;

import java.util.logging.Logger;

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
