package com.example.dispacher;

public class Operacao {
    private final TipoOperacao tipo;

    public Operacao(TipoOperacao tipo) {
        this.tipo = tipo;
    }

    public TipoOperacao getTipo() {
        return tipo;
    }

}
