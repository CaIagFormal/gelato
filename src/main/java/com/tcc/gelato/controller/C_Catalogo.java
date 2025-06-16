package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.repository.produto.R_Produto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class C_Catalogo {

    private final R_Produto r_produto;

    public C_Catalogo(R_Produto r_produto) {
        this.r_produto = r_produto;
    }
    /**
     *
     * @return Página de catálogo
     */
    @GetMapping(path="/catalogo")
    public String getCatalogo(HttpSession session, Model model) {
        M_Usuario m_usuario = (M_Usuario) session.getAttribute("usuario");

        model.addAttribute("usuario",m_usuario);
        model.addAttribute("produtos",r_produto.findAll());

        if (m_usuario!=null) {
            model.addAttribute("qtd_itens_carrinho",session.getAttribute("qtd_itens_carrinho"));
        }
        return "cliente/catalogo";

    }
}
