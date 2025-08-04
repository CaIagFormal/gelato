package com.tcc.gelato.controller;

import com.tcc.gelato.service.S_Ticket;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

/**
 * Para funcionalidades regidas ao {@link com.tcc.gelato.model.produto.M_Ticket}
 */
@Controller
public class C_Ticket {
    private final S_Ticket s_ticket;

    public C_Ticket(S_Ticket s_ticket) {
        this.s_ticket = s_ticket;
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
}
