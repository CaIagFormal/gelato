package com.tcc.gelato.model.servidor;

public class M_Resposta {

    public M_Resposta() {
    }

    private String mensagem;
    private boolean sucesso;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
}
