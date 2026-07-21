package com.example.dispacher;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class OperacaoDispatcher {

    private static final Logger log = Logger.getLogger(OperacaoDispatcher.class.getName());

    private final Map<TipoOperacao, OperacaoService> servicos;

    public OperacaoDispatcher(List<OperacaoService> lista) {
        this.servicos = lista.stream()
                .collect(Collectors.toMap(OperacaoService::getTipo, s -> s));
    }

    public void executar(Operacao operacao) {
        aplicar(operacao, Executavel.class, Executavel::executar, "executar");
    }

    public void preExecutar(Operacao operacao) {
        aplicar(operacao, PreExecutavel.class, PreExecutavel::preExecutar, "pré-executar");
    }

    public void confirmar(Operacao operacao) {
        aplicar(operacao, Confirmavel.class, Confirmavel::confirmar, "confirmar");
    }

    @SuppressWarnings("unchecked")
    private <T> void aplicar(Operacao operacao, Class<T> capacidade,
                             BiConsumer<T, Operacao> acao, String nomeFuncionalidade) {

        OperacaoService servico = servicos.get(operacao.getTipo());

        if (servico == null) {
            log.warning("Nenhum serviço registrado para a operação " + operacao.getTipo());
            return;
        }

        if (capacidade.isInstance(servico)) {
            acao.accept((T) servico, operacao);
        } else {
            log.info("Operação " + operacao.getTipo() + " não possui a funcionalidade '" + nomeFuncionalidade + "'");
        }
    }
}