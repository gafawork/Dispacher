package com.example.visitor;

import java.util.logging.Logger;

public class ExecutarVisitor implements OperacaoVisitor {
    private static final Logger log = Logger.getLogger(ExecutarVisitor.class.getName());

    @Override
    public void visitar(OperacaoX operacao) {
        log.info("Executando operação X");
    }

    @Override
    public void visitar(OperacaoY operacao) {
        log.info("Operação Y não possui a funcionalidade 'executar'");
    }

    @Override
    public void visitar(OperacaoZ operacao) {
        log.warning("Nenhum serviço registrado para a operação " + operacao.getTipo());
    }
}
