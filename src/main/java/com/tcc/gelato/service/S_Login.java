package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.controller.C_Login;
import com.tcc.gelato.model.servidor.M_UserDetails;
import com.tcc.gelato.repository.R_Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ListeningSecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.net.Authenticator;

/**
 * Aplicação de regras de negócio do {@link C_Login}
 */
@Service
public class S_Login {

    private final AuthenticationManager authenticationManager;
    private final S_JWT s_jwt;
    private final SecurityContextRepository securityContextRepository;

    public S_Login(AuthenticationManager authenticationManager, S_JWT s_jwt) {
        this.authenticationManager = authenticationManager;
        this.s_jwt = s_jwt;
        this.securityContextRepository = new HttpSessionSecurityContextRepository();
    }

    /**
     * Comunica com o repositório para obter um cadastro a partir dos parâmetros
     * @param nome Nome ou E-mail do usuário
     * @param senha Senha do usuário
     * @return {@link M_UserDetails} correspondente
     */
    public M_UserDetails loginUsuario(String nome, String senha, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nome,senha));
        if (!authentication.isAuthenticated()) return null;
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        securityContextRepository.saveContext(securityContext, request, response);

        M_UserDetails m_userDetails = (M_UserDetails) authentication.getPrincipal();

        s_jwt.gerarToken(m_userDetails.getUsername());
        return m_userDetails;
    }
}
