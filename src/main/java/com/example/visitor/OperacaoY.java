package com.example.visitor;

public class OperacaoY extends Operacao {
    public OperacaoY() {
        super(TipoOperacao.OPERACAO_Y);
    }

    @Override
    public void aceitar(OperacaoVisitor visitor) {
        visitor.visitar(this);
    }
}
