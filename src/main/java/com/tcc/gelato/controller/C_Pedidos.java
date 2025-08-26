package com.tcc.gelato.controller;

import com.tcc.gelato.model.produto.M_Ticket;
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
    M_RespostaTexto obterContatoCliente(@RequestParam("id") String id) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        if (!.validarGetUsuarioById(id)) {
            m_respostaTexto.setMensagem("O usuário solicitado não foi encontrado");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }
        // Processar página
        Context context = new Context();
        context.setVariable("v_usuario",);
        // Add variaveis ao contexto
        m_respostaTexto.setMensagem(templateEngine.process("pv/usuario :: pv_contato",context));
        m_respostaTexto.setSucesso(true);
        // Retornar
        return m_respostaTexto;
    }
}
