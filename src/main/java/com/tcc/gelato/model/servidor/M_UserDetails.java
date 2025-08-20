package com.tcc.gelato.model.servidor;

import com.tcc.gelato.model.M_Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class M_UserDetails implements UserDetails {

    private final M_Usuario usuario;
    private final LocalDateTime registro;

    public M_UserDetails(M_Usuario usuario) {
        this.usuario = usuario;
        this.registro = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (usuario.getCargo()) {
            case CLIENTE -> {
                return Collections.singleton(new SimpleGrantedAuthority("CLIENTE"));
            }
            case VENDEDOR -> {
                return Collections.singleton(new SimpleGrantedAuthority("VENDEDOR"));
            }
        }
        return null;
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return LocalDateTime.now().isBefore(registro.plusDays(1L));
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

    public M_Usuario getUsuario() {
        return usuario;
    }
}
