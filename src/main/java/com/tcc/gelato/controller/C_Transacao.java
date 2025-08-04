package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.service.S_Cargo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Funcionalidades relacionadas a classe {@link com.tcc.gelato.model.M_Transacao}
 */
@Controller
public class C_Transacao {

    private final S_Cargo s_cargo;

    public C_Transacao(S_Cargo s_cargo) {
        this.s_cargo = s_cargo;
    }

    @GetMapping(path = "gerir_saldo")
    public String getGerirSaldo(HttpSession session) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        if (!s_cargo.validarVendedor(m_usuario)) {
            return "redirect:/";
        }

        return "vendedor/gerir_saldo";
    }
}
