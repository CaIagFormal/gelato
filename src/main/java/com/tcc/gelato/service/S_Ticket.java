package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.controller.C_Ticket;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.model.view.M_ViewPedido;
import com.tcc.gelato.repository.R_Compra;
import com.tcc.gelato.repository.produto.R_Ticket;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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
    private Optional<M_Ticket> obterTicketValidoDeUsuario(M_Usuario m_usuario) {
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
     * Valida um ticket para ser alterado em maneiras que afetam seu valor monetário
     * @param m_ticket {@link M_Ticket}
     * @return validade do ticket para alterações de seus conteúdos.
     */
    public boolean validarTicketParaAlterarMonetario(M_Ticket m_ticket) {
        return m_ticket.getStatus()==M_Ticket.StatusCompra.CARRINHO;
    }

    /**
     * Valida um ticket para ser alterado em maneiras que NÃO afetam seu valor monetário
     * @param m_ticket {@link M_Ticket}
     */
    public boolean validarTicketParaAlterarOutros(M_Ticket m_ticket) {
        return m_ticket.getStatus() == M_Ticket.StatusCompra.CARRINHO || m_ticket.getStatus() == M_Ticket.StatusCompra.ENCAMINHADO;
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
     * Confere os parâmetros da função {@link C_Ticket#definirHorarioRetirada(String)}
     * @param str_horario String contendo um {@link LocalDateTime#ofEpochSecond(long, int, ZoneOffset)}
     * @return Resposta de falha se houver
     */
    public M_RespostaTexto validarParamDefinirHorarioRetirada(String str_horario) {
        LocalDateTime horario;
        try {
            horario = LocalDateTime.ofEpochSecond(Long.parseLong(str_horario),0, ZoneOffset.of("+3"));
        } catch (Exception e) {
            M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Horário inválido.");
            return m_respostaTexto;
        }
        return validarHorarioDeRetirada(horario);
    }

    public M_RespostaTexto validarHorarioDeRetirada(LocalDateTime horario) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        if (horario==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Horário de retirada não foi definido.");
            return m_respostaTexto;
        }
        if (horario.isBefore(LocalDateTime.now())) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Horário de retirada não pode ser no passado.");
            return m_respostaTexto;
        }
        if (horario.isBefore(LocalDateTime.now().plusMinutes(30))) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Horário de retirada deve ser no mínimo daqui à 30 minutos.");
            return m_respostaTexto;
        }
        m_respostaTexto.setSucesso(true);
        return m_respostaTexto;
    }

    /**
     * Define o horário de retirada de um ticket
     */
    public M_Ticket setHorarioRetirada(M_Ticket m_ticket, LocalDateTime horario) {
        m_ticket.setHorario_retirada(horario);
        return r_ticket.save(m_ticket);
    }

    /**
     * Altera de {@link M_Ticket.StatusCompra#CARRINHO} para {@link M_Ticket.StatusCompra#ENCAMINHADO}
     * @param m_ticket Ticket a ser encaminhado
     */
    public M_Ticket encaminharTicket(M_Ticket m_ticket, S_Transacao s_transacao,BigDecimal valor_total) {
        m_ticket.setPagamento(s_transacao.alterarSaldo(valor_total.negate(),null,m_ticket.getUsuario()));
        if (m_ticket.getPagamento()==null) {
            return null;
        }
        m_ticket.setStatus(M_Ticket.StatusCompra.ENCAMINHADO);

        return r_ticket.save(m_ticket);
    }


    /**
     * Cancela um pedido
     */
    public M_Ticket cancelarPedido(M_Ticket m_ticket) {
        m_ticket.getPagamento().setValida(false);
        m_ticket.setStatus(M_Ticket.StatusCompra.CANCELADO);
        return r_ticket.save(m_ticket);
    }

    /**
     * Confere os parâmetros do méto,do {@link C_Ticket#definirObservacaoTicket(String)}}
     * @param texto Texto para usar na observação
     */
    public boolean validarParamObservacaoTicket(String texto) {
        if (texto.length()>1023) return false;
        return true;
    }

    /**
     * Altera a observação de um ticket
     * @return Objeto do banco de daodos
     */
    public M_Ticket setObservacaoTicket(M_Ticket m_ticket, String texto) {
        m_ticket.setObservacao(texto);
        return r_ticket.save(m_ticket);
    }

    /**
     * Obtêm todos os pedidos válidos e os organiza por status
     * @return HashMap de 4 elementos, cada um representando um status (exceto por carrinho)
     */
    public HashMap<M_Ticket.StatusCompra,List<M_ViewPedido>> getPedidosPorStatus() {
        HashMap<M_Ticket.StatusCompra,List<M_ViewPedido>> status_pedidos = new HashMap<>();

        List<M_ViewPedido> pedidos = r_ticket.getPedidos();
        if (pedidos.isEmpty()) {
            return null;
        }

        M_Ticket.StatusCompra last_status = pedidos.get(0).getStatus();
        int last_index = 0;
        for (int i = 0; i<pedidos.size(); i++) {
            M_Ticket.StatusCompra pedido_status = pedidos.get(i).getStatus();
            if (last_status == pedido_status) {
                continue;
            }

            status_pedidos.put(
                    last_status,
                    pedidos.subList(last_index,i));

            last_index = i;
            last_status = pedido_status;
        }
        status_pedidos.put(
                pedidos.get(pedidos.size()-1).getStatus(),
                pedidos.subList(last_index,pedidos.size()));

        return status_pedidos;
    }
}
