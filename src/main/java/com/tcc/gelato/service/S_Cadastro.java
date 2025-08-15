package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.repository.R_Usuario;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aplicação de regras de negócio do {@link com.tcc.gelato.controller.C_Cadastro}
 */
@Service
public class S_Cadastro {

    private final R_Usuario r_usuario;
    private final Pattern senha_valida;

    public S_Cadastro(R_Usuario r_usuario) {
        this.r_usuario = r_usuario;
        senha_valida = Pattern.compile("(?=.*[a-z]+)(?=.*[A-Z]+)(?=.*[0-9]+)(?=.*[!-\\/:-@\\[-`{-~]+)");
    }

    /**
     * valida o cadastro do cliente
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param conf_senha Confirmação da senha do cliente
     * @param email E-mail do cliente
     * @return se é valido
     */
    public M_RespostaTexto validarCadastroCliente(String nome, String senha, String conf_senha, String email) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        m_respostaTexto.setMensagem("");

        if (nome.trim().isBlank()) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+"Nome está vazio<br>");
            m_respostaTexto.setSucesso(false);
        }

        if (email.trim().isBlank()) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+"E-mail está vazio<br>");
            m_respostaTexto.setSucesso(false);
        }

        if (conf_senha.trim().isBlank()) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+"Confirmação da senha está vazia<br>");
            m_respostaTexto.setSucesso(false);
        }

        if (senha.trim().isBlank()) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+"Senha está vazia<br>");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        if (senha.length()<8) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+"Senha tem menos de 8 dígitos<br>");
            m_respostaTexto.setSucesso(false);
        }

        Matcher matcher = senha_valida.matcher(senha);
        if (!matcher.find()) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+"Senha não possui um dos requerimentos<br>");
            m_respostaTexto.setSucesso(false);
        }

        if (!senha.equals(conf_senha)) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+"Confirmação de senha não é o mesmo que a senha;");
            m_respostaTexto.setSucesso(false);
        }

        return m_respostaTexto;
    }


    /**
     * Cadastra uma conta para um cliente
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param email E-mail do cliente
     */
    public M_Usuario criarCadastroCliente(String nome, String senha, String email) {
        M_Usuario m_usuario = new M_Usuario();

        m_usuario.setNome(nome);
        m_usuario.setCargo(M_Usuario.Cargo.CLIENTE);
        m_usuario.setSenha(senha);
        m_usuario.setEmail(email);

        try {
            return r_usuario.save(m_usuario);
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Procura por um usuário baseado em seu nome ou e-mail
     * @param nome nome ou e-mail
     * @return {@link M_Usuario}
     */
    public M_Usuario getUsuarioByNomeOrEmail(String nome) {
        return r_usuario.getUsuarioByNomeOrEmail(nome);
    }
}
