package com.tcc.gelato.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class C_Login {
    /**
     * Essa tela atua para todos os atores
     * @return Tela de login
     */
    @GetMapping(path="/login")
    public String getLogin(){
        return "login";
    }
}
