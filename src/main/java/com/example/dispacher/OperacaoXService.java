package com.example.dispacher;

import org.springframework.stereotype.Service;

@Service
public class OperacaoXService implements OperacaoService, Executavel, PreExecutavel, Confirmavel {

    @Override
    public TipoOperacao getTipo() { return TipoOperacao.OPERACAO_X; }

    @Override
    public void executar(Operacao operacao) { /* ... */ }

    @Override
    public void preExecutar(Operacao operacao) { /* ... */ }

    @Override
    public void confirmar(Operacao operacao) { /* ... */ }
}

