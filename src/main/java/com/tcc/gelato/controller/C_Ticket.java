package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.S_Cargo;
import com.tcc.gelato.service.S_Compra;
import com.tcc.gelato.service.S_Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Para funcionalidades regidas ao {@link M_Ticket}
 */
@Controller
public class C_Ticket {
    private final S_Ticket s_ticket;
    private final S_Cargo s_cargo;
    private final S_Compra s_compra;

    public C_Ticket(S_Ticket s_ticket, S_Cargo s_cargo, S_Compra s_compra) {
        this.s_ticket = s_ticket;
        this.s_cargo = s_cargo;
        this.s_compra = s_compra;
    }

    /**
     * Eventos que rodam diáriamente no começo do dia ou quando o programa é iniciado
     */
    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0 * * MON-SAT")
    public void diario() {
        this.apagarTicketsVazios();
    }



    /**
     * Confira {@link S_Ticket#apagarTicketsDescartaveis()}
     */
    public void apagarTicketsVazios() {
        this.s_ticket.apagarTicketsDescartaveis();
    }

    /**
     * @param session Redireciona para {@link C_Inicio#redirecionar(HttpSession)} caso não haja um {@link M_Usuario} vinculado
     * @return Tela de carrinho
     */
    @GetMapping(path="/carrinho")
    public String getCarrinho(HttpSession session, Model model) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);
        if (!s_cargo.validarCliente(m_usuario)) {
            return "redirect:/";
        }

        s_cargo.session_to_model_navbar(model,session);

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
        model.addAttribute("ticket",m_ticket);

        List<M_Compra> m_compras = s_ticket.getComprasDeTicket(m_ticket);
        model.addAttribute("carrinho",m_compras);
        model.addAttribute("total",s_compra.getPrecoTotalDeCompras(m_compras));
        return "cliente/carrinho";
    }

    /**
     * Define o horário de retirada de um ticket
     * @param session
     * @param str_horario
     * @return
     */
    @ResponseBody
    @PostMapping(path="/definir_horario_retirada_ticket")
    public M_RespostaTexto definirHorarioRetirada(HttpSession session, @RequestParam("horario") String str_horario) {
        M_RespostaTexto m_respostaTexto;

        m_respostaTexto = s_ticket.validarParamDefinirHorarioRetirada(str_horario);
        if (!m_respostaTexto.isSucesso()) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Horário inválido");
            return m_respostaTexto;
        }

        LocalDateTime horario = LocalDateTime.ofEpochSecond(Long.parseLong(str_horario),0, ZoneOffset.of("+3"));

        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);
        if (!s_cargo.validarCliente(m_usuario)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não está cadastrado como cliente.");
            return m_respostaTexto;
        }

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
        if (m_ticket.getStatus()!= M_Ticket.StatusCompra.CARRINHO) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Pedido não está no carrinho");
            return m_respostaTexto;
        }
        if (s_ticket.setHorarioRetirada(m_ticket,horario)==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Falha ao salvar no banco de dados.");
            return m_respostaTexto;
        }
        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("Horário de retirada do pedido com ticket "+m_ticket.getTicket()+" definido para "+m_ticket.getHorario_retirada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        return m_respostaTexto;
    }
}
