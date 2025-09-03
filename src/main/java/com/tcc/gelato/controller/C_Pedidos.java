package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_RespostaObjeto;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.model.view.M_ViewPedido;
import com.tcc.gelato.service.S_Cadastro;
import com.tcc.gelato.service.S_Cargo;
import com.tcc.gelato.service.S_Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Controller regente a tela do vendedor de pedidos
 */
@Controller
public class C_Pedidos {

    private final S_Ticket s_ticket;
    private final S_Cargo s_cargo;

    private final S_Cadastro s_cadastro;

    private final TemplateEngine templateEngine;

    public C_Pedidos(S_Ticket s_ticket, S_Cargo s_cargo, S_Cadastro sCadastro, TemplateEngine templateEngine) {
        this.s_ticket = s_ticket;
        this.s_cargo = s_cargo;
        s_cadastro = sCadastro;
        this.templateEngine = templateEngine;
    }

    /**
     * Página de pedidos
     */
    @GetMapping("/pedidos")
    public String getPedidos(HttpSession session, Model model) {
        HashMap<M_Ticket.StatusCompra,List<M_ViewPedido>> pedidos = s_ticket.getPedidosPorStatus();

        s_cargo.session_to_model_navbar(model,session);

        if (pedidos==null) {
            model.addAttribute("pedidos_ativos",false);
            return "vendedor/pedidos";
        }

        model.addAttribute("pedidos_ativos",true);
        model.addAttribute("pedidos_encaminhados",pedidos.get(M_Ticket.StatusCompra.ENCAMINHADO));
        model.addAttribute("pedidos_recebidos",pedidos.get(M_Ticket.StatusCompra.RECEBIDO));
        model.addAttribute("pedidos_entregues",pedidos.get(M_Ticket.StatusCompra.ENTREGUE));
        model.addAttribute("pedidos_cancelados",pedidos.get(M_Ticket.StatusCompra.CANCELADO));


        return "vendedor/pedidos";
    }

    @PostMapping(path = "/obter_contato_cliente")
    @ResponseBody
    M_RespostaTexto obterContatoCliente(@RequestParam("nome") String nome) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        if (!s_cadastro.validarGetUsuarioByNomeOrEmail(nome)) {
            m_respostaTexto.setMensagem("O usuário solicitado é inválido.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }
        M_Usuario m_usuario = s_cadastro.getUsuarioByNomeOrEmail(nome);
        if (m_usuario==null) {
            m_respostaTexto.setMensagem("O usuário solicitado não foi encontrado.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        // Processar página
        Context context = new Context();
        context.setVariable("v_usuario",m_usuario);
        context.setVariable("v_dados_responsaveis",s_cadastro.getDadosResponsavelByUsuario(m_usuario));
        // Add variaveis ao contexto
        m_respostaTexto.setMensagem(templateEngine.process("pv/contato_usuario",context));
        m_respostaTexto.setSucesso(true);
        // Retornar
        return m_respostaTexto;
    }

    /**
     * Obtêm um pedido baseado em um ID de um ticket
     */
    @PostMapping(path = "/obter_pedido")
    @ResponseBody
    M_RespostaTexto obterPedido(@RequestParam("id") String id) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        if (!s_ticket.validarIdTicket(id)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Pedido inválido");
            return m_respostaTexto;
        }

        M_Ticket m_ticket = s_ticket.getTicketById(Long.valueOf(id));

        List<M_Compra> m_compras = s_ticket.getComprasDeTicket(m_ticket);
        if (m_compras.isEmpty()) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não há nada neste pedido...");
            return m_respostaTexto;
        }

        Context context = new Context();
        context.setVariable("v_compras",m_compras);

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem(templateEngine.process("pv/produto_pedido", context));
        return m_respostaTexto;
    }

    /**
     * Cancela um ticket pelo vendedor
     * @return Div do pedido cancelado
     */
    @PostMapping("/pedidos/cancelar")
    @ResponseBody
    public M_RespostaTexto cancelarPedidoVendedor(HttpSession session,@RequestParam("id") String id) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();
        M_Ticket m_ticket;
        try {
            m_ticket = s_ticket.getTicketById(Long.parseLong(id));
        } catch (Exception e) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Erro ao encontrar pedido");
            return m_respostaTexto;
        }

        if (m_ticket.getStatus() == M_Ticket.StatusCompra.CANCELADO) {
            m_respostaTexto.setMensagem("O pedido já foi cancelado.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        if (m_ticket.getStatus() == M_Ticket.StatusCompra.ENTREGUE) {
            m_respostaTexto.setMensagem("O pedido já foi entregue.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        m_ticket = s_ticket.cancelarPedido(m_ticket,false);
        if (m_ticket==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não foi possível cancelar seu pedido devido a um erro no banco de dados.");
            return m_respostaTexto;
        }

        M_ViewPedido m_viewPedido = s_ticket.getPedidoFromTicket(m_ticket);
        if (m_viewPedido==null) {
            m_respostaTexto.setMensagem("Pedido foi cancelado mas houve um erro atualizando a tela.<br>Recarregue a página para atualizar seu pedido.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        Context context = new Context();
        context.setVariable("v_pedido",m_viewPedido);

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem(templateEngine.process("pv/pedido",context));
        return m_respostaTexto;
    }
}
