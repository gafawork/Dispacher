package com.example.dispacher;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class OperacaoDispatcher {

    private static final Logger log = Logger.getLogger(OperacaoDispatcher.class.getName());

    public OperacaoDispatcher(List<OperacaoService> lista) {
        for (TipoOperacao tipo : TipoOperacao.values()) {
            tipo.associarServico(null);
        }

        for (OperacaoService servico : lista) {
            servico.getTipo().associarServico(servico);
        }
    }

    public void executar(Operacao operacao) {
        OperacaoService servico = operacao.getTipo().getServico();

        if (servico == null) {
            log.warning("Nenhum serviço registrado para a operação " + operacao.getTipo());
            return;
        }

        if (servico instanceof Executavel) {
            ((Executavel) servico).executar(operacao);
            return;
        }

        log.info("Operação " + operacao.getTipo() + " não possui a funcionalidade 'executar'");
    }

    public void preExecutar(Operacao operacao) {
        OperacaoService servico = operacao.getTipo().getServico();

        if (servico == null) {
            log.warning("Nenhum serviço registrado para a operação " + operacao.getTipo());
            return;
        }

        if (servico instanceof PreExecutavel) {
            ((PreExecutavel) servico).preExecutar(operacao);
            return;
        }

        log.info("Operação " + operacao.getTipo() + " não possui a funcionalidade 'pré-executar'");
    }

    public void confirmar(Operacao operacao) {
        OperacaoService servico = operacao.getTipo().getServico();

        if (servico == null) {
            log.warning("Nenhum serviço registrado para a operação " + operacao.getTipo());
            return;
        }

        if (servico instanceof Confirmavel) {
            ((Confirmavel) servico).confirmar(operacao);
            return;
        }

        log.info("Operação " + operacao.getTipo() + " não possui a funcionalidade 'confirmar'");
    }
}