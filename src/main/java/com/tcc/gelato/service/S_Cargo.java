package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.servidor.M_NavbarCliente;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;

/**
 * Regras de negócio relacionadas com {@link M_Usuario.Cargo}s
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
     * Confere se um {@link M_Usuario} é {@link M_Usuario.Cargo#CLIENTE}
     */
    public boolean validarCliente(M_Usuario m_usuario) {
        if (m_usuario==null) {
            return false;
        }
        return m_usuario.getCargo() == M_Usuario.Cargo.CLIENTE;
    }

    /**
     * Confere se um {@link M_Usuario} é {@link M_Usuario.Cargo#VENDEDOR}
     */
    public boolean validarVendedor(M_Usuario m_Usuario) {
        if (m_Usuario==null) {
            return false;
        }
        return m_Usuario.getCargo() == M_Usuario.Cargo.VENDEDOR;
    }

    /**
     * Prepara o Model do Thymeleaf transferindo do session
     * @param model Model
     * @param session Sessão do usuário
     */
    public void session_to_model_navbar(Model model, HttpSession session) {
        model.addAttribute("m_navbar",session.getAttribute("m_navbar"));
    }

    /**
     * Define a quantidade de compras da sessão em um CLIENTE indiretamente
     * @param session Sessão do cliente
     * @param qtd quantidade
     */
    public void navClienteSetQtdCompras(HttpSession session, Integer qtd) {
        M_NavbarCliente m_navbarCliente = (M_NavbarCliente) session.getAttribute("m_navbar");
        m_navbarCliente.setQuantidade_compras(qtd);
        session.setAttribute("m_navbar",m_navbarCliente);
    }

    /**
     * Define o saldo da sessão indiretamente
     * @param session Sessão do cliente
     * @param saldo Saldo do cliente
     */
    public void navClienteSetSaldo(HttpSession session, BigDecimal saldo) {
        M_NavbarCliente m_navbarCliente = (M_NavbarCliente) session.getAttribute("m_navbar");
        m_navbarCliente.setSaldo(saldo);
        session.setAttribute("m_navbar",m_navbarCliente);
    }
}
