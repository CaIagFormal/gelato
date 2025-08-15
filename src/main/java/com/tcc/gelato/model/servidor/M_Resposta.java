package com.tcc.gelato.model.servidor;

/**
 * Base para Respostas do servidor à View
 */
public interface M_Resposta {
    Object getMensagem();
    void setMensagem(Object mensagem);
    Boolean isSucesso();
    void setSucesso(Boolean sucesso);
}
