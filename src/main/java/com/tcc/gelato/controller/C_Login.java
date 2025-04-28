package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.service.S_Login;
import jakarta.servlet.http.HttpSession;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class C_Login {

    private final S_Login s_login;

    public C_Login(S_Login s_login) { this.s_login = s_login; }

    /**
     * Essa tela atua para todos os atores
     * @return Tela de login ou redireciona para {@link com.tcc.gelato.controller.C_Inicio#redirecionar(HttpSession)} se já estiver logado
     */
    @GetMapping(path="/login")
    public String getLogin(HttpSession session){
        if (session.getAttribute("usuario")!=null) {
            return "redirect:/";
        }
        return "login";
    }

    /**
     * Faz login do usuário se os dados forem fornecidos corretamente
     * @param nome Nome ou e-mail do usuário
     * @param senha Senha do usuário
     */
    @PostMapping(path = "/login")
    public String loginUsuario(@RequestParam String nome,
                               @RequestParam String senha,
                               HttpSession session) {
        M_Usuario m_usuario = s_login.loginUsuario(nome, senha);
        if (m_usuario==null) {
            return "redirect:/login";
        }
        session.setAttribute("usuario",m_usuario);
        return "redirect:/inicio";
    }
}
