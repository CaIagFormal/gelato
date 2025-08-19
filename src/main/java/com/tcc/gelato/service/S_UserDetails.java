package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.repository.R_Usuario;
import com.tcc.gelato.model.servidor.M_UserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço para lidar com o cadastro do usuário no sistema por security
 */
@Service
public class S_UserDetails implements UserDetailsService {

    private final R_Usuario r_usuario;

    public S_UserDetails(R_Usuario r_usuario) {
        this.r_usuario = r_usuario;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        M_Usuario m_usuario = r_usuario.getUsuarioByNomeOrEmail(username);
        if (m_usuario==null) {
            throw new UsernameNotFoundException(username+" não foi encontrado no banco de dados");
        }
        return new M_UserDetails(m_usuario);
    }
}
