package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

/**
 * Regras de negócio relacionadas com {@link com.tcc.gelato.model.M_Usuario.Cargo}s
 * Também incluí casos relacionados com {@link HttpSession} para não conflitar com casos onde o cargo é VISITANTE
 */
@Service
public class S_Cargo {

    /**
     * Confere se uma sessão possuí usuário
     */
    public M_Usuario extrairUsuarioDeSessao(HttpSession session) {
        return (M_Usuario) session.getAttribute("usuario");
    }

    /**
     * Confere se um {@link M_Usuario} é VISITANTE
     */
    public boolean validarVisitante(M_Usuario m_usuario){
        return m_usuario == null;
    }

    /**
     * Confere se um {@link M_Usuario} é {@link com.tcc.gelato.model.M_Usuario.Cargo#CLIENTE}
     */
    public boolean validarCliente(M_Usuario m_usuario) {
        return m_usuario.getCargo() == M_Usuario.Cargo.CLIENTE;
    }
}
