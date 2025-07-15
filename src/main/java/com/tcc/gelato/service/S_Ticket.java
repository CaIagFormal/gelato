package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.repository.produto.R_Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * Aplicação de regras de negócio relacionados ao {@link com.tcc.gelato.model.produto.M_Ticket}
 */
@Service
public class S_Ticket {

    private final R_Ticket r_ticket;

    public S_Ticket(R_Ticket r_ticket) {
        this.r_ticket = r_ticket;
    }

    /**
     * Gera um {@link M_Ticket} baseado em um {@link M_Usuario}
     * @param m_usuario {@link M_Usuario}
     * @return {@link M_Ticket}
     */
    public M_Ticket gerarTicketParaUsuario(M_Usuario m_usuario) {
        M_Ticket m_ticket = new M_Ticket();
        m_ticket.setUsuario(m_usuario);
        m_ticket.setTicket(gerarNumeroDeTicket());
        m_ticket.setHorario_fornecido(LocalDateTime.now());
        m_ticket.setStatus(M_Ticket.StatusCompra.CARRINHO);
        m_ticket.setPagamento(false);
        try {
            return r_ticket.save(m_ticket);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gera número para um {@link M_Ticket}
     * @return {@link String} do ticket
     */
    public String gerarNumeroDeTicket() {
        Random random = new Random();
        StringBuilder ticket;
        Character c;
        do {
            ticket = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                do {
                    c = (char) (random.nextInt()%77 + '0'); // 42 -> 'z'-'0'
                } while (!Character.isDigit(c) && !Character.isAlphabetic(c));
                ticket.append(c);
            }
        } while (r_ticket.getTicketAtivoByString(ticket.toString()).isPresent());

        return ticket.toString();
    }

    /**
     * Obtêm o ticket de um {@link M_Usuario}
     * @param m_usuario {@link M_Usuario} que possuí (ou não) o ticket
     * @return {@link M_Ticket}
     */
    public Optional<M_Ticket> obterTicketValidoDeUsuario(M_Usuario m_usuario) {
        return r_ticket.getTicketAtivoDeUsuario(m_usuario.getId());
    }

    /**
     * Confere o ticket e gera se for necessário
     * @param m_usuario {@link M_Usuario} que deve ser {@link com.tcc.gelato.model.M_Usuario.Cargo#CLIENTE}
     * @return o ticket em si
     */
    public M_Ticket conferirTicketDeUsuario(M_Usuario m_usuario) {
        Optional<M_Ticket> m_ticket = obterTicketValidoDeUsuario(m_usuario);
        //noinspection OptionalIsPresent
        if (m_ticket.isPresent()) {
            return m_ticket.get();
        }
        return this.gerarTicketParaUsuario(m_usuario);

    }

}
