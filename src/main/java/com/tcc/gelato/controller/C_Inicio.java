package com.tcc.gelato.controller;

import com.tcc.gelato.model.servidor.M_RespostaObjeto;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
