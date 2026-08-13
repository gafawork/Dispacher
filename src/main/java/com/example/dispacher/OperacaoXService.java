package com.example.dispacher;

import org.springframework.stereotype.Service;

@Service
public class OperacaoXService implements OperacaoService, Executavel, PreExecutavel, Confirmavel {

    @Override
    public TipoOperacao getTipo() { return TipoOperacao.OPERACAO_X; }

    @Override
    public void executar(Operacao operacao) {
        System.out.println("Executar");
    }

    @Override
    public void preExecutar(Operacao operacao) {
        System.out.println("Pre Executar");
    }

    @Override
    public void confirmar(Operacao operacao) {
        System.out.println("Confirmar");
    }
}

