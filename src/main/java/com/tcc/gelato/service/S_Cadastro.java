package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.repository.R_Usuario;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aplicação de regras de negócio do {@link com.tcc.gelato.controller.C_Cadastro}
 */
@Service
public class S_Cadastro {

    private final R_Usuario r_usuario;

    public S_Cadastro(R_Usuario r_usuario) {
        this.r_usuario = r_usuario;
    }

    /**
     * valida o cadastro do cliente
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param conf_senha Confirmação da senha do cliente
     * @param email E-mail do cliente
     * @return se é valido
     */
    public boolean validarCadastroCliente(String nome, String senha, String conf_senha, String email) {
        boolean validade = !nome.trim().isBlank() &&
                !senha.trim().isBlank() &&
                !conf_senha.trim().isBlank() &&
                !email.trim().isBlank();

        if (!validade) return false;

        return senha.equals(conf_senha);
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
}
