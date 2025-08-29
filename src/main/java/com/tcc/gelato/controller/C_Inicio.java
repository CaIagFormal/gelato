package com.tcc.gelato.controller;

import com.tcc.gelato.model.servidor.M_RespostaObjeto;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.S_AcessoViaUrl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class C_Inicio {

    private final ApplicationContext context;
    private final S_AcessoViaUrl s_acessoViaUrl;

    public C_Inicio(ApplicationContext context, S_AcessoViaUrl sAcessoViaUrl) {
        this.context = context;
        s_acessoViaUrl = sAcessoViaUrl;
    }

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

    /**
     * Eventos que rodam diáriamente no começo do dia ou quando o programa é iniciado
     */
    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0 * * MON-SAT")
    public void diario() {
        context.getBean(C_Ticket.class).apagarTicketsVazios();
        s_acessoViaUrl.apagarAcessosInvalidos();
    }
}
