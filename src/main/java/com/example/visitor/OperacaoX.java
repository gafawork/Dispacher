package com.example.visitor;

public class OperacaoX extends Operacao {
    public OperacaoX() {
        super(TipoOperacao.OPERACAO_X);
    }

    @Override
    public void aceitar(OperacaoVisitor visitor) {
        visitor.visitar(this);
    }
}
