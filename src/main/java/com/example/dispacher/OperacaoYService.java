package com.example.dispacher;

import org.springframework.stereotype.Service;

@Service
public class OperacaoYService implements OperacaoService, Confirmavel {

    @Override
    public TipoOperacao getTipo() { return TipoOperacao.OPERACAO_Y; }

    @Override
    public void confirmar(Operacao operacao) { /* ... */ }
}
