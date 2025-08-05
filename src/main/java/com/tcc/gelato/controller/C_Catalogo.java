package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.service.S_Cargo;
import com.tcc.gelato.service.S_Produto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class C_Catalogo {

    private final S_Produto s_produto;
    private final S_Cargo s_cargo;

    public C_Catalogo(S_Produto s_produto,S_Cargo s_cargo) {
        this.s_produto = s_produto;
        this.s_cargo = s_cargo;
    }
    /**
     *
     * @return Página de catálogo
     */
    @GetMapping(path="/catalogo")
    public String getCatalogo(HttpSession session, Model model) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        model.addAttribute("usuario",m_usuario);
        model.addAttribute("produtos",s_produto.getProdutosDisponiveis());

        if (s_cargo.validarCliente(m_usuario)) {
            s_cargo.session_to_model_navbar(model,session);
        }
        return "catalogo";

    }
}
