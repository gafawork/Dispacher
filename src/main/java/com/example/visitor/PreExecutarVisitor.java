package com.example.visitor;

import java.util.logging.Logger;

public class PreExecutarVisitor implements OperacaoVisitor {
    private static final Logger log = Logger.getLogger(PreExecutarVisitor.class.getName());

    @Override
    public void visitar(OperacaoX operacao) {
        log.info("Pré-executando operação X");
    }

    @Override
    public void visitar(OperacaoY operacao) {
        log.info("Operação Y não possui a funcionalidade 'pré-executar'");
    }

    @Override
    public void visitar(OperacaoZ operacao) {
        log.warning("Nenhum serviço registrado para a operação " + operacao.getTipo());
    }
}
