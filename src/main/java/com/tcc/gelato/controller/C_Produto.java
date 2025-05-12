package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.service.S_Produto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class C_Produto {

    private final S_Produto s_produto;

    public C_Produto(S_Produto s_produto) {
        this.s_produto = s_produto;
    }

    /**
     *
     * @param id_produto ID do produto na URL
     * @param session Sessão usada para conferir {@link M_Usuario} válido
     * @return Página do produto solicitado
     */
    @GetMapping(path="/produto/{produto}")
    public String getProduto(@PathVariable("produto") Long id_produto, HttpSession session, Model model) {
        M_Usuario m_usuario = (M_Usuario) session.getAttribute("usuario");
        if (m_usuario==null) {
            return "redirect:/";
        }

        M_Produto m_produto = s_produto.getProdutoById(id_produto);

        model.addAttribute("produto",m_produto);

        return "cliente/produto";
    }
}
