package com.tcc.gelato.model.servidor;

import com.tcc.gelato.model.M_Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class M_UserDetails implements UserDetails {

    private M_Usuario m_usuario;

    public M_UserDetails(M_Usuario m_usuario) {
        this.m_usuario = m_usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (m_usuario.getCargo()) {
            case CLIENTE -> {
                return List.of(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority("CLIENTE"),
                        new SimpleGrantedAuthority("VISITANTE")});
            }
            case VENDEDOR -> {
                return List.of(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority("VENDEDOR"),
                        new SimpleGrantedAuthority("VISITANTE")});
            }
        }
        return Collections.singleton(new SimpleGrantedAuthority("VISITANTE"));
    }

    @Override
    public String getPassword() {
        return m_usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return m_usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
