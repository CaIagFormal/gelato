package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.controller.C_Login;
import com.tcc.gelato.repository.R_Usuario;
import org.springframework.stereotype.Service;

/**
 * Aplicação de regras de negócio do {@link C_Login}
 */
@Service
public class S_Login {

    private final R_Usuario r_usuario;

    public S_Login(R_Usuario r_usuario) {
        this.r_usuario = r_usuario;
    }

    /**
     * Comunica com o repositório para obter um cadastro a partir dos parâmetros
     * @param nome Nome ou E-mail do usuário
     * @param senha Senha do usuário
     * @return {@link M_Usuario} correspondente
     */
    public M_Usuario loginUsuario(String nome, String senha) {
        return r_usuario.getUsuarioByNomeOrEmailAndSenha(nome,senha);
    }
}
