package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_Resposta;
import com.tcc.gelato.service.S_Cargo;
import com.tcc.gelato.service.S_Produto;
import com.tcc.gelato.service.S_Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class C_Produto {

    private final S_Produto s_produto;
    private final S_Cargo s_cargo;

    private final S_Ticket s_ticket;

    public C_Produto(S_Produto s_produto,S_Cargo s_cargo,S_Ticket s_ticket) {
        this.s_produto = s_produto;
        this.s_cargo = s_cargo;
        this.s_ticket = s_ticket;
    }

    /**
     *
     * @param id_produto ID do produto na URL
     * @param session Sessão usada para conferir {@link M_Usuario} válido
     * @return Página do produto solicitado
     */
    @GetMapping(path="/produto/{produto}")
    public String getProduto(@PathVariable("produto") Long id_produto, HttpSession session, Model model) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);
        model.addAttribute("usuario",m_usuario);

        M_Produto m_produto = s_produto.getProdutoById(id_produto);

        model.addAttribute("produto",m_produto);
        model.addAttribute("estoque",s_produto.getEstoqueForProduto(m_produto));

        if (s_cargo.validarCliente(m_usuario)) {
            model.addAttribute("qtd_itens_carrinho",session.getAttribute("qtd_itens_carrinho"));
        }
        return "cliente/produto";
    }

    /**
     * Cria uma {@link com.tcc.gelato.model.M_Compra} com
     * @param qtd Quantidade do produto
     * @param id_produto ID do produto
     * @param session Sessão para extrair o {@link M_Usuario}
     * @return Mensagem de sucesso ou falha para a página
     */
    @PostMapping(path="/adicionar_carrinho")
    @ResponseBody
    public M_Resposta adicionarAoCarrinho(@RequestParam("qtd") String qtd, @RequestParam("id_produto") String id_produto, HttpSession session) {
        M_Resposta m_resposta = new M_Resposta();
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);
        if (!s_cargo.validarCliente(m_usuario)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Você não está cadastrado no momento.");
            return m_resposta;
        }

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);

        if (!s_produto.checkAdicionarAoCarrinhoValido(qtd,id_produto)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Sua compra é inválida.");
            return m_resposta;
        }

        M_Produto m_produto = s_produto.getProdutoById(Long.parseLong(id_produto));
        if (m_produto==null) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Produto inválido.");
            return m_resposta;
        }

        M_Compra m_compra = s_produto.gerarCompraDoCarrinho(m_usuario,m_ticket,m_produto,Integer.parseInt(qtd));

        if (m_compra==null) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Não foi possível registrar sua compra.");
            return m_resposta;
        }

        // Atualizar quantidade de itens no carrinho
        session.setAttribute("qtd_itens_carrinho",s_produto.getQtdComprasDeTicket(m_ticket));

        m_resposta.setSucesso(true);
        m_resposta.setMensagem(qtd+" "+m_produto.getMedida()+"(s) de "+m_produto.getNome()+" foram adicionados ao seu carrinho.");
        return m_resposta;
    }

    /**
     * @param session Redireciona para {@link C_Inicio#redirecionar(HttpSession)} caso não haja um {@link M_Usuario} vinculado
     * @return Tela de carrinho
     */
    @GetMapping("/carrinho")
    public String getCarrinho(HttpSession session, Model model) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);
        if (!s_cargo.validarCliente(m_usuario)) {
            return "redirect:/";
        }

        model.addAttribute("qtd_itens_carrinho",session.getAttribute("qtd_itens_carrinho"));

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
        model.addAttribute("ticket",m_ticket);

        List<M_Compra> m_compras = s_produto.getComprasDeTicket(m_ticket);
        model.addAttribute("carrinho",m_compras);
        model.addAttribute("total",s_produto.getPrecoTotalDeCompras(m_compras));
        return "cliente/carrinho";
    }
}
