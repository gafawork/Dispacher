package com.example.visitor;

public abstract class Operacao {
    private final TipoOperacao tipo;

    protected Operacao(TipoOperacao tipo) {
        this.tipo = tipo;
    }

    public TipoOperacao getTipo() {
        return tipo;
    }

    public abstract void aceitar(OperacaoVisitor visitor);
}
