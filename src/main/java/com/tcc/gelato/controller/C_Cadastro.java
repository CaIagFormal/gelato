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
     * @param endereco Endereço do cliente
     * @param email E-mail do cliente
     * @param data_nasc Data de nascimento do cliente
     */
    @PostMapping(path="/cadastro")
    public String cadastrarCliente(
            @RequestParam("nome") String nome,
            @RequestParam("senha") String senha,
            @RequestParam("conf_senha") String conf_senha,
            @RequestParam("endereco") String endereco,
            @RequestParam("email") String email,
            @RequestParam("data_nasc") String data_nasc) {
        if (!s_cadastro.validarCadastroCliente(nome, senha, conf_senha, endereco, email, data_nasc)) {
            return "redirect:/cadastro";
        }

       if (s_cadastro.criarCadastroCliente(nome, senha, endereco, email, data_nasc)==null) {
           return "redirect:/cadastro";
       }
        return "redirect:/login";
    }
}
