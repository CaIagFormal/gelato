package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.service.S_Cadastro;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class C_Cadastro {

    private final S_Cadastro s_cadastro;

    public C_Cadastro(S_Cadastro s_cadastro) {
        this.s_cadastro = s_cadastro;
    }

    /**
     * @return Tela de cadastro do cliente ou redireciona para {@link com.tcc.gelato.controller.C_Inicio#redirecionar(HttpSession)} se já estiver logado
     */
    @GetMapping(path="/cadastro")
    public String getCadastroCliente(HttpSession session){
        if (session.getAttribute("usuario")!=null) {
            return "redirect:/";
        }
        return "cliente/cadastro";
    }

    /**
     * Cadastra um usuário se foi fornecido com os dados corretamente.
     * Redireciona para {@link com.tcc.gelato.controller.C_Login#getLogin(HttpSession)}
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param conf_senha Confirmação da senha do cliente
     * @param email E-mail do cliente
     */
    @PostMapping(path="/cadastro")
    public String cadastrarCliente(
            @RequestParam("nome") String nome,
            @RequestParam("senha") String senha,
            @RequestParam("conf_senha") String conf_senha,
            @RequestParam("email") String email) {
        if (!s_cadastro.validarCadastroCliente(nome, senha, conf_senha, email)) {
            return "redirect:/cadastro";
        }

       if (s_cadastro.criarCadastroCliente(nome, senha, email)==null) {
           return "redirect:/cadastro";
       }
        return "redirect:/login";
    }

    /**
     * Performa logout da conta do usuário o inibindo de ultilizar os recursos até fazer login novamente
     * @param session Sessão do usuário incluindo conta
     * @return Redirecionamento padrão sem cadastro {@link com.tcc.gelato.controller.C_Inicio#redirecionar(HttpSession)}
     */
    @GetMapping(path="/logout")
    public String getLogout(HttpSession session){
        if (session.getAttribute("usuario")==null) {
            return "redirect:/";
        }

        session.removeAttribute("usuario");
        return "redirect:/";
    }
}
