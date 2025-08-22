package com.tcc.gelato.controller;

import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.view.M_ViewPedido;
import com.tcc.gelato.service.S_Cargo;
import com.tcc.gelato.service.S_Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

/**
 * Controller regente a tela do vendedor de pedidos
 */
@Controller
public class C_Pedidos {

    private final S_Ticket s_ticket;
    private final S_Cargo s_cargo;

    public C_Pedidos(S_Ticket s_ticket,S_Cargo s_cargo) {
        this.s_ticket = s_ticket;
        this.s_cargo = s_cargo;
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
}
