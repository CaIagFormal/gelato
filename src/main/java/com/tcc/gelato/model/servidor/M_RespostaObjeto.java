package com.tcc.gelato.model.servidor;

public class M_RespostaObjeto implements M_Resposta {
    public M_RespostaObjeto() {

    }

    private Object mensagem;
    private Boolean sucesso;

    public Object getMensagem() {
        return mensagem;
    }

    public void setMensagem(Object mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean isSucesso() {
        return sucesso;

    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    @Override
    public String toString() {
        return mensagem.toString();
    }
}
