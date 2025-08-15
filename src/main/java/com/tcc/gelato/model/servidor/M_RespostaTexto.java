package com.tcc.gelato.model.servidor;

/**
 * Modelo responsável por lidar com respostas ajax para a View com texto
 */
public class M_RespostaTexto implements M_Resposta {

    public M_RespostaTexto() {
    }

    private String mensagem;
    private Boolean sucesso;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(Object mensagem) {
        this.mensagem = mensagem.toString();
    }

    public Boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    @Override
    public String toString() {
        return this.mensagem;
    }
}
