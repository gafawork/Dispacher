package com.example.visitor;

import java.util.logging.Logger;

public class ConfirmarVisitor implements OperacaoVisitor {
    private static final Logger log = Logger.getLogger(ConfirmarVisitor.class.getName());

    @Override
    public void visitar(OperacaoX operacao) {
        log.info("Confirmando operação X");
    }

    @Override
    public void visitar(OperacaoY operacao) {
        log.info("Confirmando operação Y");
    }

    @Override
    public void visitar(OperacaoZ operacao) {
        log.warning("Nenhum serviço registrado para a operação " + operacao.getTipo());
    }
}
