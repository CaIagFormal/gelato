package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Estoque;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_Resposta;
import com.tcc.gelato.service.*;
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
    private final S_Compra s_compra;
    private final S_Estoque s_estoque;

    public C_Produto(S_Produto s_produto, S_Cargo s_cargo, S_Ticket s_ticket, S_Compra s_compra, S_Estoque s_estoque) {
        this.s_produto = s_produto;
        this.s_cargo = s_cargo;
        this.s_ticket = s_ticket;
        this.s_compra = s_compra;
        this.s_estoque = s_estoque;
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
        model.addAttribute("estoque",s_estoque.getEstoqueForProduto(m_produto));

        if (s_cargo.validarCliente(m_usuario)) {
            M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
            model.addAttribute("qtd_itens_carrinho",session.getAttribute("qtd_itens_carrinho"));
            model.addAttribute("qtd_produto_carrinho",s_produto.getQtdDeProdutoEmTicket(m_produto,m_ticket));
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

        if (!s_compra.checkAdicionarAoCarrinhoValido(qtd,id_produto)) {
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

        int qtd_int = Integer.parseInt(qtd);

        if (!s_estoque.conferirValidadeDeEstoque(m_produto,-(qtd_int+s_produto.getQtdDeProdutoEmTicket(m_produto,m_ticket)))) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Quantidade inválida.");
            return m_resposta;
        }

        M_Compra m_compra = s_compra.gerarCompraDoCarrinho(m_usuario,m_ticket,m_produto,qtd_int);

        if (m_compra==null) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Não foi possível registrar sua compra.");
            return m_resposta;
        }

        // Atualizar quantidade de itens no carrinho
        session.setAttribute("qtd_itens_carrinho",s_compra.getQtdComprasDeTicket(m_ticket));

        m_resposta.setSucesso(true);
        m_resposta.setMensagem(qtd+" "+m_produto.getMedida()+"(s) de "+m_produto.getNome()+" foram adicionados ao seu carrinho.");
        return m_resposta;
    }

    /**
     * Cria {@link com.tcc.gelato.model.produto.M_Estoque} com
     * @param qtd Quantidade do produto
     * @param id_produto ID do produto
     * @param session Sessão para extrair o {@link M_Usuario}
     * @return Mensagem de sucesso ou falha para a página
     */
    @PostMapping(path="/adicionar_estoque")
    @ResponseBody
    public M_Resposta adicionarEstoque(@RequestParam("qtd") String qtd, @RequestParam("id_produto") String id_produto, HttpSession session) {
        M_Resposta m_resposta = new M_Resposta();
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);
        if (!s_cargo.validarVendedor(m_usuario)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Você não está cadastrado como vendedor.");
            return m_resposta;
        }

        if (!s_estoque.checkAdicionarEstoqueValido(qtd,id_produto)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Parâmetros inválidos.");
            return m_resposta;
        }

        M_Produto m_produto = s_produto.getProdutoById(Long.parseLong(id_produto));
        if (m_produto==null) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Produto inválido.");
            return m_resposta;
        }

        int qtd_int = Integer.parseInt(qtd);

        if (!s_estoque.conferirValidadeDeEstoque(m_produto,qtd_int)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Quantidade de estoque a ser adicionada é inválida.");
            return m_resposta;
        }

        M_Estoque m_estoque = s_estoque.gerarEstoque(m_produto,qtd_int);

        if (m_estoque==null) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Não foi possível alterar o estoque.");
            return m_resposta;
        }

        m_resposta.setSucesso(true);
        m_resposta.setMensagem(Math.abs(qtd_int) +" "+m_produto.getMedida()+"(s) de "+m_produto.getNome()+" foram "+((qtd_int>0)?"adicionados ao ":"removidos do ")+"estoque.");
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

        List<M_Compra> m_compras = s_compra.getComprasDeTicket(m_ticket);
        model.addAttribute("carrinho",m_compras);
        model.addAttribute("total",s_compra.getPrecoTotalDeCompras(m_compras));
        return "cliente/carrinho";
    }
}
