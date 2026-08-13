package com.example.visitor;

public class OperacaoZ extends Operacao {
    public OperacaoZ() {
        super(TipoOperacao.OPERACAO_Z);
    }

    @Override
    public void aceitar(OperacaoVisitor visitor) {
        visitor.visitar(this);
    }
}
