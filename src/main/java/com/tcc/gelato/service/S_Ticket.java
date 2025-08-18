package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.controller.C_Ticket;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.repository.R_Compra;
import com.tcc.gelato.repository.produto.R_Ticket;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Aplicação de regras de negócio relacionados ao {@link M_Ticket}
 */
@Service
public class S_Ticket {

    private final R_Ticket r_ticket;
    private final R_Compra r_compra;

    public S_Ticket(R_Ticket r_ticket, R_Compra r_compra) {
        this.r_ticket = r_ticket;
        this.r_compra = r_compra;
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
     * @param m_usuario {@link M_Usuario} que deve ser {@link M_Usuario.Cargo#CLIENTE}
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

    /**
     * Valida um ticket para ser alterado
     * @param m_ticket {@link M_Ticket}
     * @return validade do ticket para alterações de seus conteúdos.
     */
    public boolean validarTicketParaAlterarConteudos(M_Ticket m_ticket) {
        return m_ticket.getStatus()==M_Ticket.StatusCompra.CARRINHO;
    }

    /**
     * Apaga todos os {@link M_Ticket}s em status {@link M_Ticket.StatusCompra#CARRINHO}
     */
    @Transactional
    public void apagarTicketsDescartaveis() {
        List<M_Ticket> descartaveis = r_ticket.selecionarTicketsDescartaveis();
        descartaveis.forEach(this::apagarTicket);
    }

    public void apagarTicket(M_Ticket ticket) {
        List<M_Compra> m_compras = r_ticket.getComprasDeTicket(ticket.getId());
        r_compra.deleteAll(m_compras);
        r_ticket.delete(ticket);
    }

    /**
     * Pega as compras de um ticket
     * @param m_ticket {@link M_Ticket
     * @return {@link M_Compra}s no carrinho
     */
    public List<M_Compra> getComprasDeTicket(M_Ticket m_ticket) {
        return r_ticket.getComprasDeTicket(m_ticket.getId());
    }

    /**
     * Confere os parâmetros da função {@link C_Ticket#definirHorarioRetirada(HttpSession, String)}
     * @param str_horario
     * @return Resposta de falha se houver
     */
    public M_RespostaTexto validarParamDefinirHorarioRetirada(String str_horario) {
        LocalDateTime horario;
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        try {
            horario = LocalDateTime.ofEpochSecond(Long.parseLong(str_horario),0, ZoneOffset.of("+3"));
        } catch (Exception e) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Horário inválido.");
            return m_respostaTexto;
        }
        if (horario.isBefore(LocalDateTime.now())) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Retirada não pode ser no passado.");
            return m_respostaTexto;
        }
        if (horario.isBefore(LocalDateTime.now().plusMinutes(30))) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Retirada deve ser no mínimo daqui à 30 minutos.");
            return m_respostaTexto;
        }
        m_respostaTexto.setSucesso(true);
        return m_respostaTexto;
    }

    /**
     * Define o horário de retirada de um ticket
     * @param m_ticket
     * @param horario
     */
    public M_Ticket setHorarioRetirada(M_Ticket m_ticket, LocalDateTime horario) {
        m_ticket.setHorario_retirada(horario);
        return r_ticket.save(m_ticket);
    }
}
