package com.tcc.gelato.controller;

import com.tcc.gelato.service.S_Cadastro;
import org.springframework.stereotype.Controller;
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
     * Não há uma tela mapeada para {@code "/"} afins de organização.
     * Sem sessão passa para: {@link #getCadastroCliente()}
     * @return Tela correspondente a sessão
     */
    @GetMapping(path="/")
    public String redirecionar(){
        return "redirect:/cadastro";
    }

    /**
     * @return Tela de cadastro do cliente
     */
    @GetMapping(path="/cadastro")
    public String getCadastroCliente(){
        return "cliente/cadastro";
    }

    /**
     * Cadastra um usuário se foi fornecido com os dados corretamente.
     * Redireciona para {@link com.tcc.gelato.controller.C_Login#getLogin()}
     */
    @PostMapping(path="/cadastro")
    public String cadastrarCliente(
            @RequestParam("nome") String nome,
            @RequestParam("senha") String senha,
            @RequestParam("conf_senha") String conf_senha,
            @RequestParam("endereco") String endereco,
            @RequestParam("email") String email) {
        if (!s_cadastro.validarCadastroCliente(nome, senha, conf_senha, endereco, email)) {
            return "cliente/cadastro";
        }

        return "redirect:/login";
    }
}
