package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Setor;
import com.tcc.gelato.repository.produto.R_Setor;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class C_Inicio {

    private final R_Setor r_setor;

    public C_Inicio(R_Setor r_setor) {
        this.r_setor = r_setor;
    }

    /**
     * Não há uma tela mapeada para {@code "/"} afins de organização.
     * Sem sessão passa para: {@link com.tcc.gelato.controller.C_Cadastro#getCadastroCliente(HttpSession)}
     * Com sessão passa para: {@link #getInicio(HttpSession, Model)}
     * @return Tela correspondente a sessão
     */
    @GetMapping(path="/")
    public String redirecionar(HttpSession session){
        M_Usuario m_usuario = (M_Usuario) session.getAttribute("usuario");
        if (m_usuario==null) {
            return "redirect:/cadastro";
        }
        return "redirect:/inicio";
    }

    /**
     *
     * @return Página de início ou redirecionamento para {@link #redirecionar(HttpSession)}
     */
    @GetMapping(path="/inicio")
    public String getInicio(HttpSession session, Model model) {
        M_Usuario m_usuario = (M_Usuario) session.getAttribute("usuario");
        if (m_usuario==null) {
            return "redirect:/";
        }
        model.addAttribute("usuario",m_usuario);
        model.addAttribute("setores",r_setor.findAll());

        model.addAttribute("qtd_itens_carrinho",session.getAttribute("qtd_itens_carrinho"));
        return "cliente/inicio";
    }
}
