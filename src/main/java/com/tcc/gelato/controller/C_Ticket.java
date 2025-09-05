package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Para funcionalidades regidas ao {@link M_Ticket},
 * se quer ver da página do vendedor de pedidos, acesse {@link C_Pedidos}
 */
@Controller
public class C_Ticket {
    private final S_Ticket s_ticket;
    private final S_Cargo s_cargo;
    private final S_Compra s_compra;
    private final S_Estoque s_estoque;
    private final S_Transacao s_transacao;

    public C_Ticket(S_Ticket s_ticket, S_Cargo s_cargo, S_Compra s_compra, S_Estoque s_estoque, S_Transacao s_transacao) {
        this.s_ticket = s_ticket;
        this.s_cargo = s_cargo;
        this.s_compra = s_compra;
        this.s_estoque = s_estoque;
        this.s_transacao = s_transacao;
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
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();

        s_cargo.session_to_model_navbar(model,session);

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
        model.addAttribute("ticket",m_ticket);

        List<M_Compra> m_compras = s_ticket.getComprasDeTicket(m_ticket);
        model.addAttribute("carrinho",m_compras);
        model.addAttribute("total",s_compra.getPrecoTotalDeCompras(m_compras));
        model.addAttribute("alterar_monetario",s_ticket.validarTicketParaAlterarMonetario(m_ticket));
        model.addAttribute("alterar_outros",s_ticket.validarTicketParaAlterarOutros(m_ticket));
        return "cliente/carrinho";
    }

    /**
     * Define o horário de retirada de um ticket
     * @return
     */
    @ResponseBody
    @PostMapping(path="/definir_horario_retirada_ticket")
    public M_RespostaTexto definirHorarioRetirada(@RequestParam("horario") String str_horario) {
        M_RespostaTexto m_respostaTexto;

        m_respostaTexto = s_ticket.validarParamDefinirHorarioRetirada(str_horario);
        if (!m_respostaTexto.isSucesso()) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Horário inválido");
            return m_respostaTexto;
        }

        LocalDateTime horario = LocalDateTime.ofEpochSecond(Long.parseLong(str_horario),0, ZoneOffset.of("-3"));

        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
        if (!s_ticket.validarTicketParaAlterarOutros(m_ticket)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não é possível alterar o horário de retirada após o pedido ser recebido.");
            return m_respostaTexto;
        }
        if (s_ticket.setHorarioRetirada(m_ticket,horario)==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Falha ao salvar no banco de dados.");
            return m_respostaTexto;
        }
        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("Horário de retirada do pedido com ticket '"+m_ticket.getTicket()+"' definido para "+m_ticket.getHorario_retirada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        return m_respostaTexto;
    }

    /**
     * Encaminha um ticket do cliente
     * @return Resposta de sucesso ou falha
     */
    @ResponseBody
    @PostMapping(path = "/encaminhar_pedido")
    @Transactional
    public M_RespostaTexto encaminharPedido(HttpSession session) {
        M_RespostaTexto m_respostaTexto;

        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();
        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);

        if (m_ticket.getStatus()!=M_Ticket.StatusCompra.CARRINHO) {
            m_respostaTexto = new M_RespostaTexto();
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Seu pedido não está mais no carrinho.");
            return m_respostaTexto;
        }

        BigDecimal total = s_compra.getPrecoTotalDeCompras(s_ticket.getComprasDeTicket(m_ticket));

        if (s_transacao.getSaldoDeCliente(m_usuario).compareTo(total)<0) {
            m_respostaTexto = new M_RespostaTexto();
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Você não possuí saldo suficiente, consulte o vendedor para pagar seu pedido de antemão.");
            return m_respostaTexto;
        }

        m_respostaTexto = s_ticket.validarHorarioDeRetirada(m_ticket.getHorario_retirada());
        if (!m_respostaTexto.isSucesso()) {
            return m_respostaTexto;
        }

        m_ticket = s_ticket.encaminharTicket(m_ticket,s_transacao,total);
        if (m_ticket==null) {
            m_respostaTexto.setMensagem("Erro comunicando com o banco de dados.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        s_ticket.desativarProdutosEmTicketComEstoqueInsuficiente(m_ticket,s_compra,s_estoque);

        s_cargo.navClienteSetSaldo(session,s_transacao.getSaldoDeCliente(m_usuario));

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("Encaminhou pedido com ticket '"+m_ticket.getTicket()+"'.<br>" +
                "Poderá cancelar seu pedido até ele ser recebido.<br>" +
                "(O saldo se manterá no sistema Gelato)");
        return m_respostaTexto;
    }

    /**
     * Cancela um ticket pelo cliente
     * @return Resultados
     */
    @PostMapping("/cancelar_pedido")
    @ResponseBody
    public M_RespostaTexto cancelarPedido(HttpSession session) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();
        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);

        if (m_ticket.getStatus() != M_Ticket.StatusCompra.ENCAMINHADO) {
            m_respostaTexto.setMensagem("Seu pedido não pode ser cancelado agora.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        m_ticket = s_ticket.cancelarPedido(m_ticket,true);
        if (m_ticket==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não foi possível cancelar seu pedido devido a um erro no banco de dados.");
            return m_respostaTexto;
        }

        s_cargo.navClienteSetSaldo(session,s_transacao.getSaldoDeCliente(m_usuario));

        // Atualizar quantidade de itens no carrinho
        s_cargo.navClienteSetQtdCompras(session,s_compra.getQtdComprasDeTicket(m_ticket));

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("Seu pedido com ticket '"+m_ticket.getTicket()+"' foi cancelado, o saldo gasto está de volta na sua conta Gelato.");
        return m_respostaTexto;
    }

    @ResponseBody
    @PostMapping(path = "/observacao_ticket")
    public M_RespostaTexto definirObservacaoTicket(@RequestParam("texto") String texto) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
        if (!s_ticket.validarTicketParaAlterarOutros(m_ticket)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não é mais possível alterar a observação...");
            return m_respostaTexto;
        }

        if (!s_ticket.validarParamObservacaoTicket(texto)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Texto inválido...");
            return m_respostaTexto;
        }

        m_ticket = s_ticket.setObservacaoTicket(m_ticket,texto);
        if (m_ticket==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Algo ocorreu acessando o banco de dados");
            return m_respostaTexto;
        }

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("Observação salva com sucesso");
        return m_respostaTexto;
    }
}
