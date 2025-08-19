package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.controller.C_Login;
import com.tcc.gelato.model.servidor.M_UserDetails;
import com.tcc.gelato.repository.R_Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.Authenticator;

/**
 * Aplicação de regras de negócio do {@link C_Login}
 */
@Service
public class S_Login {

    private final R_Usuario r_usuario;
    private final AuthenticationManager authenticationManager;
    private final S_JWT s_jwt;

    public S_Login(R_Usuario r_usuario, AuthenticationManager authenticationManager, S_JWT sJwt) {
        this.r_usuario = r_usuario;
        this.authenticationManager = authenticationManager;
        s_jwt = sJwt;
    }

    /**
     * Comunica com o repositório para obter um cadastro a partir dos parâmetros
     * @param nome Nome ou E-mail do usuário
     * @param senha Senha do usuário
     * @return {@link M_Usuario} correspondente
     */
    public String loginUsuario(String nome, String senha) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nome,senha));
        if (!authentication.isAuthenticated()) return null;

        M_UserDetails m_userDetails = (M_UserDetails) authentication.getPrincipal();

        return s_jwt.gerarToken(m_userDetails.getUsername());
    }
}
