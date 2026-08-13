package com.example.dispacher;

public enum TipoOperacao {
    OPERACAO_X,
    OPERACAO_Y,
    OPERACAO_Z;

    private OperacaoService servico;

    public void associarServico(OperacaoService servico) {
        this.servico = servico;
    }

    public OperacaoService getServico() {
        return servico;
    }
}
