package com.tcc.gelato.controller;

import com.tcc.gelato.model.servidor.M_RespostaObjeto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller que lida com erros
 */
@Controller
public class C_Error implements ErrorController {

    @RequestMapping(path = "/error")
    @ResponseBody
    public M_RespostaObjeto erro(HttpServletRequest request) {
        M_RespostaObjeto m_respostaObjeto = new M_RespostaObjeto();
        m_respostaObjeto.setMensagem(request);
        m_respostaObjeto.setSucesso(false);
        return m_respostaObjeto;
    }
}
