package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class C_Inicio {

    /**
     * Não há uma tela mapeada para {@code "/"} afins de organização.
     * Cliente/Sem sessão: {@link C_Catalogo#getCatalogo(HttpSession, Model)}
     * @return Tela correspondente a sessão
     */
    @GetMapping(path="/")
    public String redirecionar(HttpSession session){
        //M_Usuario m_usuario = (M_Usuario) session.getAttribute("usuario");
        return "redirect:/catalogo";
    }
}
